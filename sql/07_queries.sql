-- ============================================
-- 07_queries.sql (openGauss)
-- Q1~Q8 business queries
-- ============================================

-- Execute in gsql:
-- \c car_sales

-- ==========================================================
-- Q1: 查询指定时间段内（先按月度）的销售统计
-- 说明：复用存储过程 sp_get_monthly_report（输入年份、月份）。
-- ==========================================================
BEGIN;
CALL sp_get_monthly_report(2026, 2, 'cur_monthly_report');
FETCH ALL FROM cur_monthly_report;
COMMIT;

-- ==========================================================
-- Q2: 查询每位销售顾问的月度/季度业绩并排名
-- 说明：复用视图 v_sales_performance，外层增加排名窗口函数。
-- ==========================================================
SELECT
    period_type,
    stat_year,
    stat_quarter,
    stat_month,
    period_label,
    staff_id,
    staff_no,
    staff_name,
    order_count,
    sales_amount,
    gross_profit,
    DENSE_RANK() OVER (
        PARTITION BY period_type, stat_year, stat_quarter, stat_month
        ORDER BY sales_amount DESC
    ) AS sales_rank
FROM v_sales_performance
WHERE period_type IN ('MONTH', 'QUARTER')
ORDER BY period_type, stat_year, stat_quarter, stat_month, sales_rank, staff_id;

-- ==========================================================
-- Q3: 查询最畅销车型 Top 5 及销量
-- 说明：原生 SQL，按 COMPLETED 订单统计车型销量。
-- ==========================================================
SELECT
    b.brand_name,
    m.model_name,
    m.model_year,
    m.trim_name,
    COUNT(*)::INT AS sales_volume
FROM sales_order so
JOIN vehicle v ON v.vehicle_vin = so.vehicle_vin
JOIN model m ON m.model_id = v.model_id
JOIN brand b ON b.brand_id = m.brand_id
WHERE so.order_status = 'COMPLETED'
GROUP BY b.brand_name, m.model_name, m.model_year, m.trim_name
ORDER BY sales_volume DESC, b.brand_name, m.model_name
LIMIT 5;

-- ==========================================================
-- Q4: 查询库存周期超过90天的滞销车辆清单
-- 说明：按当前库存口径统计，仅包含“仍在库(IN_INVENTORY)”且入库超90天的车辆。
-- ==========================================================
SELECT
    v.vehicle_vin,
    b.brand_name,
    m.model_name,
    m.model_year,
    m.trim_name,
    v.inventory_in_date,
    (CURRENT_DATE - v.inventory_in_date) AS inventory_days
FROM vehicle v
JOIN model m ON m.model_id = v.model_id
JOIN brand b ON b.brand_id = m.brand_id
WHERE v.current_status = 'IN_INVENTORY'
  AND v.inventory_in_date IS NOT NULL
  AND (CURRENT_DATE - v.inventory_in_date) > 90
ORDER BY inventory_days DESC, v.inventory_in_date ASC, v.vehicle_vin;

-- ==========================================================
-- Q5: 根据客户历史消费总额进行客户分类
-- 说明：复用视图 v_customer_value。
-- ==========================================================
SELECT
    customer_id,
    customer_name,
    phone,
    purchase_order_count,
    purchase_total_amount,
    service_order_count,
    service_total_fee,
    customer_level
FROM v_customer_value
ORDER BY purchase_total_amount DESC, customer_id;

-- ==========================================================
-- Q6: 查询特定客户的完整购车及服务历史
-- 说明：复用存储过程 sp_get_customer_full_history。
-- 参数示例：customer_id = 1001
-- ==========================================================
BEGIN;
CALL sp_get_customer_full_history(1001, 'cur_customer_history');
FETCH ALL FROM cur_customer_history;
COMMIT;

-- ==========================================================
-- Q7: 生成库存预警报表（库存低于安全库存阈值）
-- 说明：复用视图 v_inventory_summary + model.safety_stock_threshold。
-- ==========================================================
SELECT
    inv.model_id,
    inv.brand_name,
    inv.model_name,
    inv.model_year,
    inv.trim_name,
    m.safety_stock_threshold,
    inv.in_inventory_count,
    (m.safety_stock_threshold - inv.in_inventory_count) AS shortage_count
FROM v_inventory_summary inv
JOIN model m ON m.model_id = inv.model_id
WHERE inv.in_inventory_count < m.safety_stock_threshold
ORDER BY shortage_count DESC, inv.brand_name, inv.model_name;

-- ==========================================================
-- Q8: 自定义复杂查询（业务价值）
-- 题目：销售顾问订单取消率与潜在损失分析（识别高风险顾问）
-- ==========================================================
SELECT
    st.staff_id,
    st.staff_name,
    COUNT(*)::INT AS total_orders,
    SUM(CASE WHEN so.order_status = 'CANCELLED' THEN 1 ELSE 0 END)::INT AS cancelled_orders,
    ROUND(
        SUM(CASE WHEN so.order_status = 'CANCELLED' THEN 1 ELSE 0 END)::NUMERIC
        / NULLIF(COUNT(*), 0),
        4
    ) AS cancel_rate,
    COALESCE(
        SUM(CASE WHEN so.order_status = 'CANCELLED' THEN so.total_amount ELSE 0 END),
        0
    )::NUMERIC(14, 2) AS cancelled_amount
FROM sales_order so
JOIN staff st ON st.staff_id = so.staff_id
GROUP BY st.staff_id, st.staff_name
HAVING COUNT(*) >= 1
ORDER BY cancel_rate DESC, cancelled_amount DESC, total_orders DESC;
