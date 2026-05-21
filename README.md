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

也可使用一键部署脚本（推荐）：

- `sql/00_deploy_all.sql`：按上述顺序自动执行全部脚本。
- `sql/00_reset_and_deploy.sql`：先重置 `public` schema，再执行一键部署（适合本地演示反复重建）。

示例：

```bash
# 方式1：进入 sql 目录执行
cd sql
gsql -d car_sales -f 00_deploy_all.sql

# 方式2：仓库根目录直接执行
gsql -d car_sales -f sql/00_deploy_all.sql

# 重置并重建
gsql -d car_sales -f sql/00_reset_and_deploy.sql
```

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

生产构建验证：

```bash
cd frontend
npm run build
```

### 前端路由与页面

- `/login`：登录入口页（系统入口）
- `/sales`：销售前台（查询我的订单、创建销售订单、创建意向客户）
- `/inventory`：库存管理（库存查询、车辆入库、库存预警）
- `/report`：报表中心（月报、业绩榜、畅销车型）

### 前端目录（当前实现）

```text
frontend/src
├─ api/                     # 按模块拆分 API 调用
├─ components/
│  ├─ sales/                # 销售模块组件
│  ├─ inventory/            # 库存模块组件
│  └─ report/               # 报表模块组件
├─ constants/               # 公共常量（状态枚举等）
├─ layout/                  # 主布局（侧边菜单 + 顶部栏 + 内容区）
├─ router/                  # 路由配置（登录 + 主布局子路由）
├─ utils/                   # request 封装、格式化工具
└─ views/                   # 模块页面
```

## 6. 前端协作约定

- 并行开发规范文档：`FRONTEND_AGENT_RULES.md`
- 三模块并行开发时必须遵守该文档中的：
  - 文件 ownership 边界
  - API 参数命名与后端 DTO 一致性
  - 统一页面结构与交互约定
  - 提交前构建检查（`npm run build`）

## 7. 约定

- 统一响应结构：`{ code, message, data }`
- API 前缀：
  - `/api/auth/*`
  - `/api/sales/*`
  - `/api/inventory/*`
  - `/api/report/*`
- 写操作必须事务化，SQL 必须参数化。

## 8. 迁移说明

已完成一次性切换：旧控制台 `src/` 与根 `pom.xml` 已移除。当前仅维护 Web 架构。
