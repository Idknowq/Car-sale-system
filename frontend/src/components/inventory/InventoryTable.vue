<template>
  <el-table :data="records" v-loading="loading" stripe border empty-text="暂无库存数据">
    <el-table-column prop="vehicleVin" label="VIN" min-width="180" />
    <el-table-column prop="brandName" label="品牌" min-width="120" />
    <el-table-column prop="modelName" label="车型" min-width="120" />
    <el-table-column prop="modelYear" label="年款" width="90" />
    <el-table-column prop="trimName" label="配置" min-width="140" />
    <el-table-column prop="color" label="颜色" width="90" />
    <el-table-column prop="currentStatus" label="状态" width="110">
      <template #default="{ row }">{{ formatInventoryStatus(row.currentStatus) }}</template>
    </el-table-column>
    <el-table-column prop="inventoryInDate" label="入库日期" width="130">
      <template #default="{ row }">{{ formatDate(row.inventoryInDate) }}</template>
    </el-table-column>
    <el-table-column label="操作" width="120" fixed="right">
      <template #default="{ row }">
        <el-button
          v-if="row.currentStatus === 'IN_TRANSIT'"
          type="primary"
          link
          @click="$emit('inbound', row)"
        >
          车辆入库
        </el-button>
        <span v-else>-</span>
      </template>
    </el-table-column>
  </el-table>
</template>

<script setup>
import { formatDate, formatInventoryStatus } from '../../utils/format'

defineProps({
  loading: { type: Boolean, default: false },
  records: { type: Array, default: () => [] }
})

defineEmits(['inbound'])
</script>
