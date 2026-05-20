# FRONTEND_AGENT_RULES

## 1. 目标与原则
- 本规范用于支持 `sales`、`inventory`、`report` 三模块并行开发。
- 目标：降低 merge 冲突，保持代码结构一致，保证可维护性。
- 规范强度：中等规范。统一共享层和核心约定，模块内部保留实现自由。

## 2. 分支与合并策略
- 基线分支：`feature/front`（当前前端壳层基线）。
- 业务分支建议：
- `feature/front-sales`
- `feature/front-inventory`
- `feature/front-report`
- 合并顺序建议：`feature/front` -> `sales` -> `inventory` -> `report`。
- 每个业务分支在提交前先 rebase 到最新 `feature/front`。

## 3. 文件 ownership（防冲突核心）
- `sales` agent 仅允许修改：
- `frontend/src/views/sales/*`
- `frontend/src/components/sales/*`
- `frontend/src/api/sales.js`
- `inventory` agent 仅允许修改：
- `frontend/src/views/inventory/*`
- `frontend/src/components/inventory/*`
- `frontend/src/api/inventory.js`
- `report` agent 仅允许修改：
- `frontend/src/views/report/*`
- `frontend/src/components/report/*`
- `frontend/src/api/report.js`

禁止在业务分支直接修改以下共享文件：
- `frontend/src/router/index.js`
- `frontend/src/layout/MainLayout.vue`
- `frontend/src/utils/request.js`
- `frontend/src/constants/*`
- `frontend/src/utils/format.js`

如果业务开发必须改共享层：
- 回到 `feature/front` 统一修改。
- 修改后业务分支 rebase 再继续开发。

## 4. 目录与命名规范
- 页面目录：`frontend/src/views/<module>/`。
- 模块组件目录：`frontend/src/components/<module>/`。
- 接口文件：`frontend/src/api/<module>.js`。
- 公共常量：`frontend/src/constants/*`。
- 公共工具：`frontend/src/utils/*`。

API 命名建议：
- 查询：`queryXxx`
- 详情：`getXxx`
- 新增：`createXxx`
- 更新：`updateXxx`

参数命名要求：
- 必须与后端 DTO 字段一致。
- 注意分页字段区分：
- 销售查询使用 `pageNo/pageSize`
- 库存查询使用 `pageNum/pageSize`

## 5. 页面结构与交互约定
- 页面结构统一为：筛选区 + 表格区 + 分页区 +（可选）弹窗区。
- 表单校验：提交时统一前端校验，失败不发请求。
- 加载态：请求期间禁用重复提交按钮，表格显示 loading。
- 空态：无数据时显示空态提示。
- 错误提示：统一由 `request` 层和 `ElMessage` 输出，不重复封装通用错误分支。

## 6. 请求层约定
- 后端统一响应结构：`{ code, message, data }`。
- 前端 `request` 拦截器负责：
- 成功：返回 `data`
- 业务失败（`code != 0`）：统一提示并 reject
- 网络失败：统一提示并 reject

## 7. 提交与评审 Checklist
- 是否只修改了本模块允许路径。
- 是否遵守接口命名与 DTO 参数命名。
- 是否复用公共常量与格式化工具，避免重复造轮子。
- 是否保持“筛选区 + 表格区 + 分页区”结构一致。
- 是否通过前端构建检查：`npm run build`。
