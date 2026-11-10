<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()
const loading = ref(false)

const form = reactive({
  username: '',
  password: ''
})

async function handleLogin() {
  if (!form.username || !form.password) {
    ElMessage.warning('请输入账号和密码')
    return
  }

  loading.value = true

  setTimeout(() => {
    loading.value = false

    // ========== 模拟后端返回，根据账号判断角色（测试用）==========
    let res = {}
    if(form.username === 'admin'){
      res = { role: 'admin', realName: '系统管理员' }
    }else if(form.username === 'doctor1'){
      res = { role: 'doctor', realName: '李医生' }
    }else if(form.username === 'patient1'){
      res = { role: 'patient', realName: '张三' }
    }else{
      ElMessage.error('账号密码错误')
      return
    }

    // 存入localStorage！！和侧边栏页面配合
    localStorage.setItem('role', res.role)
    localStorage.setItem('realName', res.realName)

    ElMessage.success('登录成功，即将跳转...')
    // 改成按角色跳转，推荐用这个
    if(res.role === 'admin'){
      router.push('/admin/dashboard')
    }else if(res.role === 'doctor'){
      router.push('/doctor/workstation')
    }else if(res.role === 'patient'){
      router.push('/home/appointments')
    }
  }, 1500)
}
</script>
<template>
  <div class="login-page">
    <el-card class="login-card">
      <div class="login-header">
        <!-- 使用 assets 下的图片作为 Logo -->
        <img src="@/assets/hero.png" alt="医院Logo" class="logo" />
        <h2>医院门诊系统</h2>
      </div>

      <el-form :model="form" label-width="0">
        <el-form-item>
          <el-input
              v-model="form.username"
              placeholder="请输入账号"
              prefix-icon="User"
              size="large"
          />

        </el-form-item>

        <el-form-item>
          <el-input
              v-model="form.password"
              type="password"
              placeholder="请输入密码"
              prefix-icon="Lock"
              show-password
              size="large"
          />
        </el-form-item>

        <el-form-item>
          <el-button
              type="primary"
              size="large"
              style="width: 100%"
              :loading="loading"
              @click="handleLogin"
          >
            登 录
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>


<style scoped>
.login-page {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f0f2f5;
}

.login-card {
  width: 420px;
  padding: 20px;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.login-header {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 30px;
}

.logo {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  object-fit: cover;
  margin-bottom: 15px;
}

.login-header h2 {
  font-size: 22px;
  color: #303133;
  margin: 0;
}
</style>