<template>
  <div class="module-page">
    <el-card shadow="never" class="section-card">
      <template #header>筛选区</template>
      <el-form :inline="true" :model="queryForm" class="filter-form">
        <el-form-item label="员工ID" required>
          <el-input-number v-model="queryForm.staffId" :min="1" :step="1" controls-position="right" />
        </el-form-item>
        <el-form-item label="客户姓名">
          <el-input v-model.trim="queryForm.customerName" placeholder="模糊匹配" clearable />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model.trim="queryForm.phone" placeholder="精确匹配" clearable />
        </el-form-item>
        <el-form-item label="订单状态">
          <el-select v-model="queryForm.orderStatus" clearable placeholder="全部" class="status-select">
            <el-option label="LOCKED" value="LOCKED" />
            <el-option label="DEPOSIT_PAID" value="DEPOSIT_PAID" />
            <el-option label="COMPLETED" value="COMPLETED" />
            <el-option label="CANCELLED" value="CANCELLED" />
          </el-select>
        </el-form-item>
        <el-form-item label="VIN">
          <el-input v-model.trim="queryForm.vehicleVin" clearable />
        </el-form-item>
        <el-form-item label="下单时间">
          <el-date-picker
            v-model="queryForm.createdAtRange"
            type="datetimerange"
            range-separator="-"
            start-placeholder="开始"
            end-placeholder="结束"
            value-format="YYYY-MM-DD HH:mm:ss"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleSearch">查询</el-button>
          <el-button :disabled="loading" @click="handleReset">重置</el-button>
          <el-button type="warning" @click="orderDialogVisible = true">创建销售订单</el-button>
          <el-button type="success" @click="intentDialogVisible = true">创建意向客户</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" class="section-card">
      <template #header>表格区</template>
      <el-table :data="tableData" border stripe v-loading="loading" empty-text="暂无订单数据">
        <el-table-column prop="orderId" label="订单ID" width="100" />
        <el-table-column prop="customerId" label="客户ID" width="100" />
        <el-table-column prop="customerName" label="客户姓名" min-width="120" />
        <el-table-column prop="phone" label="手机号" min-width="130" />
        <el-table-column prop="staffId" label="员工ID" width="100" />
        <el-table-column prop="vehicleVin" label="VIN" min-width="180" />
        <el-table-column prop="totalAmount" label="总金额" min-width="110" :formatter="formatMoneyCell" />
        <el-table-column prop="depositAmount" label="定金" min-width="110" :formatter="formatMoneyCell" />
        <el-table-column prop="orderStatus" label="状态" min-width="120" :formatter="formatOrderStatusCell" />
        <el-table-column prop="createdAt" label="创建时间" min-width="180" :formatter="formatDateTimeCell" />
        <el-table-column prop="deliveryTime" label="交付时间" min-width="180" :formatter="formatDateTimeCell" />
        <el-table-column label="操作" width="140" fixed="right">
          <template #default="{ row }">
            <el-button
              type="primary"
              link
              :disabled="!canComplete(row) || completeSubmitting"
              @click="handleCompleteOrder(row)"
            >
              完成订单
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-card shadow="never" class="section-card">
      <template #header>分页区</template>
      <div class="pager-row">
        <div class="total-text">共 {{ total }} 条</div>
        <el-pagination
          background
          layout="prev, pager, next, sizes"
          :current-page="queryForm.pageNo"
          :page-size="queryForm.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="total"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
        />
      </div>
    </el-card>

    <CreateOrderDialog
      v-model:visible="orderDialogVisible"
      :submitting="orderSubmitting"
      @submit="handleCreateOrder"
    />

    <CreateIntentDialog
      v-model:visible="intentDialogVisible"
      :submitting="intentSubmitting"
      @submit="handleCreateIntent"
    />
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import CreateIntentDialog from '../../components/sales/CreateIntentDialog.vue'
import CreateOrderDialog from '../../components/sales/CreateOrderDialog.vue'
import { completeSalesOrder, createCustomerIntent, createSalesOrder, queryMyOrders } from '../../api/sales'
import { formatDateTime, formatMoney, formatOrderStatus } from '../../utils/format'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)

const createDefaultQuery = () => ({
  staffId: null,
  customerName: '',
  phone: '',
  orderStatus: '',
  vehicleVin: '',
  createdAtRange: [],
  pageNo: 1,
  pageSize: 10
})

const queryForm = reactive(createDefaultQuery())

const orderDialogVisible = ref(false)
const orderSubmitting = ref(false)
const completeSubmitting = ref(false)
const intentDialogVisible = ref(false)
const intentSubmitting = ref(false)

const buildQueryParams = () => {
  const params = {
    staffId: queryForm.staffId,
    customerName: queryForm.customerName || undefined,
    phone: queryForm.phone || undefined,
    orderStatus: queryForm.orderStatus || undefined,
    vehicleVin: queryForm.vehicleVin || undefined,
    pageNo: queryForm.pageNo,
    pageSize: queryForm.pageSize
  }
  if (queryForm.createdAtRange?.length === 2) {
    params.createdAtStart = queryForm.createdAtRange[0]
    params.createdAtEnd = queryForm.createdAtRange[1]
  }
  return params
}

const loadOrders = async () => {
  if (!queryForm.staffId) {
    ElMessage.warning('请先输入员工ID后查询')
    tableData.value = []
    total.value = 0
    return
  }

  loading.value = true
  try {
    const res = await queryMyOrders(buildQueryParams())
    tableData.value = Array.isArray(res?.records) ? res.records : []
    total.value = Number(res?.total || 0)
  } catch {
    tableData.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  queryForm.pageNo = 1
  loadOrders()
}

const handleReset = () => {
  Object.assign(queryForm, createDefaultQuery())
  tableData.value = []
  total.value = 0
}

const handleCurrentChange = (page) => {
  queryForm.pageNo = page
  loadOrders()
}

const handleSizeChange = (size) => {
  queryForm.pageSize = size
  queryForm.pageNo = 1
  loadOrders()
}

const handleCreateOrder = async (payload) => {
  orderSubmitting.value = true
  try {
    const res = await createSalesOrder(payload)
    ElMessage.success(`订单创建成功：orderId=${res.orderId}`)
    orderDialogVisible.value = false
    queryForm.staffId = payload.staffId
    queryForm.pageNo = 1
    await loadOrders()
  } finally {
    orderSubmitting.value = false
  }
}

const handleCreateIntent = async (payload) => {
  intentSubmitting.value = true
  try {
    const res = await createCustomerIntent(payload)
    ElMessage.success(`创建成功：customerId=${res.customerId}, intentId=${res.intentId}`)
    intentDialogVisible.value = false
  } finally {
    intentSubmitting.value = false
  }
}

const canComplete = (row) => row?.orderStatus === 'LOCKED' || row?.orderStatus === 'DEPOSIT_PAID'

const handleCompleteOrder = async (row) => {
  if (!queryForm.staffId) {
    ElMessage.warning('请先输入员工ID')
    return
  }
  if (!canComplete(row)) return

  try {
    await ElMessageBox.confirm(
      `确认将订单 ${row.orderId} 更新为已完成吗？`,
      '完成订单确认',
      { type: 'warning' }
    )
  } catch {
    return
  }

  completeSubmitting.value = true
  try {
    await completeSalesOrder(row.orderId, { staffId: queryForm.staffId })
    ElMessage.success(`订单 ${row.orderId} 已完成`)
    await loadOrders()
  } finally {
    completeSubmitting.value = false
  }
}

const formatMoneyCell = (_row, _column, cellValue) => formatMoney(cellValue)

const formatDateTimeCell = (_row, _column, cellValue) => formatDateTime(cellValue)

const formatOrderStatusCell = (_row, _column, cellValue) => formatOrderStatus(cellValue)
</script>

<style scoped>
.section-card {
  margin-bottom: 16px;
}

.filter-form {
  display: flex;
  flex-wrap: wrap;
}

.status-select {
  min-width: 180px;
}

.pager-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
}

.total-text {
  color: #606266;
}
</style>
