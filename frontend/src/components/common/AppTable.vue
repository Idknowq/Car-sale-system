<template>
  <el-table
    v-loading="loading"
    :data="data"
    :border="border"
    :stripe="stripe"
    :empty-text="emptyText"
    :max-height="maxHeight"
  >
    <slot />
  </el-table>
</template>

<script setup>
defineProps({
  data: { type: Array, default: () => [] },
  loading: { type: Boolean, default: false },
  emptyText: { type: String, default: '暂无数据' },
  border: { type: Boolean, default: true },
  stripe: { type: Boolean, default: true },
  maxHeight: { type: [Number, String], default: undefined }
})
</script>
