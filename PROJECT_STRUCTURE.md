# 项目目录结构说明

## 1. 根目录

```text
.
├─ backend/                  # Spring Boot + MyBatis 后端
├─ frontend/                 # Vue3 + Vite 前端
├─ sql/                      # openGauss 建库、初始化、视图、索引、触发器、过程、查询脚本
├─ doc/                      # 设计文档与图示
├─ docker-compose.yml        # 本地联调数据库（可选）
├─ README.md                 # 项目总说明
└─ PROJECT_STRUCTURE.md      # 当前文档
```

## 2. 后端目录（重点）

```text
backend/src/main/java/com/carsales/backend
├─ common/
│  ├─ api/                   # 统一响应对象
│  ├─ exception/             # 业务异常
│  └─ handler/               # 全局异常处理
├─ config/                   # Spring/MyBatis/CORS 配置
├─ controller/
│  ├─ auth/
│  ├─ sales/
│  ├─ inventory/
│  └─ report/
├─ mapper/
│  ├─ inventory/             # inventory 模块 mapper 接口
│  └─ ...
├─ model/
│  ├─ dto/
│  │  └─ inventory/          # inventory 查询入参
│  ├─ vo/
│  │  ├─ inventory/          # inventory 返回结构
│  │  └─ common/             # 通用 VO（如分页）
│  └─ entity/
├─ service/
│  ├─ inventory/
│  │  ├─ InventoryService.java
│  │  └─ impl/
│  │     └─ InventoryServiceImpl.java
│  └─ sales/
│     ├─ OrderService.java
│     └─ impl/
│        └─ OrderServiceImpl.java
└─ util/
```

## 3. Mapper XML 目录

```text
backend/src/main/resources/mapper/
├─ inventory_mapper.xml
└─ placeholder.xml
```

## 4. 约定（供后续 agent 执行）

1. 业务服务目录统一采用：`service/<module>/` + `service/<module>/impl/`。
2. Controller 按业务域拆分到 `controller/<module>/`。
3. DTO/VO 按业务域拆分到 `model/dto/<module>/` 与 `model/vo/<module>/`。
4. Mapper 接口按业务域拆分到 `mapper/<module>/`，SQL 放 `resources/mapper/*.xml`。
5. 新增写接口时必须补事务与参数校验，写操作 SQL 必须参数化。
