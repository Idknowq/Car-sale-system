<template>
  <el-dialog :model-value="visible" title="库存预警报表" width="980px" @close="$emit('close')">
    <div class="alert-filter">
      <el-input-number v-model="query.brandId" :min="1" :controls="false" placeholder="品牌ID" style="width: 150px" />
      <el-input-number v-model="query.modelId" :min="1" :controls="false" placeholder="车型ID" style="width: 150px" />
      <el-switch v-model="query.onlyPositiveShortage" active-text="仅缺口>0" />
      <el-button type="primary" :loading="loading" @click="emit('search', { ...query })">查询</el-button>
    </div>

    <el-table :data="records" v-loading="loading" border empty-text="暂无预警数据" max-height="420">
      <el-table-column prop="brandName" label="品牌" min-width="120" />
      <el-table-column prop="modelName" label="车型" min-width="120" />
      <el-table-column prop="modelYear" label="年款" width="90" />
      <el-table-column prop="trimName" label="配置" min-width="120" />
      <el-table-column prop="safetyStockThreshold" label="安全库存" width="100" />
      <el-table-column prop="inInventoryCount" label="在库" width="90" />
      <el-table-column prop="lockedCount" label="锁定" width="90" />
      <el-table-column prop="inTransitCount" label="在途" width="90" />
      <el-table-column prop="shortageCount" label="缺口" width="90" />
    </el-table>

    <template #footer>
      <el-button @click="$emit('close')">关闭</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { reactive, watch } from 'vue'

const props = defineProps({
  visible: { type: Boolean, default: false },
  loading: { type: Boolean, default: false },
  records: { type: Array, default: () => [] }
})

const emit = defineEmits(['close', 'search'])

const query = reactive({
  brandId: null,
  modelId: null,
  onlyPositiveShortage: true
})

watch(
  () => props.visible,
  (v) => {
    if (!v) return
    query.brandId = null
    query.modelId = null
    query.onlyPositiveShortage = true
  }
)
</script>

<style scoped>
.alert-filter {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
}
</style>
