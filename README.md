# 汽车销售管理系统（Web 版）

## 1. 项目说明

本项目是一个面向 4S 店业务的汽车销售管理系统，当前实现为前后端分离 Web 应用，主要功能包括：

- 销售前台：创建意向客户、创建销售订单、查询销售顾问名下订单、完成订单交付。
- 库存管理：车辆库存查询、在途车辆入库、库存预警查询。
- 报表中心：月度销售统计、销售顾问业绩排行、畅销车型排行。
- 数据库对象：完成建表、初始化数据、视图、索引、触发器、存储过程和复杂查询脚本。

项目结构：

```text
.
├─ backend/                 # Spring Boot 3 + MyBatis 后端
├─ frontend/                # Vue 3 + Vite + Element Plus 前端
├─ sql/                     # openGauss 数据库脚本
├─ doc/                     # 课程设计文档
├─ docker-compose.yml       # 本地 PostgreSQL 联调配置（可选）
└─ .env.example             # 环境变量模板
```

技术栈：

- 后端：Spring Boot 3、MyBatis、Spring Validation、Spring Actuator、springdoc-openapi。
- 前端：Vue 3、Vite、Vue Router、Pinia、Element Plus、Axios。
- 数据库：openGauss 为默认数据库；项目中保留 PostgreSQL JDBC 依赖和配置文件作为兼容预留。

数据库脚本说明：

| 脚本 | 作用 |
| --- | --- |
| `sql/01_create_schema.sql` | 创建表结构、主键、外键、唯一约束、检查约束 |
| `sql/02_init_data.sql` | 初始化演示数据 |
| `sql/03_views.sql` | 创建销售业绩、库存汇总、客户价值视图 |
| `sql/04_indexes.sql` | 创建业务查询索引 |
| `sql/05_triggers.sql` | 创建订单与车辆状态联动触发器 |
| `sql/06_procedures.sql` | 创建销售下单、月报、客户历史查询过程/函数 |
| `sql/07_queries.sql` | Q1-Q8 复杂查询 |
| `sql/00_deploy_all.sql` | 按顺序执行全部数据库脚本 |
| `sql/00_reset_and_deploy.sql` | 重置 `public` schema 后重新部署 |

主要 API 前缀：

- `/api/sales/*`
- `/api/inventory/*`
- `/api/report/*`

前端主要页面：

- `/login`：系统入口。
- `/sales`：销售前台。
- `/inventory`：库存管理。
- `/report`：报表中心。

## 2. 运行环境

基础环境：

- JDK 17。
- Maven 3.8+。
- Node.js 18+，建议配套 npm 9+。
- openGauss 数据库，默认端口按 `.env.example` 为 `26000`。
- gsql 客户端，用于执行数据库初始化脚本。

后端关键配置：

- 默认 Spring Profile：`opengauss`。
- 默认后端端口：`8080`。
- 配置文件：
  - `backend/src/main/resources/application.yml`
  - `backend/src/main/resources/application-opengauss.yml`
  - `backend/src/main/resources/application-postgres.yml`

前端关键配置：

- 默认前端开发端口：`5173`。
- 默认后端请求地址：`http://localhost:8080`。

环境变量来自项目根目录 `.env`，可从模板复制：

```bash
cp .env.example .env
```

需要确认或修改的字段：

```text
BACKEND_PORT=8080
SPRING_PROFILES_ACTIVE=opengauss
DB_URL=jdbc:opengauss://127.0.0.1:26000/car_sales?sslmode=disable&currentSchema=public
DB_USERNAME=car_sales_user
DB_PASSWORD=CHANGE_ME
DB_DRIVER=org.opengauss.Driver
VITE_API_BASE_URL=http://localhost:8080
VITE_PORT=5173
```

注意：`DB_PASSWORD` 没有可用默认值，必须改成实际数据库密码。

## 3. 启动指南

### 3.1 准备数据库

先在 openGauss 中创建数据库和用户，确保 `.env` 中的 `DB_URL`、`DB_USERNAME`、`DB_PASSWORD` 与实际环境一致。

然后执行数据库脚本。推荐使用重置并重建脚本，适合本地演示和重复测试：

```bash
gsql -d car_sales -f sql/00_reset_and_deploy.sql
```

如果不需要重置已有 schema，可执行：

```bash
gsql -d car_sales -f sql/00_deploy_all.sql
```

也可以按顺序手动执行：

```bash
gsql -d car_sales -f sql/01_create_schema.sql
gsql -d car_sales -f sql/02_init_data.sql
gsql -d car_sales -f sql/03_views.sql
gsql -d car_sales -f sql/04_indexes.sql
gsql -d car_sales -f sql/05_triggers.sql
gsql -d car_sales -f sql/06_procedures.sql
gsql -d car_sales -f sql/07_queries.sql
```

### 3.2 加载环境变量

每次启动后端或前端前，在项目根目录加载 `.env`：

```bash
set -a
source .env
set +a
```

### 3.3 启动后端

```bash
cd backend
mvn clean package
mvn spring-boot:run
```

启动后可检查：

```text
GET http://localhost:${BACKEND_PORT}/actuator/health
GET http://localhost:${BACKEND_PORT}/api/sales/ping
GET http://localhost:${BACKEND_PORT}/api/inventory/ping
GET http://localhost:${BACKEND_PORT}/api/report/ping
```

默认情况下即：

```text
http://localhost:8080/actuator/health
```

### 3.4 启动前端

另开一个终端，加载同一份 `.env` 后启动前端：

```bash
cd frontend
npm install
npm run dev
```

默认访问地址：

```text
http://localhost:5173
```

生产构建检查：

```bash
cd frontend
npm run build
```

### 3.5 常见问题

- 后端启动失败且提示数据库连接异常：检查 openGauss 是否启动、`DB_URL` 是否正确、`DB_PASSWORD` 是否已修改。
- 前端请求失败：检查后端是否启动，且 `VITE_API_BASE_URL` 是否指向后端实际端口。
- 数据库脚本执行失败：优先使用 `sql/00_reset_and_deploy.sql` 重建本地演示环境；如果是已有数据环境，不应直接重置 schema。
