<template>
  <div class="workstation-container">
    <!-- 左侧候诊队列 -->
    <div class="queue-panel">
      <div class="panel-header">
        <h2>当前候诊队列</h2>
      </div>
      <div class="queue-list">
        <div
            v-for="patient in queueList"
            :key="patient.id"
            class="queue-item"
            :class="{ active: currentPatient && currentPatient.id === patient.id }"
            @click="selectPatient(patient)"
        >
          <div class="patient-name">{{ patient.patientName }}</div>
          <div class="status-text">状态: {{ patient.statusText || '待就诊' }}</div>
        </div>
        <div v-if="queueList.length === 0" class="empty-list">暂无候诊患者</div>
      </div>
    </div>

    <!-- 右侧接诊面板 -->
    <div class="consult-panel">
      <div v-if="currentPatient" class="patient-detail-box">
        <div class="detail-header">
          <h2>患者详情</h2>
          <el-tag :type="currentPatient.status === 1 ? 'success' : 'info'">
            {{ currentPatient.status === 1 ? '正在接诊' : '待接诊' }}
          </el-tag>
        </div>

        <!-- 患者基础信息 -->
        <div class="patient-info">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="姓名">{{ currentPatient.patientName }}</el-descriptions-item>
            <el-descriptions-item label="电话">{{ currentPatient.phone || '-' }}</el-descriptions-item>
            <el-descriptions-item label="过敏史">{{ currentPatient.allergyInfo || '无' }}</el-descriptions-item>
          </el-descriptions>
        </div>

        <!-- 操作按钮区 -->
        <div class="action-buttons">
          <!-- 如果状态是待接诊(0)，显示开始接诊按钮 -->
          <el-button v-if="currentPatient.status === 0" type="primary" @click="startConsultAction">开始接诊</el-button>
          <!-- 如果已经是接诊中(1)，显示写病历按钮 -->
          <el-button v-if="currentPatient.status === 1" type="warning" @click="openRecordDialog">书写病历</el-button>
        </div>
      </div>
      <div v-else class="empty-state">
        <h3>请在左侧选择患者进行叫号或接诊</h3>
      </div>
    </div>

    <!-- 病历录入弹窗 -->
    <el-dialog v-model="dialogVisible" title="填写病历信息" width="600px" append-to-body>
      <el-form :model="recordForm" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="主诉" prop="chiefComplaint">
          <el-input v-model="recordForm.chiefComplaint" placeholder="请输入患者主诉" />
        </el-form-item>
        <el-form-item label="现病史" prop="presentIllness">
          <el-input v-model="recordForm.presentIllness" type="textarea" placeholder="请输入现病史" />
        </el-form-item>
        <el-form-item label="既往史" prop="pastHistory">
          <el-input v-model="recordForm.pastHistory" placeholder="请输入既往史" />
        </el-form-item>
        <el-form-item label="过敏记录" prop="allergyRecord">
          <el-input v-model="recordForm.allergyRecord" placeholder="请输入过敏记录" />
        </el-form-item>
        <el-form-item label="诊断" prop="diagnosis">
          <el-input v-model="recordForm.diagnosis" placeholder="请输入初步诊断" />
        </el-form-item>
        <el-form-item label="医嘱" prop="advice">
          <el-input v-model="recordForm.advice" type="textarea" placeholder="请输入医嘱建议" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="submitRecord">保 存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getWaitingList, startConsult as requestStartConsult, submitMedicalRecord } from '@/api/doctor'

// 定义变量
const queueList = ref([])
const currentPatient = ref(null) // 这里和模板保持一致

const dialogVisible = ref(false)
const formRef = ref(null)
const recordForm = ref({
  appointmentId: 0,
  chiefComplaint: '',
  presentIllness: '',
  pastHistory: '',
  allergyRecord: '',
  diagnosis: '',
  advice: ''
})

const rules = {
  chiefComplaint: [{ required: true, message: '请输入主诉', trigger: 'blur' }],
  diagnosis: [{ required: true, message: '请输入诊断', trigger: 'blur' }]
}

// 获取候诊队列
const fetchQueue = async () => {
  const doctorId = 1
  const today = new Date().toISOString().split('T')[0]
  try {
    const res = await getWaitingList({ doctorId, visitDate: today })
    const list = Array.isArray(res) ? res : (res.data || res.rows || res.list || [])
    queueList.value = list
  } catch (error) {
    console.error(error)
    ElMessage.error('获取候诊列表失败')
  }
}

// 点击左侧患者
const selectPatient = (patient) => {
  currentPatient.value = patient
}

// 点击“开始接诊”
const startConsultAction = async () => {
  if (!currentPatient.value) return
  try {
    // 调用后端接口更新状态
    await requestStartConsult(currentPatient.value.id)

    // 更新当前显示的患者状态
    currentPatient.value.status = 1
    currentPatient.value.statusText = '接诊中'

    // 同步更新左侧列表
    const item = queueList.value.find(p => p.id === currentPatient.value.id)
    if (item) {
      item.status = 1
      item.statusText = '接诊中'
    }
    ElMessage.success('接诊成功，可以书写病历')
  } catch (error) {
    console.error(error)
    ElMessage.error('接诊失败')
  }
}

// 打开病历弹窗
const openRecordDialog = () => {
  if (!currentPatient.value) return
  dialogVisible.value = true
  recordForm.value = {
    appointmentId: currentPatient.value.id,
    chiefComplaint: '',
    presentIllness: '',
    pastHistory: '',
    allergyRecord: '',
    diagnosis: '',
    advice: ''
  }
}

// 保存病历
const submitRecord = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        await submitMedicalRecord(recordForm.value)
        ElMessage.success('病历保存成功')
        dialogVisible.value = false
        fetchQueue() // 保存后刷新列表
      } catch (error) {
        console.error(error)
        ElMessage.error('病历保存失败')
      }
    }
  })
}

onMounted(() => {
  fetchQueue()
})
</script>

<style scoped>
.workstation-container { display: flex; height: calc(100vh - 80px); }
.queue-panel { width: 280px; border-right: 1px solid #e4e7ed; background: #fff; }
.panel-header { padding: 15px; border-bottom: 1px solid #ebeef5; }
.panel-header h2 { margin: 0; font-size: 16px; }
.queue-list { flex: 1; overflow-y: auto; background: #f5f7fa; }
.queue-item { padding: 15px; border-bottom: 1px solid #ebeef5; cursor: pointer; background: #fff; }
.queue-item:hover { background: #ecf5ff; }
.queue-item.active { background: #e6f3ff; border-left: 3px solid #409eff; }
.patient-name { font-weight: bold; }
.status-text { font-size: 12px; color: #909399; }
.consult-panel { flex: 1; padding: 30px; background: #f0f2f5; }
.empty-state { text-align: center; margin-top: 150px; color: #909399; }
.patient-detail-box { background: #fff; padding: 20px; border-radius: 4px; }
.detail-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.action-buttons { margin-top: 30px; }
</style>