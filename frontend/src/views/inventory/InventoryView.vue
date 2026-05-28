<template>
  <div class="module-page">
    <el-card shadow="never" class="section-card">
      <template #header>筛选区</template>
      <InventoryFilter :loading="loading" @search="handleSearch" @show-alerts="openAlerts" />
    </el-card>

    <el-card shadow="never" class="section-card">
      <template #header>表格区</template>
      <el-alert v-if="errorMessage" :title="errorMessage" type="error" show-icon :closable="false" class="error-tip" />
      <InventoryTable :loading="loading" :records="records" @inbound="openInbound" />
      <el-empty v-if="!loading && records.length === 0" description="暂无匹配库存" />
    </el-card>

    <el-card shadow="never" class="section-card">
      <template #header>分页区</template>
      <InventoryPagination
        :total="total"
        :page-num="query.pageNum"
        :page-size="query.pageSize"
        @change-page="handleChangePage"
        @change-size="handleChangeSize"
      />
    </el-card>

    <InventoryInboundDialog
      :visible="inboundDialog.visible"
      :vehicle-vin="inboundDialog.vehicleVin"
      :loading="inboundDialog.loading"
      @close="closeInbound"
      @submit="submitInbound"
    />

    <InventoryAlertDialog
      :visible="alertDialog.visible"
      :loading="alertDialog.loading"
      :records="alertDialog.records"
      @close="closeAlerts"
      @search="fetchAlerts"
    />
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import {
  inboundInventoryVehicle,
  queryInventoryAlerts,
  queryInventoryVehicles
} from '../../api/inventory'
import InventoryFilter from '../../components/inventory/InventoryFilter.vue'
import InventoryTable from '../../components/inventory/InventoryTable.vue'
import InventoryPagination from '../../components/inventory/InventoryPagination.vue'
import InventoryInboundDialog from '../../components/inventory/InventoryInboundDialog.vue'
import InventoryAlertDialog from '../../components/inventory/InventoryAlertDialog.vue'
import { isNetworkError } from '../../utils/request'

const loading = ref(false)
const errorMessage = ref('')
const records = ref([])
const total = ref(0)

const query = reactive({
  pageNum: 1,
  pageSize: 10,
  vehicleVin: undefined,
  currentStatus: undefined,
  color: undefined,
  brandId: undefined,
  modelId: undefined,
  inventoryInDateStart: undefined,
  inventoryInDateEnd: undefined,
  manufactureDateStart: undefined,
  manufactureDateEnd: undefined
})

const inboundDialog = reactive({
  visible: false,
  loading: false,
  vehicleVin: ''
})

const alertDialog = reactive({
  visible: false,
  loading: false,
  records: []
})

async function fetchVehicles() {
  loading.value = true
  errorMessage.value = ''
  try {
    const data = await queryInventoryVehicles({ ...query })
    records.value = data?.records || []
    total.value = data?.total || 0
    query.pageNum = data?.pageNo || query.pageNum
    query.pageSize = data?.pageSize || query.pageSize
  } catch (error) {
    records.value = []
    total.value = 0
    errorMessage.value = isNetworkError(error)
      ? '库存查询失败：服务连接异常，请检查后端服务或网络'
      : error?.message || '库存查询失败'
  } finally {
    loading.value = false
  }
}

function handleSearch(params) {
  Object.assign(query, {
    ...params,
    pageNum: 1
  })
  fetchVehicles()
}

function handleChangePage(pageNum) {
  query.pageNum = pageNum
  fetchVehicles()
}

function handleChangeSize(pageSize) {
  query.pageSize = pageSize
  query.pageNum = 1
  fetchVehicles()
}

function openInbound(row) {
  inboundDialog.visible = true
  inboundDialog.vehicleVin = row.vehicleVin
}

function closeInbound() {
  inboundDialog.visible = false
  inboundDialog.vehicleVin = ''
  inboundDialog.loading = false
}

async function submitInbound(payload) {
  inboundDialog.loading = true
  try {
    await inboundInventoryVehicle(payload)
    ElMessage.success('车辆入库成功')
    closeInbound()
    fetchVehicles()
  } catch (_) {
  } finally {
    inboundDialog.loading = false
  }
}

function openAlerts() {
  alertDialog.visible = true
  fetchAlerts({ onlyPositiveShortage: true })
}

function closeAlerts() {
  alertDialog.visible = false
  alertDialog.records = []
  alertDialog.loading = false
}

async function fetchAlerts(params) {
  alertDialog.loading = true
  try {
    const data = await queryInventoryAlerts(params)
    alertDialog.records = data || []
  } catch (_) {
    alertDialog.records = []
  } finally {
    alertDialog.loading = false
  }
}

onMounted(() => {
  fetchVehicles()
})
</script>

<style scoped>
.section-card {
  margin-bottom: 16px;
}

.error-tip {
  margin-bottom: 12px;
}
</style>
