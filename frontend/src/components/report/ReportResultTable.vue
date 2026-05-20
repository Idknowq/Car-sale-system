<template>
  <el-table :data="rows" border v-loading="loading" :empty-text="emptyText">
    <template v-if="reportType === 'monthly'">
      <el-table-column prop="statYear" label="年份" width="100" />
      <el-table-column prop="statMonth" label="月份" width="100" />
      <el-table-column prop="staffId" label="员工ID" width="100" />
      <el-table-column prop="staffName" label="员工姓名" min-width="140" />
      <el-table-column prop="orderCount" label="订单数" width="100" />
      <el-table-column prop="salesAmount" label="销售额" min-width="140" />
      <el-table-column prop="grossProfit" label="毛利" min-width="140" />
    </template>

    <template v-else-if="reportType === 'performance'">
      <el-table-column prop="salesRank" label="排名" width="90" />
      <el-table-column prop="periodLabel" label="统计周期" min-width="140" />
      <el-table-column prop="staffId" label="员工ID" width="100" />
      <el-table-column prop="staffNo" label="员工工号" min-width="120" />
      <el-table-column prop="staffName" label="员工姓名" min-width="140" />
      <el-table-column prop="orderCount" label="订单数" width="100" />
      <el-table-column prop="salesAmount" label="销售额" min-width="140" />
      <el-table-column prop="grossProfit" label="毛利" min-width="140" />
    </template>

    <template v-else>
      <el-table-column prop="brandName" label="品牌" min-width="120" />
      <el-table-column prop="modelName" label="车型" min-width="140" />
      <el-table-column prop="modelYear" label="年款" width="100" />
      <el-table-column prop="trimName" label="配置" min-width="140" />
      <el-table-column prop="salesVolume" label="销量" width="100" />
      <el-table-column prop="salesAmount" label="销售额" min-width="140" />
    </template>
  </el-table>
</template>

<script setup>
defineProps({
  reportType: {
    type: String,
    required: true
  },
  rows: {
    type: Array,
    default: () => []
  },
  loading: {
    type: Boolean,
    default: false
  },
  emptyText: {
    type: String,
    default: '暂无数据'
  }
})
</script>
