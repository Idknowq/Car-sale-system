-- ============================================
-- 03_views.sql (openGauss)
-- ============================================

-- 1) Sales performance view
--    Scope: COMPLETED orders only
--    Gross profit: total_amount - purchase_cost
CREATE OR REPLACE VIEW v_sales_performance AS
SELECT
    so.staff_id,
    st.staff_no,
    st.staff_name,
    'MONTH'::VARCHAR(10) AS period_type,
    EXTRACT(YEAR FROM so.created_at)::INT AS stat_year,
    EXTRACT(QUARTER FROM so.created_at)::INT AS stat_quarter,
    EXTRACT(MONTH FROM so.created_at)::INT AS stat_month,
    TO_CHAR(so.created_at, 'YYYY-MM') AS period_label,
    COUNT(*)::INT AS order_count,
    SUM(so.total_amount)::NUMERIC(14, 2) AS sales_amount,
    SUM(so.total_amount - v.purchase_cost)::NUMERIC(14, 2) AS gross_profit
FROM sales_order so
JOIN staff st ON st.staff_id = so.staff_id
JOIN vehicle v ON v.vehicle_vin = so.vehicle_vin
WHERE so.order_status = 'COMPLETED'
GROUP BY
    so.staff_id, st.staff_no, st.staff_name,
    EXTRACT(YEAR FROM so.created_at),
    EXTRACT(QUARTER FROM so.created_at),
    EXTRACT(MONTH FROM so.created_at),
    TO_CHAR(so.created_at, 'YYYY-MM')

UNION ALL

SELECT
    so.staff_id,
    st.staff_no,
    st.staff_name,
    'QUARTER'::VARCHAR(10) AS period_type,
    EXTRACT(YEAR FROM so.created_at)::INT AS stat_year,
    EXTRACT(QUARTER FROM so.created_at)::INT AS stat_quarter,
    NULL::INT AS stat_month,
    TO_CHAR(so.created_at, 'YYYY') || '-Q' || EXTRACT(QUARTER FROM so.created_at)::INT AS period_label,
    COUNT(*)::INT AS order_count,
    SUM(so.total_amount)::NUMERIC(14, 2) AS sales_amount,
    SUM(so.total_amount - v.purchase_cost)::NUMERIC(14, 2) AS gross_profit
FROM sales_order so
JOIN staff st ON st.staff_id = so.staff_id
JOIN vehicle v ON v.vehicle_vin = so.vehicle_vin
WHERE so.order_status = 'COMPLETED'
GROUP BY
    so.staff_id, st.staff_no, st.staff_name,
    EXTRACT(YEAR FROM so.created_at),
    EXTRACT(QUARTER FROM so.created_at)

UNION ALL

SELECT
    so.staff_id,
    st.staff_no,
    st.staff_name,
    'YEAR'::VARCHAR(10) AS period_type,
    EXTRACT(YEAR FROM so.created_at)::INT AS stat_year,
    NULL::INT AS stat_quarter,
    NULL::INT AS stat_month,
    TO_CHAR(so.created_at, 'YYYY') AS period_label,
    COUNT(*)::INT AS order_count,
    SUM(so.total_amount)::NUMERIC(14, 2) AS sales_amount,
    SUM(so.total_amount - v.purchase_cost)::NUMERIC(14, 2) AS gross_profit
FROM sales_order so
JOIN staff st ON st.staff_id = so.staff_id
JOIN vehicle v ON v.vehicle_vin = so.vehicle_vin
WHERE so.order_status = 'COMPLETED'
GROUP BY
    so.staff_id, st.staff_no, st.staff_name,
    EXTRACT(YEAR FROM so.created_at);

-- 2) Inventory summary view (real-time by model)
CREATE OR REPLACE VIEW v_inventory_summary AS
SELECT
    m.model_id,
    b.brand_name,
    m.model_name,
    m.model_year,
    m.trim_name,
    COALESCE(SUM(CASE WHEN v.current_status = 'IN_INVENTORY' THEN 1 ELSE 0 END), 0)::INT AS in_inventory_count,
    COALESCE(SUM(CASE WHEN v.current_status = 'LOCKED' THEN 1 ELSE 0 END), 0)::INT AS locked_count,
    COALESCE(SUM(CASE WHEN v.current_status = 'IN_TRANSIT' THEN 1 ELSE 0 END), 0)::INT AS in_transit_count
FROM model m
JOIN brand b ON b.brand_id = m.brand_id
LEFT JOIN vehicle v ON v.model_id = m.model_id
GROUP BY m.model_id, b.brand_name, m.model_name, m.model_year, m.trim_name;

-- 3) Customer value view
--    Purchase and after-sales are separated into two columns
CREATE OR REPLACE VIEW v_customer_value AS
SELECT
    c.customer_id,
    c.customer_name,
    c.phone,
    COALESCE(so_stats.purchase_order_count, 0)::INT AS purchase_order_count,
    COALESCE(so_stats.purchase_total_amount, 0)::NUMERIC(14, 2) AS purchase_total_amount,
    COALESCE(so_stats.last_purchase_time, NULL::TIMESTAMP) AS last_purchase_time,
    COALESCE(sv_stats.service_order_count, 0)::INT AS service_order_count,
    COALESCE(sv_stats.service_total_fee, 0)::NUMERIC(14, 2) AS service_total_fee,
    CASE
        WHEN COALESCE(so_stats.purchase_total_amount, 0) < 100000 THEN 'REGULAR'
        WHEN COALESCE(so_stats.purchase_total_amount, 0) <= 300000 THEN 'SILVER'
        ELSE 'GOLD'
    END AS customer_level
FROM customer c
LEFT JOIN (
    SELECT
        so.customer_id,
        COUNT(*) AS purchase_order_count,
        SUM(so.total_amount) AS purchase_total_amount,
        MAX(so.delivery_time) AS last_purchase_time
    FROM sales_order so
    WHERE so.order_status = 'COMPLETED'
    GROUP BY so.customer_id
) so_stats ON so_stats.customer_id = c.customer_id
LEFT JOIN (
    SELECT
        s.customer_id,
        COUNT(*) AS service_order_count,
        SUM(s.total_fee) AS service_total_fee
    FROM service_order s
    WHERE s.status = 'COMPLETED'
    GROUP BY s.customer_id
) sv_stats ON sv_stats.customer_id = c.customer_id;
