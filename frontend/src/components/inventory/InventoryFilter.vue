<template>
  <el-form :model="form" label-width="88px" inline>
    <el-form-item label="VIN">
      <el-input v-model.trim="form.vehicleVin" placeholder="支持前缀匹配" clearable style="width: 200px" />
    </el-form-item>

    <el-form-item label="库存状态">
      <el-select v-model="form.currentStatus" placeholder="全部" clearable style="width: 160px">
        <el-option v-for="item in statusOptions" :key="item.value" :label="item.label" :value="item.value" />
      </el-select>
    </el-form-item>

    <el-form-item label="颜色">
      <el-input v-model.trim="form.color" placeholder="颜色" clearable style="width: 140px" />
    </el-form-item>

    <el-form-item label="品牌ID">
      <el-input-number v-model="form.brandId" :min="1" :controls="false" placeholder="brandId" style="width: 140px" />
    </el-form-item>

    <el-form-item label="车型ID">
      <el-input-number v-model="form.modelId" :min="1" :controls="false" placeholder="modelId" style="width: 140px" />
    </el-form-item>

    <el-form-item label="入库日期">
      <el-date-picker
        v-model="form.inventoryRange"
        type="daterange"
        range-separator="至"
        start-placeholder="开始"
        end-placeholder="结束"
        value-format="YYYY-MM-DD"
      />
    </el-form-item>

    <el-form-item label="生产日期">
      <el-date-picker
        v-model="form.manufactureRange"
        type="daterange"
        range-separator="至"
        start-placeholder="开始"
        end-placeholder="结束"
        value-format="YYYY-MM-DD"
      />
    </el-form-item>

    <el-form-item>
      <el-button type="primary" :loading="loading" @click="onSearch">查询</el-button>
      <el-button :disabled="loading" @click="onReset">重置</el-button>
      <el-button :disabled="loading" @click="$emit('show-alerts')">库存预警</el-button>
    </el-form-item>
  </el-form>
</template>

<script setup>
import { reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { INVENTORY_STATUS_OPTIONS as statusOptions } from '../../constants/status'

const props = defineProps({
  loading: { type: Boolean, default: false }
})

const emit = defineEmits(['search', 'show-alerts'])

const form = reactive({
  vehicleVin: '',
  currentStatus: '',
  color: '',
  brandId: null,
  modelId: null,
  inventoryRange: [],
  manufactureRange: []
})

function buildParams() {
  const [inventoryInDateStart, inventoryInDateEnd] = form.inventoryRange || []
  const [manufactureDateStart, manufactureDateEnd] = form.manufactureRange || []
  return {
    vehicleVin: form.vehicleVin || undefined,
    currentStatus: form.currentStatus || undefined,
    color: form.color || undefined,
    brandId: form.brandId ?? undefined,
    modelId: form.modelId ?? undefined,
    inventoryInDateStart,
    inventoryInDateEnd,
    manufactureDateStart,
    manufactureDateEnd
  }
}

function validateRange() {
  const [invStart, invEnd] = form.inventoryRange || []
  const [manStart, manEnd] = form.manufactureRange || []
  if (invStart && invEnd && invStart > invEnd) {
    ElMessage.warning('入库日期范围不合法')
    return false
  }
  if (manStart && manEnd && manStart > manEnd) {
    ElMessage.warning('生产日期范围不合法')
    return false
  }
  return true
}

function onSearch() {
  if (!validateRange()) return
  emit('search', buildParams())
}

function onReset() {
  form.vehicleVin = ''
  form.currentStatus = ''
  form.color = ''
  form.brandId = null
  form.modelId = null
  form.inventoryRange = []
  form.manufactureRange = []
  emit('search', buildParams())
}
</script>
