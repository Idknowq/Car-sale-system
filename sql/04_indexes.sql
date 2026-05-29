-- ============================================
-- 04_indexes.sql (openGauss)
-- ============================================

-- 1) 优化按时间范围查询销售订单（Q1月/季/年统计、月报查询）
CREATE INDEX idx_sales_order_created_at
ON sales_order (created_at);

-- 2) 优化按顾问+时间维度统计业绩（Q2销售顾问业绩榜）
CREATE INDEX idx_sales_order_staff_created_at
ON sales_order (staff_id, created_at);

-- 3) 优化按订单状态+时间统计（只统计COMPLETED时减少扫描）
CREATE INDEX idx_sales_order_status_created_at
ON sales_order (order_status, created_at);

-- 4) 优化库存汇总与预警查询（按状态、车型聚合）
CREATE INDEX idx_vehicle_status_model
ON vehicle (current_status, model_id);

-- 5) 优化按车型统计库存（模型维度下钻、库存结构分析）
CREATE INDEX idx_vehicle_model_id
ON vehicle (model_id);

-- 6) 优化按手机号定位客户（销售前台高频精确查询）
CREATE INDEX idx_customer_phone
ON customer (phone);

-- 7) 优化客户购车历史查询（Q6客户完整购车历史按时间线）
CREATE INDEX idx_sales_order_customer_created_at
ON sales_order (customer_id, created_at);

-- 8) 优化客户售后历史查询（Q6客户完整售后历史按时间线）
CREATE INDEX idx_service_order_customer_created_at
ON service_order (customer_id, created_at);

-- 9) 防止同一车辆存在多个有效销售订单，同时允许取消后重新下单
CREATE UNIQUE INDEX uk_sales_order_vehicle_vin_active
ON sales_order (vehicle_vin)
WHERE order_status <> 'CANCELLED';