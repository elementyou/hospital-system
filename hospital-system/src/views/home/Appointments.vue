<template>
  <div style="padding: 20px; background-color: #fff; border-radius: 8px;">
    <h2>预约挂号</h2>

    <!-- 预约表单 -->
    <el-form :inline="true" style="margin-bottom: 20px;">
      <el-form-item label="医生ID">
        <el-input
            v-model="form.doctorId"
            placeholder="请输入医生ID"
            style="width: 150px"
            type="number"
        ></el-input>
      </el-form-item>
      <el-form-item label="排班ID">
        <el-input
            v-model="form.scheduleId"
            placeholder="请输入排班ID"
            style="width: 150px"
            type="number"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-button
            type="primary"
            @click="addAppointment"
            :loading="submitting"
        >
          提交预约
        </el-button>
      </el-form-item>
    </el-form>

    <!-- 显示刚刚预约成功的结果 -->
    <div v-if="latestAppointment" style="margin-bottom: 20px;">
      <el-card shadow="never" style="background-color: #f5f7fa;">
        <div style="display: flex; justify-content: space-between; align-items: center;">
          <span style="font-weight: bold; color: #409EFF;">预约成功！</span>
          <el-tag :type="latestAppointment.status === 0 ? 'success' : 'danger'">
            状态：{{ latestAppointment.status === 0 ? '已预约' : '未知状态' }}
          </el-tag>
        </div>
        <div style="margin-top: 10px; color: #666;">
          就诊时间：{{ latestAppointment.visitDate }} {{ latestAppointment.timeSlot }}
        </div>
      </el-card>
    </div>

    <!-- 预约记录表格 -->
    <el-table
        :data="tableData"
        border
        style="width: 100%"
        v-loading="loading"
    >
      <el-table-column prop="id" label="预约ID" width="100"></el-table-column>
      <el-table-column prop="patientId" label="患者ID" width="120"></el-table-column>
      <el-table-column prop="doctorId" label="医生ID" width="120"></el-table-column>
      <el-table-column prop="scheduleId" label="排班ID" width="120"></el-table-column>
      <el-table-column prop="visitDate" label="就诊日期" width="150"></el-table-column>
      <el-table-column prop="timeSlot" label="时间段" width="120"></el-table-column>
      <el-table-column prop="queueNumber" label="排队号" width="100">
        <template #default="scope">
          {{ scope.row.queueNumber || '未分配' }}
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template #default="scope">
          <el-tag :type="scope.row.status === 0 ? 'success' : 'danger'">
            {{ scope.row.status === 0 ? '已预约' : '其他' }}
          </el-tag>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { addAppointment as addAppointmentApi } from '@/api/appointment'

const loading = ref(false)
const submitting = ref(false)
const latestAppointment = ref(null)

const form = reactive({
  doctorId: '',
  scheduleId: ''
})

const tableData = ref([])

// 当前测试的模拟用户ID
const currentPatientId = 7

// 提交预约
const addAppointment = async () => {
  if (!form.doctorId || !form.scheduleId) {
    ElMessage.warning('请填写完整的预约信息')
    return
  }

  submitting.value = true
  try {
    // 调用后端接口
    const appointment = await addAppointmentApi({
      doctorId: Number(form.doctorId),
      scheduleId: Number(form.scheduleId),
      patientId: currentPatientId
    })

    ElMessage.success('预约成功')

    // 保存最新的预约信息并展示
    latestAppointment.value = appointment

    // 添加到表格列表中
    tableData.value.unshift(appointment)

    // 清空表单
    form.doctorId = ''
    form.scheduleId = ''
  } catch (err) {
    console.error(err)
  } finally {
    submitting.value = false
  }
}

onMounted(() => {
  loading.value = false
})
</script>