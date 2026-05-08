-- ============================================
-- 01_create_schema.sql (openGauss)
-- Database: car_sales
-- ============================================

CREATE DATABASE car_sales;

-- Execute the following in gsql after creating database:
-- \c car_sales

-- =========================
-- 1) Master Data
-- =========================

CREATE TABLE brand (
    brand_id                INT PRIMARY KEY,
    brand_name              VARCHAR(40) NOT NULL,
    CONSTRAINT uk_brand_name UNIQUE (brand_name)
);

CREATE TABLE model (
    model_id                INT PRIMARY KEY,
    brand_id                INT NOT NULL,
    model_name              VARCHAR(80) NOT NULL,
    model_year              INT NOT NULL,
    trim_name               VARCHAR(80) NOT NULL,
    msrp                    NUMERIC(12, 2) NOT NULL CHECK (msrp >= 0),
    engine_displacement     NUMERIC(4, 1) NOT NULL CHECK (engine_displacement > 0),
    model_type              VARCHAR(30) NOT NULL,
    safety_stock_threshold  INT NOT NULL DEFAULT 2 CHECK (safety_stock_threshold >= 0),
    CONSTRAINT fk_model_brand
        FOREIGN KEY (brand_id) REFERENCES brand (brand_id),
    CONSTRAINT uk_model_identity
        UNIQUE (brand_id, model_name, model_year, trim_name)
);

CREATE TABLE customer (
    customer_id             INT PRIMARY KEY,
    customer_name           VARCHAR(50) NOT NULL,
    gender                  VARCHAR(10),
    phone                   VARCHAR(20) NOT NULL,
    id_card                 VARCHAR(32) NOT NULL,
    address                 VARCHAR(200),
    first_visit_date        DATE NOT NULL,
    source_channel          VARCHAR(30),
    CONSTRAINT ck_customer_gender
        CHECK (gender IN ('M', 'F', 'OTHER') OR gender IS NULL),
    CONSTRAINT uk_customer_phone UNIQUE (phone),
    CONSTRAINT uk_customer_id_card UNIQUE (id_card)
);

CREATE TABLE staff (
    staff_id                INT PRIMARY KEY,
    staff_no                VARCHAR(30) NOT NULL,
    staff_name              VARCHAR(50) NOT NULL,
    role                    VARCHAR(30) NOT NULL,
    department              VARCHAR(30) NOT NULL,
    hire_date               DATE NOT NULL,
    manager_id              INT,
    CONSTRAINT uk_staff_no UNIQUE (staff_no),
    CONSTRAINT fk_staff_manager
        FOREIGN KEY (manager_id) REFERENCES staff (staff_id)
);

CREATE TABLE vehicle (
    vehicle_vin             CHAR(17) PRIMARY KEY,
    model_id                INT NOT NULL,
    engine_no               VARCHAR(32) NOT NULL,
    color                   VARCHAR(20) NOT NULL,
    manufacture_date        DATE NOT NULL,
    inventory_in_date       DATE,
    purchase_cost           NUMERIC(12, 2) NOT NULL CHECK (purchase_cost >= 0),
    suggested_retail_price  NUMERIC(12, 2) NOT NULL CHECK (suggested_retail_price >= 0),
    current_status          VARCHAR(20) NOT NULL,
    CONSTRAINT fk_vehicle_model
        FOREIGN KEY (model_id) REFERENCES model (model_id),
    CONSTRAINT uk_vehicle_engine_no UNIQUE (engine_no),
    CONSTRAINT ck_vehicle_status
        CHECK (current_status IN ('IN_TRANSIT', 'IN_INVENTORY', 'LOCKED', 'SOLD'))
);

-- =========================
-- 2) Sales Domain
-- =========================

CREATE TABLE customer_intent (
    intent_id               INT PRIMARY KEY,
    customer_id             INT NOT NULL,
    model_id                INT NOT NULL,
    intent_level            VARCHAR(20) NOT NULL,
    remark                  VARCHAR(500),
    staff_id                INT NOT NULL,
    next_contact_time       TIMESTAMP,
    created_at              TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_intent_customer
        FOREIGN KEY (customer_id) REFERENCES customer (customer_id),
    CONSTRAINT fk_intent_model
        FOREIGN KEY (model_id) REFERENCES model (model_id),
    CONSTRAINT fk_intent_staff
        FOREIGN KEY (staff_id) REFERENCES staff (staff_id)
);

CREATE TABLE sales_order (
    order_id                INT PRIMARY KEY,
    customer_id             INT NOT NULL,
    staff_id                INT NOT NULL,
    vehicle_vin             CHAR(17) NOT NULL,
    total_amount            NUMERIC(12, 2) NOT NULL CHECK (total_amount >= 0),
    deposit_amount          NUMERIC(12, 2) NOT NULL DEFAULT 0 CHECK (deposit_amount >= 0),
    order_status            VARCHAR(20) NOT NULL,
    created_at              TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    delivery_time           TIMESTAMP,
    CONSTRAINT fk_sales_order_customer
        FOREIGN KEY (customer_id) REFERENCES customer (customer_id),
    CONSTRAINT fk_sales_order_staff
        FOREIGN KEY (staff_id) REFERENCES staff (staff_id),
    CONSTRAINT fk_sales_order_vehicle
        FOREIGN KEY (vehicle_vin) REFERENCES vehicle (vehicle_vin),
    CONSTRAINT ck_sales_order_amounts
        CHECK (deposit_amount <= total_amount),
    CONSTRAINT uk_sales_order_vehicle_vin UNIQUE (vehicle_vin),
    CONSTRAINT ck_sales_order_status
        CHECK (order_status IN ('CREATED', 'LOCKED', 'DEPOSIT_PAID', 'COMPLETED', 'CANCELLED'))
);

CREATE TABLE order_item (
    item_id                 INT PRIMARY KEY,
    order_id                INT NOT NULL,
    item_type               VARCHAR(20) NOT NULL,
    item_desc               VARCHAR(200) NOT NULL,
    amount                  NUMERIC(12, 2) NOT NULL,
    CONSTRAINT fk_order_item_order
        FOREIGN KEY (order_id) REFERENCES sales_order (order_id),
    CONSTRAINT ck_order_item_type
        CHECK (item_type IN ('VEHICLE', 'OPTION', 'INSURANCE', 'DISCOUNT', 'OTHER'))
);

-- =========================
-- 3) After-sales Domain
-- =========================

CREATE TABLE service_order (
    service_order_id        INT PRIMARY KEY,
    customer_id             INT NOT NULL,
    staff_id                INT NOT NULL,
    vehicle_vin             CHAR(17) NOT NULL,
    service_type            VARCHAR(20) NOT NULL,
    created_at              TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expected_finish_time    TIMESTAMP,
    total_fee               NUMERIC(12, 2) NOT NULL DEFAULT 0 CHECK (total_fee >= 0),
    status                  VARCHAR(20) NOT NULL,
    CONSTRAINT fk_service_order_customer
        FOREIGN KEY (customer_id) REFERENCES customer (customer_id),
    CONSTRAINT fk_service_order_staff
        FOREIGN KEY (staff_id) REFERENCES staff (staff_id),
    CONSTRAINT fk_service_order_vehicle
        FOREIGN KEY (vehicle_vin) REFERENCES vehicle (vehicle_vin),
    CONSTRAINT ck_service_type
        CHECK (service_type IN ('MAINTENANCE', 'REPAIR', 'OTHER')),
    CONSTRAINT ck_service_status
        CHECK (status IN ('CREATED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED'))
);

CREATE TABLE service_item (
    item_id                 INT PRIMARY KEY,
    service_order_id        INT NOT NULL,
    item_name               VARCHAR(80) NOT NULL,
    quantity                NUMERIC(12, 2) NOT NULL CHECK (quantity > 0),
    unit_price              NUMERIC(12, 2) NOT NULL CHECK (unit_price >= 0),
    amount                  NUMERIC(12, 2) NOT NULL CHECK (amount >= 0),
    CONSTRAINT fk_service_item_order
        FOREIGN KEY (service_order_id) REFERENCES service_order (service_order_id)
);

-- =========================
-- 4) Vehicle Status Audit
-- =========================

CREATE TABLE vehicle_status_log (
    log_id                  INT PRIMARY KEY,
    vehicle_vin             CHAR(17) NOT NULL,
    from_status             VARCHAR(20),
    to_status               VARCHAR(20) NOT NULL,
    changed_at              TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    staff_id                INT,
    reason                  VARCHAR(200),
    CONSTRAINT fk_status_log_vehicle
        FOREIGN KEY (vehicle_vin) REFERENCES vehicle (vehicle_vin),
    CONSTRAINT fk_status_log_staff
        FOREIGN KEY (staff_id) REFERENCES staff (staff_id),
    CONSTRAINT ck_status_log_from
        CHECK (from_status IN ('IN_TRANSIT', 'IN_INVENTORY', 'LOCKED', 'SOLD') OR from_status IS NULL),
    CONSTRAINT ck_status_log_to
        CHECK (to_status IN ('IN_TRANSIT', 'IN_INVENTORY', 'LOCKED', 'SOLD'))
);
