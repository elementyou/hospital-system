<template>
  <div class="queue-list-container">
    <h2>今日候诊队列</h2>

    <!-- 日期选择 + 状态筛选 -->
    <div class="toolbar">
      <el-date-picker
          v-model="visitDate"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="选择就诊日期"
          style="width: 180px; margin-right: 12px;"
          @change="fetchQueue"
      />
      <el-tabs v-model="activeTab" @tab-change="fetchQueue" style="flex: 1;">
        <el-tab-pane label="全部" name="all" />
        <el-tab-pane label="待接诊" name="0" />
        <el-tab-pane label="已接诊" name="1" />
      </el-tabs>
    </div>

    <!-- 队列表格 -->
    <el-table :data="queueList" style="width: 100%" v-loading="loading">
      <el-table-column prop="id" label="预约号" width="80" />
      <el-table-column prop="patientName" label="患者姓名" width="100" />
      <el-table-column prop="age" label="年龄" width="80" />
      <el-table-column prop="gender" label="性别" width="80">
        <template #default="scope">
          {{ scope.row.gender === 1 ? '男' : (scope.row.gender === 2 ? '女' : '-') }}
        </template>
      </el-table-column>
      <el-table-column prop="phone" label="手机号" width="150" />
      <el-table-column prop="timeSlot" label="时段" width="120" />
      <el-table-column prop="allergyInfo" label="过敏信息" />
      <el-table-column prop="statusText" label="当前状态" width="120">
        <template #default="scope">
          <el-tag :type="scope.row.status === 1 ? 'success' : 'warning'">
            {{ scope.row.statusText || '待接诊' }}
          </el-tag>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getQueueList } from '@/api/doctor'

const loading = ref(false)
const queueList = ref([])
const activeTab = ref('all')
// 默认当天（本地时区，避免 toISOString 的 UTC 偏移）
const visitDate = ref(new Date().toLocaleDateString('sv-SE'))

const fetchQueue = async () => {
  if (!visitDate.value) {
    ElMessage.warning('请先选择就诊日期')
    return
  }
  loading.value = true
  const doctorId = 1 // TODO: 换成当前登录医生的真实 id

  try {
    const res = await getQueueList({ doctorId, visitDate: visitDate.value })
    const list = Array.isArray(res) ? res : (res.data || res.list || res.rows || [])

    queueList.value = activeTab.value === 'all'
        ? list
        : list.filter(item => item.status === Number(activeTab.value))

    if (queueList.value.length === 0) {
      ElMessage.info(`${visitDate.value} 没有匹配的候诊记录`)
    }
  } catch (error) {
    console.error('获取候诊列表失败:', error)
    ElMessage.error('获取候诊列表失败')
    queueList.value = []
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchQueue()
})
</script>

<style scoped>
.queue-list-container {
  background-color: #fff;
  padding: 20px;
  border-radius: 4px;
}
.toolbar {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
}
:deep(.el-tabs__header) {
  margin: 0;
}
</style>
