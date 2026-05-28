<template>
  <div class="pager-wrap">
    <div class="summary">共 {{ total }} 条</div>
    <el-pagination
      background
      :layout="layout"
      :total="total"
      :current-page="currentPage"
      :page-size="pageSize"
      :page-sizes="pageSizes"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
  </div>
</template>

<script setup>
defineProps({
  total: { type: Number, default: 0 },
  currentPage: { type: Number, default: 1 },
  pageSize: { type: Number, default: 10 },
  pageSizes: { type: Array, default: () => [10, 20, 50, 100] },
  layout: { type: String, default: 'total, sizes, prev, pager, next, jumper' }
})

const emit = defineEmits(['update:currentPage', 'update:pageSize', 'change-page', 'change-size'])

function handleCurrentChange(page) {
  emit('update:currentPage', page)
  emit('change-page', page)
}

function handleSizeChange(size) {
  emit('update:pageSize', size)
  emit('change-size', size)
}
</script>

<style scoped>
.pager-wrap {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  flex-wrap: wrap;
}

.summary {
  color: #606266;
  font-size: 13px;
}
</style>
