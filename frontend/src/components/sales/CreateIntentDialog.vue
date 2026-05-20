<template>
  <el-dialog
    :model-value="visible"
    title="创建意向客户"
    width="760px"
    @close="emit('update:visible', false)"
  >
    <el-form ref="formRef" :model="form" :rules="rules" label-width="110px">
      <el-row :gutter="12">
        <el-col :span="12">
          <el-form-item label="客户姓名" prop="customerName">
            <el-input v-model.trim="form.customerName" maxlength="50" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="手机号" prop="phone">
            <el-input v-model.trim="form.phone" maxlength="20" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="12">
        <el-col :span="12">
          <el-form-item label="证件号" prop="idCard">
            <el-input v-model.trim="form.idCard" maxlength="32" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="性别" prop="gender">
            <el-select v-model="form.gender" clearable placeholder="可选">
              <el-option label="男(M)" value="M" />
              <el-option label="女(F)" value="F" />
              <el-option label="其他(OTHER)" value="OTHER" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="12">
        <el-col :span="12">
          <el-form-item label="车型ID" prop="modelId">
            <el-input-number v-model="form.modelId" :min="1" :step="1" controls-position="right" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="跟进员工ID" prop="staffId">
            <el-input-number v-model="form.staffId" :min="1" :step="1" controls-position="right" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="12">
        <el-col :span="12">
          <el-form-item label="意向等级" prop="intentLevel">
            <el-select v-model="form.intentLevel" placeholder="请选择">
              <el-option label="高(HIGH)" value="HIGH" />
              <el-option label="中(MEDIUM)" value="MEDIUM" />
              <el-option label="低(LOW)" value="LOW" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="首次到店日期" prop="firstVisitDate">
            <el-date-picker
              v-model="form.firstVisitDate"
              type="date"
              value-format="YYYY-MM-DD"
              placeholder="YYYY-MM-DD"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="12">
        <el-col :span="12">
          <el-form-item label="下次联系时间" prop="nextContactTime">
            <el-date-picker
              v-model="form.nextContactTime"
              type="datetime"
              value-format="YYYY-MM-DD HH:mm:ss"
              placeholder="YYYY-MM-DD HH:mm:ss"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="来源渠道" prop="sourceChannel">
            <el-input v-model.trim="form.sourceChannel" maxlength="30" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="地址" prop="address">
        <el-input v-model.trim="form.address" maxlength="200" />
      </el-form-item>
      <el-form-item label="备注" prop="remark">
        <el-input v-model.trim="form.remark" type="textarea" :rows="3" maxlength="500" show-word-limit />
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="emit('update:visible', false)">取消</el-button>
      <el-button type="primary" :loading="submitting" @click="submit">提交</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { reactive, ref, watch } from 'vue'

const props = defineProps({
  visible: { type: Boolean, default: false },
  submitting: { type: Boolean, default: false }
})

const emit = defineEmits(['update:visible', 'submit'])

const formRef = ref()

const createDefaultForm = () => ({
  customerName: '',
  gender: '',
  phone: '',
  idCard: '',
  address: '',
  firstVisitDate: '',
  sourceChannel: '',
  modelId: null,
  intentLevel: 'MEDIUM',
  remark: '',
  staffId: null,
  nextContactTime: ''
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
  customerName: [{ required: true, message: '请输入客户姓名', trigger: 'blur' }],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1\d{10}$/, message: '手机号格式不正确', trigger: 'blur' }
  ],
  idCard: [{ required: true, message: '请输入证件号', trigger: 'blur' }],
  modelId: [{ required: true, message: '请输入车型ID', trigger: 'change' }],
  intentLevel: [{ required: true, message: '请选择意向等级', trigger: 'change' }],
  staffId: [{ required: true, message: '请输入员工ID', trigger: 'change' }]
}

const submit = async () => {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return
  emit('submit', { ...form })
}
</script>
