<template>
  <div class="module-page">
    <el-card shadow="never" class="section-card">
      <template #header>筛选区</template>
      <ReportFilterPanel :model-value="filters" :loading="loading" @search="handleSearch" @reset="handleReset" />
    </el-card>

    <el-card shadow="never" class="section-card">
      <template #header>表格区</template>
      <ReportResultTable
        :report-type="filters.reportType"
        :rows="displayRows"
        :loading="loading"
        :empty-text="queried ? '暂无匹配数据' : '请先设置筛选条件后查询'"
      />
    </el-card>

    <el-card v-if="isMonthlyReport" shadow="never" class="section-card">
      <template #header>分页区</template>
      <div class="pager-row">
        <div class="summary">共 {{ total }} 条</div>
        <el-pagination
          v-model:current-page="pageNo"
          v-model:page-size="pageSize"
          :total="total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handlePageSizeChange"
          @current-change="handlePageNoChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { computed, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import ReportFilterPanel from '../../components/report/ReportFilterPanel.vue'
import ReportResultTable from '../../components/report/ReportResultTable.vue'
import {
  queryBestSellingModels,
  queryMonthlySalesReport,
  querySalesPerformanceRanking
} from '../../api/report'

const createDefaultFilters = () => ({
  reportType: 'monthly',
  year: new Date().getFullYear(),
  month: new Date().getMonth() + 1,
  periodType: 'MONTH',
  statYear: new Date().getFullYear(),
  statMonth: new Date().getMonth() + 1,
  statQuarter: 1,
  topN: 10
})

const filters = reactive(createDefaultFilters())
const loading = ref(false)
const queried = ref(false)
const rows = ref([])

const pageNo = ref(1)
const pageSize = ref(10)
const monthlyTotal = ref(0)

const isMonthlyReport = computed(() => filters.reportType === 'monthly')
const total = computed(() => (isMonthlyReport.value ? monthlyTotal.value : rows.value.length))
const displayRows = computed(() => rows.value)

const validateFilters = () => {
  if (filters.reportType === 'monthly') {
    if (!filters.year || filters.year < 2000 || filters.year > 2100) {
      ElMessage.warning('年份必须在 2000-2100 之间')
      return false
    }
    if (!filters.month || filters.month < 1 || filters.month > 12) {
      ElMessage.warning('月份必须在 1-12 之间')
      return false
    }
  }

  if (filters.reportType === 'performance') {
    if (!filters.statYear || filters.statYear < 2000 || filters.statYear > 2100) {
      ElMessage.warning('统计年份必须在 2000-2100 之间')
      return false
    }
    if (filters.periodType === 'MONTH' && (!filters.statMonth || filters.statMonth < 1 || filters.statMonth > 12)) {
      ElMessage.warning('统计月份必须在 1-12 之间')
      return false
    }
    if (filters.periodType === 'QUARTER' && (!filters.statQuarter || filters.statQuarter < 1 || filters.statQuarter > 4)) {
      ElMessage.warning('统计季度必须在 1-4 之间')
      return false
    }
    if (!filters.topN || filters.topN < 1 || filters.topN > 100) {
      ElMessage.warning('Top N 必须在 1-100 之间')
      return false
    }
  }

  if (filters.reportType === 'bestSelling') {
    if (!filters.topN || filters.topN < 1 || filters.topN > 100) {
      ElMessage.warning('Top N 必须在 1-100 之间')
      return false
    }
  }

  return true
}

const buildRequest = () => {
  if (filters.reportType === 'monthly') {
    return {
      runner: queryMonthlySalesReport,
      params: {
        year: filters.year,
        month: filters.month,
        pageNo: pageNo.value,
        pageSize: pageSize.value
      }
    }
  }

  if (filters.reportType === 'performance') {
    const params = {
      periodType: filters.periodType,
      statYear: filters.statYear,
      topN: filters.topN
    }
    if (filters.periodType === 'MONTH') {
      params.statMonth = filters.statMonth
    } else {
      params.statQuarter = filters.statQuarter
    }
    return {
      runner: querySalesPerformanceRanking,
      params
    }
  }

  return {
    runner: queryBestSellingModels,
    params: {
      topN: filters.topN
    }
  }
}

const handleSearch = async () => {
  if (!validateFilters()) {
    return
  }

  const { runner, params } = buildRequest()

  loading.value = true
  try {
    const result = await runner(params)
    if (isMonthlyReport.value) {
      rows.value = Array.isArray(result?.records) ? result.records : []
      monthlyTotal.value = Number(result?.total || 0)
      pageNo.value = Number(result?.pageNo || pageNo.value)
      pageSize.value = Number(result?.pageSize || pageSize.value)
    } else {
      rows.value = Array.isArray(result) ? result : []
      monthlyTotal.value = 0
      pageNo.value = 1
      pageSize.value = 10
    }
    queried.value = true
  } finally {
    loading.value = false
  }
}

const handleReset = () => {
  Object.assign(filters, createDefaultFilters())
  rows.value = []
  monthlyTotal.value = 0
  queried.value = false
  pageNo.value = 1
  pageSize.value = 10
}

const handlePageNoChange = (value) => {
  pageNo.value = value
  if (isMonthlyReport.value && queried.value) {
    handleSearch()
  }
}

const handlePageSizeChange = (value) => {
  pageSize.value = value
  pageNo.value = 1
  if (isMonthlyReport.value && queried.value) {
    handleSearch()
  }
}
</script>

<style scoped>
.section-card {
  margin-bottom: 16px;
}

.pager-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  flex-wrap: wrap;
}

.summary {
  color: #606266;
}
</style>
