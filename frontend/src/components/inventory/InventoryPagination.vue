<template>
  <AppPagination
    :total="total"
    :current-page="pageNum"
    :page-size="pageSize"
    @change-page="(page) => $emit('change-page', page)"
    @change-size="(size) => $emit('change-size', size)"
  />
</template>

<script setup>
import AppPagination from '../common/AppPagination.vue'

defineProps({
  total: { type: Number, default: 0 },
  pageNum: { type: Number, default: 1 },
  pageSize: { type: Number, default: 10 }
})

defineEmits(['change-page', 'change-size'])
</script>
