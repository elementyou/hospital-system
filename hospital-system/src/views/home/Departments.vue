<template>
  <div style="padding: 20px; background-color: #fff; border-radius: 8px;">
    <h2>科室列表</h2>

    <!-- 加载中状态 -->
    <div v-if="loading" v-loading="loading" style="min-height: 200px; display: flex; justify-content: center; align-items: center;">
      <span>正在加载科室数据...</span>
    </div>

    <!-- 无数据状态 -->
    <div v-else-if="!loading && deptList.length === 0" style="text-align: center; color: #999; padding: 40px;">
      暂无科室数据
    </div>

    <!-- 科室卡片网格 -->
    <div v-else style="display: flex; flex-wrap: wrap; gap: 20px; margin-top: 20px;">
      <el-card
          v-for="dept in deptList"
          :key="dept.id"
          style="width: 240px;"
          shadow="hover"
          class="dept-card"
      >
        <template #header>
          <div class="card-header">
            <span style="font-weight: bold; font-size: 16px;">{{ dept.name }}</span>
          </div>
        </template>
        <div style="color: #666; font-size: 13px; line-height: 1.6;">
          <p>简介：{{ dept.description || '暂无简介' }}</p>
        </div>
        <div style="margin-top: 15px; text-align: right;">
          <el-button
              type="primary"
              size="small"
              round
              @click="goToAppointments(dept.id, dept.name)"
          >
            去挂号
          </el-button>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
// 👇 1. 确保引入的是刚才写好的 getDepartmentList 方法
import { getDepartmentList } from '@/api/appointment'

const router = useRouter()
const deptList = ref([])
const loading = ref(true)

onMounted(async () => {
  try {
    // 👇 2. 调用后端接口
    const res = await getDepartmentList()

    // 👇 3. 根据你后端返回的格式赋值
    // (通常后端返回格式是 { code: 200, data: [...], msg: "成功" })
    deptList.value = res.data || res || []
  } catch (error) {
    console.error('获取科室列表失败:', error)
  } finally {
    loading.value = false
  }
})

const goToAppointments = (deptId, deptName) => {
  router.push({
    path: '/home/appointments',
    query: { deptId, deptName }
  })
}
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.dept-card:hover {
  border-color: #409EFF;
}
</style>