-- ============================================
-- 06_procedures.sql (openGauss)
-- ============================================

-- Execute in gsql:
-- \c car_sales

-- ==========================================================
-- Procedure 1: sp_create_sales_order
-- Create sales order + order items in one transaction scope.
-- Vehicle locking is handled by trigger trg_lock_car_on_order.
-- ==========================================================
CREATE OR REPLACE PROCEDURE sp_create_sales_order(
    IN  p_customer_id       INT,
    IN  p_staff_id          INT,
    IN  p_vehicle_vin       CHAR(17),
    IN  p_deposit_amount    NUMERIC(12, 2),
    IN  p_vehicle_amount    NUMERIC(12, 2),
    IN  p_option_amount     NUMERIC(12, 2) DEFAULT 0,
    IN  p_insurance_amount  NUMERIC(12, 2) DEFAULT 0,
    IN  p_discount_amount   NUMERIC(12, 2) DEFAULT 0,
    IN  p_other_amount      NUMERIC(12, 2) DEFAULT 0,
    OUT p_order_id          INT
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_total_amount   NUMERIC(12, 2);
    v_vehicle_status VARCHAR(20);
BEGIN
    -- Basic reference checks
    IF NOT EXISTS (SELECT 1 FROM customer c WHERE c.customer_id = p_customer_id) THEN
        RAISE EXCEPTION 'Customer % does not exist', p_customer_id;
    END IF;

    IF NOT EXISTS (SELECT 1 FROM staff s WHERE s.staff_id = p_staff_id) THEN
        RAISE EXCEPTION 'Staff % does not exist', p_staff_id;
    END IF;

    -- Lock target vehicle row and validate status
    SELECT v.current_status
      INTO v_vehicle_status
      FROM vehicle v
     WHERE v.vehicle_vin = p_vehicle_vin
     FOR UPDATE;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'Vehicle % does not exist', p_vehicle_vin;
    END IF;

    IF v_vehicle_status <> 'IN_INVENTORY' THEN
        RAISE EXCEPTION 'Vehicle % status is %, expected IN_INVENTORY',
            p_vehicle_vin, v_vehicle_status;
    END IF;

    -- Amount checks
    IF p_vehicle_amount <= 0 THEN
        RAISE EXCEPTION 'Vehicle amount must be > 0';
    END IF;

    v_total_amount :=
        COALESCE(p_vehicle_amount, 0) +
        COALESCE(p_option_amount, 0) +
        COALESCE(p_insurance_amount, 0) +
        COALESCE(p_discount_amount, 0) +
        COALESCE(p_other_amount, 0);

    IF v_total_amount <= 0 THEN
        RAISE EXCEPTION 'Total amount must be > 0';
    END IF;

    IF p_deposit_amount < 0 OR p_deposit_amount > v_total_amount THEN
        RAISE EXCEPTION 'Invalid deposit amount %, total amount %',
            p_deposit_amount, v_total_amount;
    END IF;

    -- Insert order
    INSERT INTO sales_order (
        customer_id, staff_id, vehicle_vin, total_amount, deposit_amount, order_status, created_at
    ) VALUES (
        p_customer_id, p_staff_id, p_vehicle_vin, v_total_amount, p_deposit_amount, 'LOCKED', CURRENT_TIMESTAMP
    )
    RETURNING order_id INTO p_order_id;

    -- Insert order items (only non-zero rows)
    INSERT INTO order_item (order_id, item_type, item_desc, amount)
    VALUES (p_order_id, 'VEHICLE', 'Vehicle price', p_vehicle_amount);

    IF COALESCE(p_option_amount, 0) <> 0 THEN
        INSERT INTO order_item (order_id, item_type, item_desc, amount)
        VALUES (p_order_id, 'OPTION', 'Optional package', p_option_amount);
    END IF;

    IF COALESCE(p_insurance_amount, 0) <> 0 THEN
        INSERT INTO order_item (order_id, item_type, item_desc, amount)
        VALUES (p_order_id, 'INSURANCE', 'Insurance package', p_insurance_amount);
    END IF;

    IF COALESCE(p_discount_amount, 0) <> 0 THEN
        INSERT INTO order_item (order_id, item_type, item_desc, amount)
        VALUES (p_order_id, 'DISCOUNT', 'Discount adjustment', p_discount_amount);
    END IF;

    IF COALESCE(p_other_amount, 0) <> 0 THEN
        INSERT INTO order_item (order_id, item_type, item_desc, amount)
        VALUES (p_order_id, 'OTHER', 'Other fees', p_other_amount);
    END IF;
END;
$$;

-- ==========================================================
-- Procedure 2: sp_get_monthly_report
-- Return monthly completed-order report via refcursor.
-- ==========================================================
CREATE OR REPLACE PROCEDURE sp_get_monthly_report(
    IN     p_year      INT,
    IN     p_month     INT,
    INOUT  p_result    REFCURSOR DEFAULT 'cur_monthly_report'
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_start_ts TIMESTAMP;
    v_end_ts   TIMESTAMP;
BEGIN
    IF p_month < 1 OR p_month > 12 THEN
        RAISE EXCEPTION 'Invalid month %, expected 1..12', p_month;
    END IF;

    v_start_ts := to_timestamp(p_year::TEXT || '-' || lpad(p_month::TEXT, 2, '0') || '-01', 'YYYY-MM-DD');
    v_end_ts := v_start_ts + INTERVAL '1 month';

    OPEN p_result FOR
    SELECT
        p_year AS stat_year,
        p_month AS stat_month,
        st.staff_id,
        st.staff_name,
        COUNT(*)::INT AS order_count,
        SUM(so.total_amount)::NUMERIC(14, 2) AS sales_amount,
        SUM(so.total_amount - v.purchase_cost)::NUMERIC(14, 2) AS gross_profit
    FROM sales_order so
    JOIN staff st ON st.staff_id = so.staff_id
    JOIN vehicle v ON v.vehicle_vin = so.vehicle_vin
    WHERE so.order_status = 'COMPLETED'
      AND so.created_at >= v_start_ts
      AND so.created_at < v_end_ts
    GROUP BY st.staff_id, st.staff_name
    ORDER BY sales_amount DESC, gross_profit DESC;
END;
$$;

-- ==========================================================
-- Procedure 3: sp_get_customer_full_history
-- Return complete customer purchase + after-sales timeline.
-- ==========================================================
CREATE OR REPLACE PROCEDURE sp_get_customer_full_history(
    IN     p_customer_id INT,
    INOUT  p_result      REFCURSOR DEFAULT 'cur_customer_history'
)
LANGUAGE plpgsql
AS $$
BEGIN
    OPEN p_result FOR
    SELECT
        t.customer_id,
        t.event_time,
        t.event_type,
        t.ref_no,
        t.status,
        t.amount,
        t.vehicle_vin,
        t.staff_name
    FROM (
        SELECT
            so.customer_id,
            so.created_at AS event_time,
            'SALES_ORDER'::VARCHAR(20) AS event_type,
            so.order_id::VARCHAR(50) AS ref_no,
            so.order_status AS status,
            so.total_amount AS amount,
            so.vehicle_vin,
            st.staff_name
        FROM sales_order so
        JOIN staff st ON st.staff_id = so.staff_id
        WHERE so.customer_id = p_customer_id

        UNION ALL

        SELECT
            sv.customer_id,
            sv.created_at AS event_time,
            'SERVICE_ORDER'::VARCHAR(20) AS event_type,
            sv.service_order_id::VARCHAR(50) AS ref_no,
            sv.status AS status,
            sv.total_fee AS amount,
            sv.vehicle_vin,
            st.staff_name
        FROM service_order sv
        JOIN staff st ON st.staff_id = sv.staff_id
        WHERE sv.customer_id = p_customer_id
    ) t
    ORDER BY t.event_time, t.event_type;
END;
$$;

-- ==========================================================
-- Future procedure candidates (for iterative upgrade)
-- ==========================================================
-- 1) sp_cancel_sales_order
--    Cancel order, release vehicle lock, write status log, and handle refund status.
--
-- 2) sp_pay_deposit
--    Record deposit payment and update order status from LOCKED to DEPOSIT_PAID.
--
-- 3) sp_complete_delivery
--    Complete delivery workflow, finalize payment checks, and mark order COMPLETED.
--
-- 4) sp_vehicle_inbound
--    Inbound vehicle from transit to inventory with status/audit logging.
--
-- 5) sp_transfer_vehicle
--    Transfer vehicle between locations/stores with trace records.
--
-- 6) sp_create_service_order
--    Create after-sales service order with initial service items.
--
-- 7) sp_settle_service_order
--    Recalculate and settle service order total fee, then close work order.
--
-- 8) sp_rebuild_order_total
--    Recompute sales_order.total_amount from order_item for data repair.
--
-- 9) sp_get_inventory_alert
--    Return models below safety_stock_threshold for replenishment alerts.
