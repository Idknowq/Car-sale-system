-- ============================================
-- 02_init_data.sql (openGauss)
-- Sample data for car_sales
-- ============================================

-- Execute in gsql:
-- \c car_sales

BEGIN;

-- =========================
-- 1) brand (>=3)
-- =========================
INSERT INTO brand (brand_id, brand_name) VALUES
(1, 'Mercedes-Benz'),
(2, 'BMW'),
(3, 'Audi'),
(4, 'Porsche'),
(5, 'Tesla');

-- =========================
-- 2) model (>=10)
-- =========================
INSERT INTO model (model_id, brand_id, model_name, model_year, trim_name, msrp, engine_displacement, model_type, safety_stock_threshold) VALUES
(101, 1, 'C-Class', 2026, 'C 260 L Sport', 359800.00, 2.0, 'Sedan', 2),
(102, 1, 'E-Class', 2026, 'E 300 L Luxury', 489800.00, 2.0, 'Sedan', 2),
(103, 1, 'GLC SUV', 2026, 'GLC 260 L 4MATIC', 429800.00, 2.0, 'SUV', 3),
(201, 2, '3 Series', 2026, '325Li M Sport', 339900.00, 2.0, 'Sedan', 2),
(202, 2, '5 Series', 2026, '530Li xDrive', 499900.00, 2.0, 'Sedan', 2),
(203, 2, 'X3', 2026, 'xDrive30i M Sport', 439900.00, 2.0, 'SUV', 2),
(301, 3, 'A4L', 2026, '40 TFSI Luxury', 329800.00, 2.0, 'Sedan', 2),
(302, 3, 'A6L', 2026, '45 TFSI Quattro', 469800.00, 2.0, 'Sedan', 2),
(303, 3, 'Q5L', 2026, '45 TFSI Luxury', 419800.00, 2.0, 'SUV', 3),
(401, 4, 'Cayenne', 2026, 'Base', 918000.00, 3.0, 'SUV', 1),
(402, 4, 'Macan', 2026, 'Base', 578000.00, 2.0, 'SUV', 1),
(501, 5, 'Model Y', 2026, 'Long Range AWD', 299900.00, 0.1, 'SUV', 4);

-- =========================
-- 3) staff (>=10)
-- =========================
INSERT INTO staff (staff_id, staff_no, staff_name, role, department, hire_date, manager_id) VALUES
(1, 'S0001', 'Li Wei', 'Sales Director', 'Sales', '2020-03-01', NULL),
(2, 'S0002', 'Zhang Min', 'Sales Manager', 'Sales', '2021-05-10', 1),
(3, 'S0003', 'Wang Lei', 'Sales Consultant', 'Sales', '2022-01-15', 2),
(4, 'S0004', 'Chen Yu', 'Sales Consultant', 'Sales', '2022-06-20', 2),
(5, 'S0005', 'Liu Yang', 'Sales Consultant', 'Sales', '2023-02-18', 2),
(6, 'S0006', 'Zhao Qian', 'Inventory Manager', 'Inventory', '2021-08-08', 1),
(7, 'S0007', 'Sun Hao', 'Inventory Clerk', 'Inventory', '2023-04-12', 6),
(8, 'S0008', 'Guo Lin', 'Service Manager', 'Service', '2020-11-01', 1),
(9, 'S0009', 'Ma Chao', 'Service Advisor', 'Service', '2022-09-09', 8),
(10, 'S0010', 'He Jing', 'Service Advisor', 'Service', '2023-03-03', 8);

-- =========================
-- 4) customer (>=20)
-- =========================
INSERT INTO customer (customer_id, customer_name, gender, phone, id_card, address, first_visit_date, source_channel) VALUES
(1001, 'Chen Ming', 'M', '13800000001', '310101198901010011', 'Shanghai Pudong', '2026-01-03', 'WALK_IN'),
(1002, 'Li Na', 'F', '13800000002', '310101199002020022', 'Shanghai Minhang', '2026-01-05', 'ONLINE_AD'),
(1003, 'Wang Bin', 'M', '13800000003', '310101198803030033', 'Shanghai Jingan', '2026-01-07', 'REFERRAL'),
(1004, 'Zhao Xin', 'F', '13800000004', '310101199104040044', 'Shanghai Xuhui', '2026-01-09', 'SOCIAL_MEDIA'),
(1005, 'Liu Qiang', 'M', '13800000005', '310101198705050055', 'Shanghai Yangpu', '2026-01-11', 'WALK_IN'),
(1006, 'Sun Mei', 'F', '13800000006', '310101199206060066', 'Shanghai Changning', '2026-01-13', 'PHONE'),
(1007, 'Guo Peng', 'M', '13800000007', '310101198607070077', 'Shanghai Baoshan', '2026-01-15', 'ONLINE_AD'),
(1008, 'He Fang', 'F', '13800000008', '310101199308080088', 'Shanghai Songjiang', '2026-01-17', 'WALK_IN'),
(1009, 'Xu Lei', 'M', '13800000009', '310101198909090099', 'Shanghai Qingpu', '2026-01-19', 'REFERRAL'),
(1010, 'Tang Yue', 'F', '13800000010', '310101199010100010', 'Shanghai Hongkou', '2026-01-21', 'ONLINE_AD'),
(1011, 'Deng Bo', 'M', '13800000011', '310101198811110011', 'Shanghai Putuo', '2026-02-01', 'SOCIAL_MEDIA'),
(1012, 'Yuan Jing', 'F', '13800000012', '310101199212120022', 'Shanghai Jinshan', '2026-02-03', 'PHONE'),
(1013, 'Cao Rui', 'M', '13800000013', '310101198713130033', 'Shanghai Fengxian', '2026-02-05', 'WALK_IN'),
(1014, 'Qin Lan', 'F', '13800000014', '310101199114140044', 'Shanghai Pudong', '2026-02-07', 'REFERRAL'),
(1015, 'Hu Tao', 'M', '13800000015', '310101198615150055', 'Shanghai Minhang', '2026-02-09', 'ONLINE_AD'),
(1016, 'Song Yi', 'F', '13800000016', '310101199316160066', 'Shanghai Jingan', '2026-02-11', 'SOCIAL_MEDIA'),
(1017, 'Pan Hao', 'M', '13800000017', '310101198917170077', 'Shanghai Xuhui', '2026-02-13', 'WALK_IN'),
(1018, 'Zheng Min', 'F', '13800000018', '310101199018180088', 'Shanghai Yangpu', '2026-02-15', 'PHONE'),
(1019, 'Feng Kai', 'M', '13800000019', '310101198819190099', 'Shanghai Changning', '2026-02-17', 'ONLINE_AD'),
(1020, 'Jiang Xin', 'F', '13800000020', '310101199220200020', 'Shanghai Baoshan', '2026-02-19', 'REFERRAL');

-- =========================
-- 5) vehicle (>=30, mixed status)
-- =========================
INSERT INTO vehicle (vehicle_vin, model_id, engine_no, color, manufacture_date, inventory_in_date, purchase_cost, suggested_retail_price, current_status) VALUES
('VIN00000000000001', 101, 'ENG20260001', 'Black', '2025-12-10', '2026-01-02', 298000.00, 359800.00, 'SOLD'),
('VIN00000000000002', 101, 'ENG20260002', 'White', '2025-12-12', '2026-01-03', 300000.00, 359800.00, 'SOLD'),
('VIN00000000000003', 102, 'ENG20260003', 'Black', '2025-12-18', '2026-01-05', 402000.00, 489800.00, 'SOLD'),
('VIN00000000000004', 103, 'ENG20260004', 'Grey',  '2025-12-22', '2026-01-06', 350000.00, 429800.00, 'SOLD'),
('VIN00000000000005', 201, 'ENG20260005', 'Blue',  '2025-12-25', '2026-01-08', 278000.00, 339900.00, 'SOLD'),
('VIN00000000000006', 202, 'ENG20260006', 'Black', '2025-12-26', '2026-01-09', 410000.00, 499900.00, 'SOLD'),
('VIN00000000000007', 203, 'ENG20260007', 'White', '2025-12-28', '2026-01-10', 360000.00, 439900.00, 'SOLD'),
('VIN00000000000008', 301, 'ENG20260008', 'Silver','2025-12-29', '2026-01-11', 270000.00, 329800.00, 'SOLD'),
('VIN00000000000009', 302, 'ENG20260009', 'Black', '2025-12-30', '2026-01-12', 385000.00, 469800.00, 'SOLD'),
('VIN00000000000010', 303, 'ENG20260010', 'Grey',  '2026-01-02', '2026-01-13', 345000.00, 419800.00, 'SOLD'),
('VIN00000000000011', 401, 'ENG20260011', 'White', '2026-01-03', '2026-01-14', 760000.00, 918000.00, 'LOCKED'),
('VIN00000000000012', 402, 'ENG20260012', 'Blue',  '2026-01-04', '2026-01-15', 470000.00, 578000.00, 'LOCKED'),
('VIN00000000000013', 501, 'ENG20260013', 'White', '2026-01-05', '2026-01-16', 255000.00, 299900.00, 'LOCKED'),
('VIN00000000000014', 501, 'ENG20260014', 'Black', '2026-01-06', '2026-01-17', 257000.00, 299900.00, 'LOCKED'),
('VIN00000000000015', 103, 'ENG20260015', 'Red',   '2026-01-06', '2026-01-18', 352000.00, 429800.00, 'LOCKED'),
('VIN00000000000016', 203, 'ENG20260016', 'Grey',  '2026-01-07', '2026-01-19', 362000.00, 439900.00, 'LOCKED'),
('VIN00000000000017', 302, 'ENG20260017', 'White', '2026-01-08', '2026-01-20', 386000.00, 469800.00, 'LOCKED'),
('VIN00000000000018', 401, 'ENG20260018', 'Black', '2026-01-09', '2026-01-21', 765000.00, 918000.00, 'LOCKED'),
('VIN00000000000019', 101, 'ENG20260019', 'White', '2026-01-10', '2026-01-22', 301000.00, 359800.00, 'LOCKED'),
('VIN00000000000020', 102, 'ENG20260020', 'Grey',  '2026-01-11', '2026-01-23', 404000.00, 489800.00, 'LOCKED'),
('VIN00000000000021', 201, 'ENG20260021', 'Blue',  '2026-01-12', '2026-01-24', 279000.00, 339900.00, 'IN_INVENTORY'),
('VIN00000000000022', 301, 'ENG20260022', 'Black', '2026-01-13', '2026-01-25', 271000.00, 329800.00, 'IN_INVENTORY'),
('VIN00000000000023', 303, 'ENG20260023', 'White', '2026-01-14', '2026-01-26', 347000.00, 419800.00, 'IN_INVENTORY'),
('VIN00000000000024', 402, 'ENG20260024', 'Grey',  '2026-01-15', '2026-01-27', 472000.00, 578000.00, 'IN_INVENTORY'),
('VIN00000000000025', 501, 'ENG20260025', 'Blue',  '2026-01-16', '2026-01-28', 258000.00, 299900.00, 'IN_INVENTORY'),
('VIN00000000000026', 202, 'ENG20260026', 'Black', '2026-01-17', '2026-01-29', 412000.00, 499900.00, 'IN_INVENTORY'),
('VIN00000000000027', 103, 'ENG20260027', 'Silver','2026-01-18', '2026-01-30', 353000.00, 429800.00, 'IN_INVENTORY'),
('VIN00000000000028', 203, 'ENG20260028', 'White', '2026-01-19', '2026-01-31', 364000.00, 439900.00, 'IN_INVENTORY'),
('VIN00000000000029', 302, 'ENG20260029', 'Grey',  '2026-01-20', NULL,         388000.00, 469800.00, 'IN_TRANSIT'),
('VIN00000000000030', 401, 'ENG20260030', 'Black', '2026-01-21', NULL,         770000.00, 918000.00, 'IN_TRANSIT'),
('VIN00000000000031', 402, 'ENG20260031', 'Red',   '2026-01-22', NULL,         474000.00, 578000.00, 'IN_TRANSIT'),
('VIN00000000000032', 501, 'ENG20260032', 'White', '2026-01-23', NULL,         259000.00, 299900.00, 'IN_TRANSIT');

-- =========================
-- 6) customer_intent
-- =========================
INSERT INTO customer_intent (intent_id, customer_id, model_id, intent_level, remark, staff_id, next_contact_time, created_at) VALUES
(1, 1001, 101, 'A', 'Budget confirmed', 3, '2026-02-10 10:00:00', '2026-02-01 10:00:00'),
(2, 1002, 501, 'A', 'Need immediate delivery', 4, '2026-02-11 11:00:00', '2026-02-01 11:00:00'),
(3, 1003, 203, 'B', 'Compare with Q5L', 5, '2026-02-12 15:00:00', '2026-02-02 09:30:00'),
(4, 1004, 302, 'B', 'Family use', 3, '2026-02-13 14:00:00', '2026-02-02 14:00:00'),
(5, 1005, 401, 'A', 'Trade-in possible', 4, '2026-02-14 16:00:00', '2026-02-03 10:20:00'),
(6, 1006, 402, 'C', 'Waiting for discount', 5, '2026-02-15 09:00:00', '2026-02-03 16:40:00'),
(7, 1007, 102, 'B', 'Corporate purchase', 3, '2026-02-16 11:30:00', '2026-02-04 09:10:00'),
(8, 1008, 201, 'A', 'Loan preferred', 4, '2026-02-17 13:30:00', '2026-02-04 13:10:00'),
(9, 1009, 303, 'B', 'Need white color', 5, '2026-02-18 10:30:00', '2026-02-05 10:30:00'),
(10, 1010, 103, 'A', 'High urgency', 3, '2026-02-19 17:00:00', '2026-02-05 17:00:00'),
(11, 1011, 301, 'C', 'Just inquiry', 4, '2026-02-20 10:00:00', '2026-02-06 09:00:00'),
(12, 1012, 202, 'B', 'Need test drive', 5, '2026-02-21 15:30:00', '2026-02-06 15:30:00'),
(13, 1013, 501, 'A', 'Ready to pay deposit', 3, '2026-02-22 11:00:00', '2026-02-07 11:00:00'),
(14, 1014, 402, 'B', 'Color undecided', 4, '2026-02-23 16:00:00', '2026-02-07 16:00:00'),
(15, 1015, 302, 'A', 'Urgent replacement', 5, '2026-02-24 09:30:00', '2026-02-08 09:30:00'),
(16, 1016, 201, 'B', 'Needs financing plan', 3, '2026-02-25 14:30:00', '2026-02-08 14:30:00'),
(17, 1017, 401, 'C', 'Price-sensitive', 4, '2026-02-26 10:30:00', '2026-02-09 10:30:00'),
(18, 1018, 103, 'B', 'Compares with X3', 5, '2026-02-27 15:00:00', '2026-02-09 15:00:00'),
(19, 1019, 202, 'A', 'Ready this month', 3, '2026-02-28 11:30:00', '2026-02-10 11:30:00'),
(20, 1020, 301, 'B', 'Need old car appraisal', 4, '2026-03-01 13:00:00', '2026-02-10 13:00:00');

-- =========================
-- 7) sales_order (>=20, mixed status)
-- =========================
INSERT INTO sales_order (order_id, customer_id, staff_id, vehicle_vin, total_amount, deposit_amount, order_status, created_at, delivery_time) VALUES
(5001, 1001, 3, 'VIN00000000000001', 366800.00, 50000.00, 'COMPLETED', '2026-01-20 10:00:00', '2026-01-25 15:00:00'),
(5002, 1002, 4, 'VIN00000000000002', 359800.00, 60000.00, 'COMPLETED', '2026-01-21 11:00:00', '2026-01-28 16:00:00'),
(5003, 1003, 5, 'VIN00000000000003', 497800.00, 80000.00, 'COMPLETED', '2026-01-22 14:00:00', '2026-01-31 14:30:00'),
(5004, 1004, 3, 'VIN00000000000004', 438800.00, 70000.00, 'COMPLETED', '2026-01-23 10:30:00', '2026-02-01 11:00:00'),
(5005, 1005, 4, 'VIN00000000000005', 344900.00, 50000.00, 'COMPLETED', '2026-01-24 09:30:00', '2026-02-02 10:00:00'),
(5006, 1006, 5, 'VIN00000000000006', 506900.00, 100000.00, 'COMPLETED', '2026-01-25 15:30:00', '2026-02-03 16:00:00'),
(5007, 1007, 3, 'VIN00000000000007', 447900.00, 70000.00, 'COMPLETED', '2026-01-26 13:00:00', '2026-02-04 13:30:00'),
(5008, 1008, 4, 'VIN00000000000008', 333800.00, 50000.00, 'COMPLETED', '2026-01-27 17:00:00', '2026-02-05 17:30:00'),
(5009, 1009, 5, 'VIN00000000000009', 475800.00, 80000.00, 'COMPLETED', '2026-01-28 12:00:00', '2026-02-06 12:30:00'),
(5010, 1010, 3, 'VIN00000000000010', 427800.00, 60000.00, 'COMPLETED', '2026-01-29 10:00:00', '2026-02-07 10:30:00'),
(5011, 1011, 4, 'VIN00000000000011', 925000.00, 120000.00, 'DEPOSIT_PAID', '2026-02-08 11:00:00', NULL),
(5012, 1012, 5, 'VIN00000000000012', 585000.00, 80000.00, 'DEPOSIT_PAID', '2026-02-09 16:00:00', NULL),
(5013, 1013, 3, 'VIN00000000000013', 305900.00, 40000.00, 'DEPOSIT_PAID', '2026-02-10 09:00:00', NULL),
(5014, 1014, 4, 'VIN00000000000014', 306900.00, 40000.00, 'DEPOSIT_PAID', '2026-02-11 14:00:00', NULL),
(5015, 1015, 5, 'VIN00000000000015', 438800.00, 50000.00, 'LOCKED', '2026-02-12 13:00:00', NULL),
(5016, 1016, 3, 'VIN00000000000016', 445900.00, 50000.00, 'LOCKED', '2026-02-13 15:00:00', NULL),
(5017, 1017, 4, 'VIN00000000000017', 474800.00, 60000.00, 'LOCKED', '2026-02-14 10:00:00', NULL),
(5018, 1018, 5, 'VIN00000000000018', 926000.00, 100000.00, 'LOCKED', '2026-02-15 11:30:00', NULL),
(5019, 1019, 3, 'VIN00000000000019', 364800.00, 30000.00, 'LOCKED', '2026-02-16 16:30:00', NULL),
(5020, 1020, 4, 'VIN00000000000020', 493800.00, 30000.00, 'LOCKED', '2026-02-17 09:45:00', NULL),
(5021, 1001, 5, 'VIN00000000000021', 342900.00, 20000.00, 'CANCELLED', '2026-02-18 10:15:00', NULL),
(5022, 1002, 3, 'VIN00000000000022', 334800.00, 20000.00, 'CANCELLED', '2026-02-19 14:20:00', NULL);

-- =========================
-- 8) order_item
-- =========================
INSERT INTO order_item (item_id, order_id, item_type, item_desc, amount) VALUES
(70001, 5001, 'VEHICLE', 'Vehicle price', 359800.00),
(70002, 5001, 'INSURANCE', 'Commercial insurance package', 7000.00),
(70003, 5002, 'VEHICLE', 'Vehicle price', 359800.00),
(70004, 5003, 'VEHICLE', 'Vehicle price', 489800.00),
(70005, 5003, 'OPTION', 'Driver assistance package', 8000.00),
(70006, 5004, 'VEHICLE', 'Vehicle price', 429800.00),
(70007, 5004, 'INSURANCE', 'Insurance package', 9000.00),
(70008, 5005, 'VEHICLE', 'Vehicle price', 339900.00),
(70009, 5005, 'OPTION', 'Window film', 5000.00),
(70010, 5006, 'VEHICLE', 'Vehicle price', 499900.00),
(70011, 5006, 'OPTION', 'Seat ventilation', 7000.00),
(70012, 5007, 'VEHICLE', 'Vehicle price', 439900.00),
(70013, 5007, 'INSURANCE', 'Insurance package', 8000.00),
(70014, 5008, 'VEHICLE', 'Vehicle price', 329800.00),
(70015, 5008, 'OPTION', 'Extended warranty', 4000.00),
(70016, 5009, 'VEHICLE', 'Vehicle price', 469800.00),
(70017, 5009, 'INSURANCE', 'Insurance package', 6000.00),
(70018, 5010, 'VEHICLE', 'Vehicle price', 419800.00),
(70019, 5010, 'OPTION', 'Dashcam', 8000.00),
(70020, 5011, 'VEHICLE', 'Vehicle price', 918000.00),
(70021, 5011, 'INSURANCE', 'Insurance package', 7000.00),
(70022, 5012, 'VEHICLE', 'Vehicle price', 578000.00),
(70023, 5012, 'OPTION', 'Paint protection', 7000.00),
(70024, 5013, 'VEHICLE', 'Vehicle price', 299900.00),
(70025, 5013, 'OPTION', 'FSD software', 6000.00),
(70026, 5014, 'VEHICLE', 'Vehicle price', 299900.00),
(70027, 5014, 'OPTION', 'Interior upgrade', 7000.00),
(70028, 5015, 'VEHICLE', 'Vehicle price', 429800.00),
(70029, 5015, 'OPTION', 'Insurance package', 9000.00),
(70030, 5016, 'VEHICLE', 'Vehicle price', 439900.00),
(70031, 5016, 'OPTION', 'Floor mat set', 6000.00),
(70032, 5017, 'VEHICLE', 'Vehicle price', 469800.00),
(70033, 5017, 'INSURANCE', 'Insurance package', 5000.00),
(70034, 5018, 'VEHICLE', 'Vehicle price', 918000.00),
(70035, 5018, 'OPTION', 'Wheel upgrade', 8000.00),
(70036, 5019, 'VEHICLE', 'Vehicle price', 359800.00),
(70037, 5019, 'OPTION', 'Insurance package', 5000.00),
(70038, 5020, 'VEHICLE', 'Vehicle price', 489800.00),
(70039, 5020, 'OPTION', 'Tinted glass', 4000.00),
(70040, 5021, 'VEHICLE', 'Vehicle price', 339900.00),
(70041, 5021, 'OPTION', 'Discount adjustment', 3000.00),
(70042, 5022, 'VEHICLE', 'Vehicle price', 329800.00),
(70043, 5022, 'OPTION', 'Discount adjustment', 5000.00);

-- =========================
-- 9) service_order (>=10)
-- =========================
INSERT INTO service_order (service_order_id, customer_id, staff_id, vehicle_vin, service_type, created_at, expected_finish_time, total_fee, status) VALUES
(9001, 1001, 9,  'VIN00000000000001', 'MAINTENANCE', '2026-03-01 09:00:00', '2026-03-01 17:00:00', 1280.00, 'COMPLETED'),
(9002, 1002, 10, 'VIN00000000000002', 'REPAIR',      '2026-03-02 10:00:00', '2026-03-03 18:00:00', 3560.00, 'COMPLETED'),
(9003, 1003, 9,  'VIN00000000000003', 'MAINTENANCE', '2026-03-03 11:00:00', '2026-03-03 16:00:00', 980.00,  'COMPLETED'),
(9004, 1004, 10, 'VIN00000000000004', 'REPAIR',      '2026-03-04 14:00:00', '2026-03-05 18:00:00', 4200.00, 'IN_PROGRESS'),
(9005, 1005, 9,  'VIN00000000000005', 'MAINTENANCE', '2026-03-05 09:30:00', '2026-03-05 15:00:00', 760.00,  'COMPLETED'),
(9006, 1006, 10, 'VIN00000000000006', 'OTHER',       '2026-03-06 13:00:00', '2026-03-07 12:00:00', 500.00,  'CREATED'),
(9007, 1007, 9,  'VIN00000000000007', 'REPAIR',      '2026-03-07 08:30:00', '2026-03-08 17:30:00', 2890.00, 'IN_PROGRESS'),
(9008, 1008, 10, 'VIN00000000000008', 'MAINTENANCE', '2026-03-08 15:00:00', '2026-03-08 18:00:00', 640.00,  'COMPLETED'),
(9009, 1009, 9,  'VIN00000000000009', 'MAINTENANCE', '2026-03-09 10:20:00', '2026-03-09 17:00:00', 1120.00, 'COMPLETED'),
(9010, 1010, 10, 'VIN00000000000010', 'REPAIR',      '2026-03-10 11:10:00', '2026-03-11 18:00:00', 5100.00, 'CREATED'),
(9011, 1011, 9,  'VIN00000000000011', 'OTHER',       '2026-03-11 09:50:00', '2026-03-12 12:00:00', 860.00,  'CANCELLED'),
(9012, 1012, 10, 'VIN00000000000012', 'MAINTENANCE', '2026-03-12 13:20:00', '2026-03-12 18:00:00', 980.00,  'COMPLETED');

-- =========================
-- 10) service_item
-- =========================
INSERT INTO service_item (item_id, service_order_id, item_name, quantity, unit_price, amount) VALUES
(95001, 9001, 'Engine oil', 1.00, 520.00, 520.00),
(95002, 9001, 'Oil filter', 1.00, 180.00, 180.00),
(95003, 9001, 'Labor', 1.00, 580.00, 580.00),
(95004, 9002, 'Brake pads', 1.00, 1600.00, 1600.00),
(95005, 9002, 'Labor', 1.00, 1960.00, 1960.00),
(95006, 9003, 'Basic maintenance package', 1.00, 980.00, 980.00),
(95007, 9004, 'Body paint', 1.00, 2200.00, 2200.00),
(95008, 9004, 'Labor', 1.00, 2000.00, 2000.00),
(95009, 9005, 'Basic maintenance package', 1.00, 760.00, 760.00),
(95010, 9006, 'Vehicle detailing', 1.00, 500.00, 500.00),
(95011, 9007, 'Battery replacement', 1.00, 1890.00, 1890.00),
(95012, 9007, 'Labor', 1.00, 1000.00, 1000.00),
(95013, 9008, 'Air filter', 1.00, 240.00, 240.00),
(95014, 9008, 'Labor', 1.00, 400.00, 400.00),
(95015, 9009, 'Spark plugs', 1.00, 720.00, 720.00),
(95016, 9009, 'Labor', 1.00, 400.00, 400.00),
(95017, 9010, 'Suspension repair', 1.00, 4200.00, 4200.00),
(95018, 9010, 'Labor', 1.00, 900.00, 900.00),
(95019, 9011, 'Inspection fee', 1.00, 860.00, 860.00),
(95020, 9012, 'Basic maintenance package', 1.00, 980.00, 980.00);

-- =========================
-- 11) vehicle_status_log
-- =========================
INSERT INTO vehicle_status_log (log_id, vehicle_vin, from_status, to_status, changed_at, staff_id, reason) VALUES
(11001, 'VIN00000000000001', 'IN_INVENTORY', 'LOCKED', '2026-01-20 10:05:00', 3, 'Order created'),
(11002, 'VIN00000000000001', 'LOCKED', 'SOLD', '2026-01-25 15:00:00', 3, 'Vehicle delivered'),
(11003, 'VIN00000000000011', 'IN_INVENTORY', 'LOCKED', '2026-02-08 11:02:00', 4, 'Deposit paid'),
(11004, 'VIN00000000000029', NULL, 'IN_TRANSIT', '2026-01-20 09:00:00', 6, 'Purchased from factory'),
(11005, 'VIN00000000000023', 'IN_TRANSIT', 'IN_INVENTORY', '2026-01-26 10:00:00', 7, 'Arrived at dealership');

COMMIT;

-- =========================
-- 12) Align IDENTITY sequences to MAX(id)+1
-- (important after manual explicit-id inserts)
-- =========================
SELECT setval(
    pg_get_serial_sequence('brand', 'brand_id'),
    COALESCE((SELECT MAX(brand_id) FROM brand), 0) + 1,
    false
);

SELECT setval(
    pg_get_serial_sequence('model', 'model_id'),
    COALESCE((SELECT MAX(model_id) FROM model), 0) + 1,
    false
);

SELECT setval(
    pg_get_serial_sequence('customer', 'customer_id'),
    COALESCE((SELECT MAX(customer_id) FROM customer), 0) + 1,
    false
);

SELECT setval(
    pg_get_serial_sequence('staff', 'staff_id'),
    COALESCE((SELECT MAX(staff_id) FROM staff), 0) + 1,
    false
);

SELECT setval(
    pg_get_serial_sequence('customer_intent', 'intent_id'),
    COALESCE((SELECT MAX(intent_id) FROM customer_intent), 0) + 1,
    false
);

SELECT setval(
    pg_get_serial_sequence('sales_order', 'order_id'),
    COALESCE((SELECT MAX(order_id) FROM sales_order), 0) + 1,
    false
);

SELECT setval(
    pg_get_serial_sequence('order_item', 'item_id'),
    COALESCE((SELECT MAX(item_id) FROM order_item), 0) + 1,
    false
);

SELECT setval(
    pg_get_serial_sequence('service_order', 'service_order_id'),
    COALESCE((SELECT MAX(service_order_id) FROM service_order), 0) + 1,
    false
);

SELECT setval(
    pg_get_serial_sequence('service_item', 'item_id'),
    COALESCE((SELECT MAX(item_id) FROM service_item), 0) + 1,
    false
);

SELECT setval(
    pg_get_serial_sequence('vehicle_status_log', 'log_id'),
    COALESCE((SELECT MAX(log_id) FROM vehicle_status_log), 0) + 1,
    false
);
