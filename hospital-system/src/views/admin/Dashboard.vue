<template>
  <div style="padding:20px;">
    <div class="search-bar" style="display:flex;align-items:center;gap:12px;margin-bottom:20px;">
      <el-date-picker
          v-model="dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="YYYY-MM-DD"
      />
      <el-button type="primary" @click="loadData">查询统计</el-button>
    </div>

    <h2>后台数据看板</h2>
    <!-- 统计卡片 -->
    <el-row :gutter="20" style="margin-top:20px;margin-bottom:24px;">
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="card-title">预约总数</div>
          <div class="card-num">{{ statData.totalAppointments || 0 }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="card-title">已接诊</div>
          <div class="card-num">{{ statData.completedAppointments || 0 }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="card-title">取消率</div>
          <div class="card-num">{{ ((statData.cancelRate || 0) * 100).toFixed(2) }}%</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="card-title">药品总金额</div>
          <div class="card-num">{{ medicineTotalAmount }}</div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card shadow="hover">
          <h3 style="margin:0 0 12px 0;">科室预约数量</h3>
          <div ref="chartDep" style="width:100%;height:320px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <h3 style="margin:0 0 12px 0;">医生接诊工作量</h3>
          <div ref="chartDoc" style="width:100%;height:320px;"></div>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="20" style="margin-top:20px;">
      <el-col :span="12">
        <el-card shadow="hover">
          <h3 style="margin:0 0 12px 0;">药品消费占比</h3>
          <div ref="chartMed" style="width:100%;height:320px;"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick, watch } from 'vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'
import * as echarts from 'echarts'

const dateRange = ref([])
const statData = ref({})
const medicineTotalAmount = ref(0)

// 图表dom
const chartDep = ref(null)
const chartDoc = ref(null)
const chartMed = ref(null)
let depChart, docChart, medChart

// 渲染科室图
function renderDepChart() {
  const data = statData.value.departmentStats || []
  const names = data.map(item => item.departmentName)
  const vals = data.map(item => item.appointmentCount)
  depChart.setOption({
    xAxis: { type: 'category', data: names },
    yAxis: { type: 'value' },
    series: [{ type: 'bar', data: vals, color: '#409EFF' }]
  })
}

// 渲染医生工作量图
function renderDocChart() {
  const data = statData.value.doctorWorkload || []
  const names = data.map(item => item.doctorName)
  const vals = data.map(item => item.consultCount)
  docChart.setOption({
    xAxis: { type: 'category', data: names },
    yAxis: { type: 'value' },
    series: [{ type: 'bar', data: vals, color: '#67C23A' }]
  })
}

// 渲染药品饼图
function renderMedChart() {
  const data = statData.value.medicineConsumption || []
  const pieData = data.filter(i => i.consumeAmount).map(item => ({
    name: item.medicineName,
    value: item.consumeAmount
  }))
  medChart.setOption({
    series: [{ type: 'pie', radius: '60%', data: pieData }]
  })
}

const loadData = async () => {
  if (!dateRange.value || dateRange.value.length !== 2) {
    ElMessage.warning('请选择起止日期！')
    return
  }
  const [startDate, endDate] = dateRange.value
  const res = await axios.get('/api/statistics/dashboard', {
    params: { startDate, endDate }
  })
  console.log('后端返回：', res.data)
  if (res.data.code === 200) {
    statData.value = res.data.data
    // 计算药品总额
    let sum = 0
    statData.value.medicineConsumption?.forEach(item => {
      sum += Number(item.consumeAmount || 0)
    })
    medicineTotalAmount.value = sum.toFixed(2)
    // 等dom更新完再画图
    nextTick(() => {
      renderDepChart()
      renderDocChart()
      renderMedChart()
    })
  } else {
    ElMessage.error(res.data.message || '查询失败')
  }
}

onMounted(() => {
  // 初始化echarts实例
  depChart = echarts.init(chartDep.value)
  docChart = echarts.init(chartDoc.value)
  medChart = echarts.init(chartMed.value)

  const end = new Date()
  const start = new Date()
  start.setDate(start.getDate() - 7)
  const formatDate = (d) => {
    const y = d.getFullYear()
    const m = String(d.getMonth() + 1).padStart(2, '0')
    const day = String(d.getDate()).padStart(2, '0')
    return `${y}-${m}-${day}`
  }
  dateRange.value = [formatDate(start), formatDate(end)]
  loadData()

  // 窗口大小自适应
  window.addEventListener('resize', () => {
    depChart.resize()
    docChart.resize()
    medChart.resize()
  })
})
</script>

<style scoped>
.card-title {
  color: #666;
  font-size:14px;
}
.card-num {
  font-size: 32px;
  font-weight: bold;
  color: #409EFF;
  margin-top:8px;
}
</style>
