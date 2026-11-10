<template>
  <el-container style="height: 100vh">
    <!-- 左侧菜单栏 -->
    <el-aside width="220px" style="background-color: #304156; color: white;">
      <div class="logo-container">
        <h3 style="margin: 20px 0 10px; text-align: center; color: white;">医院门诊系统</h3>
      </div>

      <el-menu
          :default-active="$route.path"
          background-color="#304156"
          text-color="#bfcbd9"
          active-text-color="#409EFF"
          router
      >
        <!-- 管理员菜单 -->
        <template v-if="role === 'admin'">


          <el-menu-item index="/admin/user">用户管理</el-menu-item>


          <el-menu-item index="/admin/deptManage">科室管理</el-menu-item>


          <el-menu-item index="/admin/medicineManage">药品管理</el-menu-item>

          <el-sub-menu index="doctor">
            <template #title>医生排班管理</template>
            <el-menu-item index="/admin/doctorManage">医生管理</el-menu-item>
            <el-menu-item index="/admin/scheduleManage">排班管理</el-menu-item>
          </el-sub-menu>


          <el-menu-item index="/admin/dashboard">数据看板</el-menu-item>


        </template>

        <!-- 患者菜单 -->
        <template v-if="role === 'patient'">
          <el-menu-item index="/home/appointments">预约挂号</el-menu-item>

          <!-- 👇 在这里加上我的预约记录 -->
          <el-menu-item index="/home/my-appointments">我的预约记录</el-menu-item>

          <el-menu-item index="/home/departments">科室列表</el-menu-item>
          <el-menu-item index="/home/prescriptions">我的处方</el-menu-item>
        </template>

        <!-- 医生菜单 -->
        <template v-if="role === 'doctor'">
          <el-menu-item index="/doctor/workstation">接诊工作台</el-menu-item>
          <el-menu-item index="/doctor/queue-list">查看候诊患者详情</el-menu-item>
        </template>
      </el-menu>
    </el-aside>

    <!-- 右侧内容区 -->
    <el-container>
      <!-- 顶部栏 -->
      <el-header style="background-color: #fff; border-bottom: 1px solid #e4e7ed; display: flex; align-items: center; justify-content: flex-end; padding: 0 20px;">
        <div>欢迎您, {{ realName }}</div>
        <el-button type="danger" text style="margin-left: 10px;" @click="handleLogout">退出登录</el-button>
      </el-header>

      <!-- 核心：业务页面的渲染出口 -->
      <el-main style="background-color: #f0f2f5;">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
  import { ref, onMounted } from 'vue'
  import { useRouter } from 'vue-router'

  const router = useRouter()
  const role = ref('')
  const realName = ref('')

  // 页面加载时读取本地存储的登录信息
  onMounted(() => {
  role.value = localStorage.getItem('role')
  realName.value = localStorage.getItem('realName')
})

  // 退出登录
  const handleLogout = () => {
  localStorage.clear()
  router.push('/login')
}
</script>


<style scoped>
.logo-container {
  border-bottom: 1px solid #3a4a5e;
}
</style>