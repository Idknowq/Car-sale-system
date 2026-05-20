<template>
  <div class="module-page">
    <el-card shadow="never" class="section-card">
      <template #header>筛选区</template>
      <div class="placeholder">Inventory 模块在该区域放置库存筛选组件。</div>
    </el-card>

    <el-card shadow="never" class="section-card">
      <template #header>表格区</template>
      <div class="placeholder">Inventory 模块在该区域放置库存列表与状态展示。</div>
    </el-card>

    <el-card shadow="never" class="section-card">
      <template #header>分页区</template>
      <div class="placeholder">Inventory 模块在该区域放置分页器与统计信息。</div>
    </el-card>
  </div>
</template>

<style scoped>
.section-card {
  margin-bottom: 16px;
}

.placeholder {
  color: #606266;
}
</style>
