# 汽车销售管理系统（Web 版）

## 1. 项目结构

```text
.
├─ backend/                 # Spring Boot + MyBatis
├─ frontend/                # Vue 3 + Vite + Element Plus
├─ sql/                     # 数据库脚本（openGauss）
├─ doc/
├─ docker-compose.yml       # 本地 PostgreSQL 联调（可选）
└─ .env.example
```

## 2. 技术栈

- 后端：Spring Boot 3、MyBatis、openGauss JDBC（默认）、PostgreSQL JDBC（预留）
- 前端：Vue 3、Vite、Element Plus、Pinia、Axios
- 数据库：openGauss（生产/演示优先）

## 3. 数据库初始化（openGauss）

按顺序执行：

1. `sql/01_create_schema.sql`
2. `sql/02_init_data.sql`
3. `sql/03_views.sql`
4. `sql/04_indexes.sql`
5. `sql/05_triggers.sql`
6. `sql/06_procedures.sql`
7. `sql/07_queries.sql`

## 4. 后端启动

```bash
cd backend
mvn clean package
mvn spring-boot:run
```

默认 profile 为 `opengauss`，配置文件：

- `backend/src/main/resources/application.yml`
- `backend/src/main/resources/application-opengauss.yml`
- `backend/src/main/resources/application-postgres.yml`（仅预留）

健康检查：

- `GET http://localhost:8080/actuator/health`

示例接口：

- `/api/auth/ping`
- `/api/sales/ping`
- `/api/inventory/ping`
- `/api/report/ping`

## 5. 前端启动

```bash
cd frontend
npm install
npm run dev
```

默认地址：`http://localhost:5173`

## 6. 约定

- 统一响应结构：`{ code, message, data }`
- API 前缀：
  - `/api/auth/*`
  - `/api/sales/*`
  - `/api/inventory/*`
  - `/api/report/*`
- 写操作必须事务化，SQL 必须参数化。

## 7. 迁移说明

已完成一次性切换：旧控制台 `src/` 与根 `pom.xml` 已移除。当前仅维护 Web 架构。
