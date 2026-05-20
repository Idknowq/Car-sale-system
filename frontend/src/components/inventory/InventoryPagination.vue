<template>
  <div class="pager-wrap">
    <div class="summary">共 {{ total }} 条</div>
    <el-pagination
      background
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
      :current-page="pageNum"
      :page-size="pageSize"
      :page-sizes="[10, 20, 50, 100]"
      @current-change="(page) => $emit('change-page', page)"
      @size-change="(size) => $emit('change-size', size)"
    />
  </div>
</template>

<script setup>
defineProps({
  total: { type: Number, default: 0 },
  pageNum: { type: Number, default: 1 },
  pageSize: { type: Number, default: 10 }
})

defineEmits(['change-page', 'change-size'])
</script>

<style scoped>
.pager-wrap {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
}

.summary {
  color: #606266;
  font-size: 13px;
}
</style>
