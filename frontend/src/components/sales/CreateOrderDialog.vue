<template>
  <el-dialog
    :model-value="visible"
    title="创建销售订单"
    width="760px"
    @close="emit('update:visible', false)"
  >
    <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
      <el-row :gutter="12">
        <el-col :span="12">
          <el-form-item label="客户ID" prop="customerId">
            <el-input-number v-model="form.customerId" :min="1" :step="1" controls-position="right" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="员工ID" prop="staffId">
            <el-input-number v-model="form.staffId" :min="1" :step="1" controls-position="right" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="车辆VIN" prop="vehicleVin">
        <el-input v-model.trim="form.vehicleVin" maxlength="17" placeholder="17位VIN" />
      </el-form-item>

      <el-row :gutter="12">
        <el-col :span="12">
          <el-form-item label="车辆金额" prop="vehicleAmount">
            <el-input-number v-model="form.vehicleAmount" :min="0.01" :precision="2" :step="1000" controls-position="right" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="定金" prop="depositAmount">
            <el-input-number v-model="form.depositAmount" :min="0" :precision="2" :step="1000" controls-position="right" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="12">
        <el-col :span="12">
          <el-form-item label="精品金额" prop="optionAmount">
            <el-input-number v-model="form.optionAmount" :min="0" :precision="2" :step="500" controls-position="right" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="保险金额" prop="insuranceAmount">
            <el-input-number v-model="form.insuranceAmount" :min="0" :precision="2" :step="500" controls-position="right" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="12">
        <el-col :span="12">
          <el-form-item label="折扣金额" prop="discountAmount">
            <el-input-number v-model="form.discountAmount" :precision="2" :step="500" controls-position="right" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="其他金额" prop="otherAmount">
            <el-input-number v-model="form.otherAmount" :min="0" :precision="2" :step="500" controls-position="right" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <template #footer>
      <el-button @click="emit('update:visible', false)">取消</el-button>
      <el-button type="primary" :loading="submitting" @click="submit">提交</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { reactive, ref, watch } from 'vue'
import { ElMessage } from 'element-plus'

const props = defineProps({
  visible: { type: Boolean, default: false },
  submitting: { type: Boolean, default: false }
})

const emit = defineEmits(['update:visible', 'submit'])

const formRef = ref()

const createDefaultForm = () => ({
  customerId: null,
  staffId: null,
  vehicleVin: '',
  depositAmount: 0,
  vehicleAmount: null,
  optionAmount: 0,
  insuranceAmount: 0,
  discountAmount: 0,
  otherAmount: 0
})

const form = reactive(createDefaultForm())

watch(
  () => props.visible,
  (val) => {
    if (val) {
      Object.assign(form, createDefaultForm())
      formRef.value?.clearValidate()
    }
  }
)

const rules = {
  customerId: [{ required: true, message: '请输入客户ID', trigger: 'change' }],
  staffId: [{ required: true, message: '请输入员工ID', trigger: 'change' }],
  vehicleVin: [
    { required: true, message: '请输入VIN', trigger: 'blur' },
    { min: 17, max: 17, message: 'VIN必须为17位', trigger: 'blur' }
  ],
  vehicleAmount: [{ required: true, message: '请输入车辆金额', trigger: 'change' }],
  depositAmount: [{ required: true, message: '请输入定金', trigger: 'change' }]
}

const submit = async () => {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return

  const total = Number(form.vehicleAmount) + Number(form.optionAmount) + Number(form.insuranceAmount) + Number(form.discountAmount) + Number(form.otherAmount)
  if (Number(form.depositAmount) > total) {
    ElMessage.warning('定金不能大于订单总金额')
    return
  }

  emit('submit', { ...form })
}
</script>
