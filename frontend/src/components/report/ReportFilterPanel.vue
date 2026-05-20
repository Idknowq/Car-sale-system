<template>
  <el-form :model="modelValue" inline label-width="120px">
    <el-form-item label="报表类型">
      <el-radio-group v-model="modelValue.reportType">
        <el-radio-button label="monthly">月度销售统计</el-radio-button>
        <el-radio-button label="performance">销售业绩榜</el-radio-button>
        <el-radio-button label="bestSelling">畅销车型排行</el-radio-button>
      </el-radio-group>
    </el-form-item>

    <el-form-item v-if="modelValue.reportType === 'monthly'" label="年份">
      <el-input-number v-model="modelValue.year" :min="2000" :max="2100" />
    </el-form-item>
    <el-form-item v-if="modelValue.reportType === 'monthly'" label="月份">
      <el-input-number v-model="modelValue.month" :min="1" :max="12" />
    </el-form-item>

    <template v-if="modelValue.reportType === 'performance'">
      <el-form-item label="周期类型">
        <el-radio-group v-model="modelValue.periodType">
          <el-radio-button label="MONTH">月度</el-radio-button>
          <el-radio-button label="QUARTER">季度</el-radio-button>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="年份">
        <el-input-number v-model="modelValue.statYear" :min="2000" :max="2100" />
      </el-form-item>
      <el-form-item v-if="modelValue.periodType === 'MONTH'" label="月份">
        <el-input-number v-model="modelValue.statMonth" :min="1" :max="12" />
      </el-form-item>
      <el-form-item v-else label="季度">
        <el-input-number v-model="modelValue.statQuarter" :min="1" :max="4" />
      </el-form-item>
      <el-form-item label="Top N">
        <el-input-number v-model="modelValue.topN" :min="1" :max="100" />
      </el-form-item>
    </template>

    <el-form-item v-if="modelValue.reportType === 'bestSelling'" label="Top N">
      <el-input-number v-model="modelValue.topN" :min="1" :max="100" />
    </el-form-item>

    <el-form-item>
      <el-button type="primary" :loading="loading" @click="$emit('search')">查询</el-button>
      <el-button @click="$emit('reset')">重置</el-button>
    </el-form-item>
  </el-form>
</template>

<script setup>
defineProps({
  modelValue: {
    type: Object,
    required: true
  },
  loading: {
    type: Boolean,
    default: false
  }
})

defineEmits(['search', 'reset'])
</script>
