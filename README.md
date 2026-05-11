# 汽车销售管理系统（openGauss + Java 控制台）

## 1. 项目简介
本项目为《数据库系统》课程大作业，主题为汽车销售管理系统。  
当前仓库已完成数据库侧核心资产（建库建表、初始化数据、视图、索引、触发器、存储过程、复杂查询），下一步进入 Java 控制台应用开发阶段。

定位：
- 数据库：openGauss（华为云优先）
- 应用：Java 控制台（菜单驱动）
- 兼容策略：预留 PostgreSQL 连接接口（后续再处理 SQL 语法差异）
- 说明：本项目不依赖 Node.js

---

## 2. 技术栈与版本要求
建议使用以下版本（或兼容版本）：
- JDK：17（推荐）
- Maven：3.9+
- openGauss：5.x（推荐）
- gsql：与 openGauss 同版本客户端

可选工具：
- Docker / Docker Compose（用于本地数据库联调，可先用于 PostgreSQL）

---

## 3. 项目目录结构
当前目录关键结构如下：

```text
.
├─ pom.xml
├─ .gitignore
├─ README.md
├─ sql/
│  ├─ 01_create_schema.sql
│  ├─ 02_init_data.sql
│  ├─ 03_views.sql
│  ├─ 04_indexes.sql
│  ├─ 05_triggers.sql
│  ├─ 06_procedures.sql
│  └─ 07_queries.sql
├─ src/
│  ├─ main/
│  │  ├─ java/com/carsales/app/
│  │  │  ├─ Application.java
│  │  │  ├─ config/
│  │  │  ├─ dao/
│  │  │  ├─ service/
│  │  │  ├─ ui/
│  │  │  └─ model/
│  │  └─ resources/
│  │     ├─ application.properties
│  │     ├─ application-example.properties
│  │     └─ logback.xml
│  └─ test/java/com/carsales/app/ApplicationSmokeTest.java
└─ doc/
```

SQL 脚本作用说明：
- `01_create_schema.sql`：建库建表与约束
- `02_init_data.sql`：初始化模拟数据（含 identity 序列校准）
- `03_views.sql`：业务视图（业绩、库存、客户价值）
- `04_indexes.sql`：关键索引
- `05_triggers.sql`：核心业务触发器（锁车、交付、取消回滚）
- `06_procedures.sql`：核心存储过程（创建订单、月报、客户历史）
- `07_queries.sql`：Q1~Q8 查询与验收查询

---

## 4. 开发前必须准备的配置文件
在 Java 业务代码开发前，建议先准备以下文件：

### 4.1 必需
1. `pom.xml`（已创建）
- 已包含：
  - openGauss JDBC（默认数据库驱动）
  - PostgreSQL JDBC（预留切换接口）
  - 日志：`slf4j-api` + `logback-classic`
  - 测试：`junit-jupiter`
  - 构建插件：`maven-compiler-plugin`、`maven-surefire-plugin`、`exec-maven-plugin`

2. `src/main/resources/application.properties`（本地私有，已创建）
- 保存本机数据库连接信息（不要提交真实密码）。

3. `src/main/resources/application-example.properties`（提交模板，已创建）
- 提供无敏感信息的模板键，供他人复制为 `application.properties`。

4. `.gitignore`（已创建）
- 忽略本地私密配置与构建产物（如 `target/`、本地配置文件）。

### 4.2 可选
1. `docker-compose.yml`
- 一键启动本地数据库（可先用于 PostgreSQL 联调）。

2. `scripts/init_db.sh`
- 按固定顺序执行 `sql/01~07` 的初始化脚本。

---

## 5. openGauss 部署方式（华为云优先）

### 5.1 华为云 openGauss（推荐）
使用华为云 openGauss 时，确保：
- 数据库服务可用
- 安全组已放行数据库端口（通常 5432）
- 本机网络可访问该实例
- 具备创建数据库与对象权限

连接示例（按实际环境替换）：

```bash
gsql -h YOUR_HOST -p 5432 -d postgres -U your_user -W
```

### 5.2 本地 Docker（可选）
本地 Docker 可作为联调环境（当前优先预留 PostgreSQL 接口，不做 SQL 全量兼容承诺）。  
说明：生产/课程演示目标仍以 openGauss 为准。

---

## 6. 数据库初始化步骤（严格顺序）
必须按以下顺序执行：

1. `sql/01_create_schema.sql`
2. `sql/02_init_data.sql`
3. `sql/03_views.sql`
4. `sql/04_indexes.sql`
5. `sql/05_triggers.sql`
6. `sql/06_procedures.sql`
7. `sql/07_queries.sql`

原因：
- 触发器、视图、过程依赖前置表结构
- 初始化数据依赖完整约束与状态定义
- 查询验收脚本依赖视图与过程已创建

执行示例（按实际连接参数替换）：

```bash
gsql -d postgres -U your_user -W -f sql/01_create_schema.sql
gsql -d car_sales -U your_user -W -f sql/02_init_data.sql
gsql -d car_sales -U your_user -W -f sql/03_views.sql
gsql -d car_sales -U your_user -W -f sql/04_indexes.sql
gsql -d car_sales -U your_user -W -f sql/05_triggers.sql
gsql -d car_sales -U your_user -W -f sql/06_procedures.sql
gsql -d car_sales -U your_user -W -f sql/07_queries.sql
```

---

## 7. Java 控制台工程初始化建议
当前仓库已创建第一版骨架，不含具体业务实现。当前结构：

```text
src/main/java/com/carsales/app/
├─ Application.java                # 应用入口（启动+DB连通性检查+菜单）
├─ config/
│  ├─ ApplicationConfig.java       # 配置读取
│  ├─ ConnectionFactory.java       # 按 db.vendor 选择驱动并建连
│  └─ DatabaseHealthChecker.java   # 启动时 SELECT 1 连通性检查
├─ dao/
│  ├─ BaseDao.java                 # DAO基类
│  └─ OrderDao.java                # 订单DAO占位
├─ service/
│  ├─ TransactionManager.java      # 事务模板
│  └─ TransactionCallback.java     # 事务回调接口
├─ ui/
│  └─ ConsoleMenu.java             # 控制台菜单壳
└─ model/
   └─ PlaceholderModel.java        # 模型占位
```

入口类职责建议：
- 菜单展示与路由（销售前台、库存管理、报表中心）
- 调用 service 层，不直接拼 SQL
- 统一异常提示与用户输入校验

事务边界约定（已落地为模板）：
- 关键写操作通过 `TransactionManager` 显式 `commit/rollback`
- 查询操作保持只读，并统一参数化查询

---

## 8. 构建与运行
当前可用命令如下：

```bash
mvn clean package
```

```bash
java -jar target/your-app.jar
```

使用 `exec-maven-plugin` 启动：

```bash
mvn exec:java
```

说明：
- 应用启动时会先读取 `application.properties`，并执行 `SELECT 1` 连通性检查。
- 未填真实数据库配置时，启动会失败（符合预期）。

---

## 9. 快速验收清单
新同学仅按 README 应可完成以下验收：

1. openGauss 服务可连接。
2. `car_sales` 数据库创建成功。
3. `01~06` 脚本执行成功（表、视图、索引、触发器、过程存在）。
4. `07_queries.sql` 可执行并返回结果。
5. Java 控制台工程可启动到菜单入口（即便业务模块暂未完成）。
6. `db.vendor=opengauss` 可正常走 openGauss 驱动分支；`db.vendor=postgres` 可切换到 PostgreSQL 驱动分支（仅连接接口预留）。

建议额外手工验证：
- 调用 `sp_get_monthly_report`
- 调用 `sp_get_customer_full_history`
- 验证订单插入触发锁车逻辑（触发器）

---

## 10. 常见问题（FAQ）

### Q1：为什么不用 Node.js？
本项目是 openGauss + Java 控制台应用，数据库与应用均不依赖 Node.js。

### Q1.1：当前为什么同时有 PostgreSQL 驱动？
用于预留后续数据库切换接口。当前阶段仍以 openGauss 为主，不承诺现有 SQL 脚本在 PostgreSQL 上无差异执行。

### Q2：执行 `02_init_data.sql` 后，后续自动插入主键冲突怎么办？
`02_init_data.sql` 末尾已包含 identity 序列对齐（`MAX(id)+1`），请确保脚本完整执行。

### Q3：为什么 `01_create_schema.sql` 在 `postgres` 库执行，而后续在 `car_sales` 执行？
`01` 包含 `CREATE DATABASE car_sales;`，建库后后续对象应在目标业务库中创建。

### Q4：触发器报“车辆状态不符合预期”怎么办？
检查订单状态流转与车辆当前状态是否符合规则（例如下单前车辆应为 `IN_INVENTORY`）。

### Q5：如何避免泄露数据库密码？
将真实密码只放在本地 `application.properties`，仓库仅提交 `application-example.properties`。
