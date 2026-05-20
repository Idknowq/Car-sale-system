<template>
  <el-dialog :model-value="visible" title="车辆入库" width="520px" @close="$emit('close')">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
      <el-form-item label="车辆VIN">
        <el-input v-model="form.vehicleVin" disabled />
      </el-form-item>
      <el-form-item label="员工ID" prop="staffId">
        <el-input-number v-model="form.staffId" :min="1" :controls="false" style="width: 100%" />
      </el-form-item>
      <el-form-item label="入库日期" prop="inventoryInDate">
        <el-date-picker v-model="form.inventoryInDate" value-format="YYYY-MM-DD" type="date" style="width: 100%" />
      </el-form-item>
      <el-form-item label="备注" prop="reason">
        <el-input v-model.trim="form.reason" type="textarea" :rows="2" maxlength="200" show-word-limit />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button :disabled="loading" @click="$emit('close')">取消</el-button>
      <el-button type="primary" :loading="loading" @click="onSubmit">提交</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { reactive, ref, watch } from 'vue'

const props = defineProps({
  visible: { type: Boolean, default: false },
  loading: { type: Boolean, default: false },
  vehicleVin: { type: String, default: '' }
})

const emit = defineEmits(['submit', 'close'])
const formRef = ref()

const form = reactive({
  vehicleVin: '',
  staffId: null,
  inventoryInDate: '',
  reason: ''
})

const rules = {
  staffId: [{ required: true, message: '请输入员工ID', trigger: 'blur' }],
  inventoryInDate: [{ required: true, message: '请选择入库日期', trigger: 'change' }]
}

watch(
  () => props.visible,
  (v) => {
    if (!v) return
    form.vehicleVin = props.vehicleVin
    form.staffId = null
    form.inventoryInDate = ''
    form.reason = ''
  }
)

async function onSubmit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  emit('submit', {
    vehicleVin: form.vehicleVin,
    staffId: form.staffId,
    inventoryInDate: form.inventoryInDate,
    reason: form.reason || undefined
  })
}
</script>
