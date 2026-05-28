-- ============================================
-- 05_triggers.sql (openGauss)
-- ============================================

-- Execute in gsql:
-- \c car_sales

-- ============================================
-- Trigger A:
-- trg_lock_car_on_order
-- On new sales_order insert, lock vehicle from IN_INVENTORY -> LOCKED
-- ============================================
CREATE OR REPLACE FUNCTION fn_lock_car_on_order()
RETURNS TRIGGER AS $$
DECLARE
    v_rows INT;
BEGIN
    -- BEFORE INSERT keeps the order row and vehicle lock in the same atomic write.
    UPDATE vehicle
    SET current_status = 'LOCKED'
    WHERE vehicle_vin = NEW.vehicle_vin
      AND current_status = 'IN_INVENTORY';

    GET DIAGNOSTICS v_rows = ROW_COUNT;
    IF v_rows = 0 THEN
        RAISE EXCEPTION 'Vehicle % is not in IN_INVENTORY, cannot lock for new order',
            NEW.vehicle_vin;
    END IF;

    INSERT INTO vehicle_status_log
        (vehicle_vin, from_status, to_status, changed_at, staff_id, reason)
    VALUES
        (NEW.vehicle_vin, 'IN_INVENTORY', 'LOCKED', CURRENT_TIMESTAMP, NEW.staff_id, 'Order created');

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_lock_car_on_order ON sales_order;
CREATE TRIGGER trg_lock_car_on_order
BEFORE INSERT ON sales_order
FOR EACH ROW
EXECUTE PROCEDURE fn_lock_car_on_order();

-- ============================================
-- Trigger B:
-- trg_update_inventory_on_delivery
-- On sales_order status change to COMPLETED, set vehicle LOCKED -> SOLD
-- and fill delivery_time if null
-- ============================================
CREATE OR REPLACE FUNCTION fn_update_inventory_on_delivery()
RETURNS TRIGGER AS $$
DECLARE
    v_rows INT;
BEGIN
    IF OLD.order_status <> 'COMPLETED' AND NEW.order_status = 'COMPLETED' THEN
        -- Completing an order is the only path that moves a locked vehicle to sold.
        UPDATE vehicle
        SET current_status = 'SOLD'
        WHERE vehicle_vin = NEW.vehicle_vin
          AND current_status = 'LOCKED';

        GET DIAGNOSTICS v_rows = ROW_COUNT;
        IF v_rows = 0 THEN
            RAISE EXCEPTION 'Vehicle % is not in LOCKED status, cannot mark SOLD for order %',
                NEW.vehicle_vin, NEW.order_id;
        END IF;

        IF NEW.delivery_time IS NULL THEN
            NEW.delivery_time := CURRENT_TIMESTAMP;
        END IF;

        INSERT INTO vehicle_status_log
            (vehicle_vin, from_status, to_status, changed_at, staff_id, reason)
        VALUES
            (NEW.vehicle_vin, 'LOCKED', 'SOLD', CURRENT_TIMESTAMP, NEW.staff_id, 'Order delivered');
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_update_inventory_on_delivery ON sales_order;
CREATE TRIGGER trg_update_inventory_on_delivery
BEFORE UPDATE OF order_status ON sales_order
FOR EACH ROW
EXECUTE PROCEDURE fn_update_inventory_on_delivery();

-- ============================================
-- Trigger C:
-- trg_prevent_invalid_order_cancel
-- On sales_order status change to CANCELLED:
-- 1) completed orders cannot be cancelled
-- 2) rollback vehicle status from LOCKED -> IN_INVENTORY
-- ============================================
CREATE OR REPLACE FUNCTION fn_prevent_invalid_order_cancel()
RETURNS TRIGGER AS $$
DECLARE
    v_rows INT;
BEGIN
    IF OLD.order_status <> 'CANCELLED' AND NEW.order_status = 'CANCELLED' THEN
        -- Delivered orders are final; only pending locked orders can release inventory.
        IF OLD.order_status = 'COMPLETED' THEN
            RAISE EXCEPTION 'Completed order % cannot be cancelled', NEW.order_id;
        END IF;

        UPDATE vehicle
        SET current_status = 'IN_INVENTORY'
        WHERE vehicle_vin = NEW.vehicle_vin
          AND current_status = 'LOCKED';

        GET DIAGNOSTICS v_rows = ROW_COUNT;
        IF v_rows = 0 THEN
            RAISE EXCEPTION 'Vehicle % is not in LOCKED status, cannot rollback for cancelled order %',
                NEW.vehicle_vin, NEW.order_id;
        END IF;

        INSERT INTO vehicle_status_log
            (vehicle_vin, from_status, to_status, changed_at, staff_id, reason)
        VALUES
            (NEW.vehicle_vin, 'LOCKED', 'IN_INVENTORY', CURRENT_TIMESTAMP, NEW.staff_id, 'Order cancelled');
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_prevent_invalid_order_cancel ON sales_order;
CREATE TRIGGER trg_prevent_invalid_order_cancel
BEFORE UPDATE OF order_status ON sales_order
FOR EACH ROW
EXECUTE PROCEDURE fn_prevent_invalid_order_cancel();
