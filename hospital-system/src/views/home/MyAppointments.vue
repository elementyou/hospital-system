<template>
  <div class="app-container">
    <!-- 列表展示区域 -->
    <div v-if="loading" class="loading-container">正在加载...</div>

    <div v-else-if="list.length === 0" class="empty-container">暂无预约记录</div>

    <div v-else class="list-container">
      <div class="appointment-card" v-for="item in list" :key="item.id">
        <div class="card-header">
          <span class="doctor-name">{{ item.doctorName }}</span>
          <span class="status-tag" :class="'status-' + item.status">
            {{ getStatusText(item.status) }}
          </span>
        </div>
        <div class="card-body">
          <p><span class="label">预约日期：</span>{{ item.visitDate }} ({{ item.timeSlot }})</p>
          <p><span class="label">预约时间：</span>{{ item.createdAt?.split('T')[0] }}</p>
        </div>
      </div>
    </div>

    <!-- 分页控件 -->
    <el-pagination
        v-if="total > 0"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="queryParams.pageNum"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="queryParams.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        style="margin-top: 20px; display: flex; justify-content: flex-end;"
    >
    </el-pagination>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getMyAppointmentList } from '@/api/appointment'

// 查询参数（暂时写死 patientId 为 7 进行测试）
const queryParams = reactive({
  patientId: 7,
  pageNum: 1,
  pageSize: 10
})

const list = ref([])
const total = ref(0)
const loading = ref(false)

// 状态翻译
const getStatusText = (status) => {
  switch (status) {
    case 0: return '待就诊'
    case 1: return '已完成'
    case 2: return '已取消'
    default: return '未知'
  }
}

// 获取列表数据
const getList = async () => {
  loading.value = true
  try {
    const res = await getMyAppointmentList(queryParams)
    console.log('接口返回:', res) // 先打印看看结构
    list.value = res?.records || []
    total.value = res?.total || 0
  } catch (error) {
    console.error('获取预约记录失败:', error)
  } finally {
    loading.value = false
  }
}

// 分页事件处理
const handleSizeChange = (val) => {
  queryParams.pageSize = val
  getList()
}
const handleCurrentChange = (val) => {
  queryParams.pageNum = val
  getList()
}

// 页面加载时获取数据
onMounted(() => {
  getList()
})
</script>

<style scoped>
.app-container {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
}
.page-header h2 {
  margin-bottom: 20px;
  color: #333;
}
.loading-container, .empty-container {
  text-align: center;
  padding: 40px 0;
  color: #909399;
}
.appointment-card {
  background-color: #fff;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  padding: 15px;
  margin-bottom: 15px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.05);
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
  border-bottom: 1px solid #eee;
  padding-bottom: 10px;
}
.doctor-name {
  font-size: 16px;
  font-weight: bold;
  color: #303133;
}
.status-tag {
  padding: 2px 8px;
  border-radius: 10px;
  font-size: 12px;
  color: #fff;
}
.status-0 { background-color: #409eff; } /* 蓝色：待就诊 */
.status-1 { background-color: #67c23a; } /* 绿色：已完成 */
.status-2 { background-color: #f56c6c; } /* 红色：已取消 */

.card-body p {
  margin: 5px 0;
  font-size: 14px;
  color: #606266;
}
.label {
  color: #909399;
  margin-right: 5px;
}
</style>