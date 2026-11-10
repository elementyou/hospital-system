<template>
  <div style="padding: 20px; background-color: #fff; border-radius: 8px;">
    <h2>科室与医生排班</h2>

    <el-row :gutter="20">
      <!-- 左侧：科室列表 -->
      <el-col :span="8">
        <el-card shadow="never" style="min-height: 500px;">
          <template #header><span style="font-weight: bold;">选择科室</span></template>
          <div v-loading="state.deptLoading" style="min-height: 400px; max-height: 600px; overflow-y: auto;">
            <div v-if="!state.deptLoading && state.deptList.length === 0" style="color: #999; text-align: center; margin-top: 50px;">
              暂无科室数据
            </div>
            <div
                v-for="dept in state.deptList"
                :key="dept.id"
                :class="{ 'active-card': dept.id === state.activeDeptId }"
                class="dept-item"
                @click="handleDeptClick(dept)"
            >
              <div style="font-size: 16px; font-weight: bold; color: #303133;">
                {{ dept.name || dept.deptName || dept.dept_name || '未知科室' }}
              </div>
              <div style="margin-top: 8px; font-size: 13px; color: #666; line-height: 1.4;">
                {{ dept.description || dept.intro || '暂无简介' }}
              </div>
            </div>
          </div>
        </el-card>
      </el-col>

      <!-- 右侧：医生列表 -->
      <el-col :span="16">
        <el-card v-if="state.currentDeptName" shadow="never" style="min-height: 500px;">
          <template #header>
            <div style="display: flex; justify-content: space-between; align-items: center;">
              <span style="font-size: 18px; font-weight: bold;">{{ state.currentDeptName }} - 专家排班</span>
            </div>
          </template>

          <div v-loading="state.doctorLoading">
            <el-empty v-if="!state.doctorLoading && state.doctorList.length === 0" description="该科室暂无医生" />

            <div v-else>
              <el-card
                  v-for="doc in state.doctorList"
                  :key="doc.id"
                  shadow="hover"
                  style="margin-bottom: 20px; cursor: pointer;"
                  @click="handleDoctorExpand(doc)"
              >
                <div style="display: flex; justify-content: space-between; align-items: center;">

                  <!-- 左侧：医生信息 -->
                  <div style="flex: 1; display: flex; align-items: center;">
                    <el-avatar :size="48" style="background-color: #409EFF; margin-right: 15px;">
                      {{ (doc.name || '医').substring(0, 1) }}
                    </el-avatar>

                    <div>
                      <!-- 姓名与职称 -->
                      <div style="display: flex; align-items: center; margin-bottom: 8px;">
                        <div style="font-size: 16px; font-weight: bold;">{{ doc.name }}</div>
                        <el-tag size="small" style="margin-left: 10px;">{{ doc.title || '医师' }}</el-tag>
                      </div>
                      <!-- 简介 -->
                      <div style="color: #666; font-size: 14px; line-height: 1.6;">
                        简介：{{ doc.introduction || '暂无' }}
                      </div>
                    </div>
                  </div>

                  <!-- 右侧：去挂号按钮（点击不触发展开） -->
                  <div>
                    <el-button
                        type="primary"
                        round
                        size="small"
                        @click.stop="handleBooking(doc)"
                    >
                      去挂号
                    </el-button>
                  </div>

                </div>

                <!-- 👇 新增：展开显示的排班详情区域 -->
                <div v-if="state.expandedDoctorId === doc.id" style="margin-top: 20px; padding-top: 15px; border-top: 1px dashed #ebeef5;">
                  <div v-loading="doc.loading">
                    <el-empty v-if="!doc.loading && (!doc.scheduleList || doc.scheduleList.length === 0)" size="small" description="暂无排班数据" />

                    <div v-else style="display: flex; gap: 15px; flex-wrap: wrap;">
                      <el-card
                          v-for="schedule in doc.scheduleList"
                          :key="schedule.id"
                          shadow="hover"
                          style="width: 160px; font-size: 13px; border-color: #f0f0f0;"
                          @click.stop="handleBooking(doc, schedule)"
                      >
                        <div style="margin-bottom: 8px; font-weight: bold; color: #303133;">{{ schedule.workDate }}</div>
                        <div style="color: #E6A23C; margin-bottom: 5px; display: flex; align-items: center;">
                          <span>时段：{{ schedule.timeSlot }}</span>
                        </div>
                        <div style="color: #67C23A; margin-bottom: 10px;">
                          余号：{{ schedule.remainCount }} / {{ schedule.totalCount }}
                        </div>
                        <el-button
                            type="primary"
                            size="small"
                            :disabled="schedule.remainCount <= 0"
                            style="width: 100%;"
                        >
                          {{ schedule.remainCount > 0 ? '立即预约' : '号源已满' }}
                        </el-button>
                      </el-card>
                    </div>
                  </div>
                </div>
              </el-card>
            </div>
          </div>
        </el-card>
        <el-empty v-else description="请从左侧选择一个科室查看医生排班" style="margin-top: 100px;" />
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import { useRouter } from 'vue-router'
// 👇 引入新接口 getScheduleList
import { getDepartmentList, getDoctorList, getScheduleList } from '@/api/appointment'

const router = useRouter()

const state = reactive({
  deptList: [],
  deptLoading: true,
  activeDeptId: null,
  currentDeptName: '',
  doctorList: [],
  doctorLoading: false,
  expandedDoctorId: null // 👈 记录当前展开的医生ID
})

// 页面加载时获取科室列表
const fetchDepartments = async () => {
  try {
    const res = await getDepartmentList()
    let list = []
    if (Array.isArray(res)) { list = res }
    else if (Array.isArray(res.data)) { list = res.data }
    else if (res.rows) { list = res.rows }

    state.deptList = [...list]
  } catch (error) {
    console.error('获取科室失败:', error)
  } finally {
    state.deptLoading = false
  }
}

fetchDepartments()

// 点击科室卡片，获取医生
const handleDeptClick = async (dept) => {
  state.activeDeptId = dept.id
  state.currentDeptName = dept.name || dept.deptName || dept.dept_name
  state.doctorLoading = true
  state.doctorList = []
  state.expandedDoctorId = null // 切换科室时收起排班

  try {
    const res = await getDoctorList({ deptId: dept.id })
    let list = Array.isArray(res) ? res : (res.data || res.rows || [])
    state.doctorList = [...list]
  } catch (error) {
    console.error('获取医生失败:', error)
  } finally {
    state.doctorLoading = false
  }
}

// 👇 新增：点击医生卡片，展开/收起排班
// 修改这个方法
const handleDoctorExpand = async (doctor) => {
  // 如果点击的是同一个医生，则收起
  if (state.expandedDoctorId === doctor.id) {
    state.expandedDoctorId = null;
    return;
  }

  state.expandedDoctorId = doctor.id;
  doctor.loading = true;
  doctor.scheduleList = [];

  try {
    const today = new Date().toISOString().split('T')[0]

    const res = await getScheduleList({
      doctorId: doctor.id,
      workDate: today

    });

    // 兼容处理返回数据
    doctor.scheduleList = res.data || res || [];
  } catch (error) {
    console.error('获取排班失败:', error);
  } finally {
    doctor.loading = false;
  }
}

// 👇 修改：点击挂号按钮
const handleBooking = (doctor, schedule = null) => {
  const query = {
    doctorId: doctor.id,
    deptName: state.currentDeptName,
    doctorName: doctor.name
  }

  // 如果指定了具体排班时段，带上排班参数
  if (schedule) {
    query.scheduleId = schedule.id
    query.workDate = schedule.workDate
    query.timeSlot = schedule.timeSlot
  }

  router.push({
    path: '/home/appointments',
    query: query
  })
}
</script>

<style scoped>
.dept-item {
  padding: 15px;
  border-bottom: 1px solid #ebeef5;
  cursor: pointer;
  transition: all 0.3s;
}
.dept-item:hover {
  background-color: #f5f7fa;
}
.active-card {
  background-color: #ecf5ff;
  border-left: 4px solid #409EFF;
}
.dept-item:last-child {
  border-bottom: none;
}
</style>