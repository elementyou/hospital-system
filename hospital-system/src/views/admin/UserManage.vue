<template>
  <div style="padding:20px;">
    <h2>用户管理</h2>
    <div class="toolbar" style="margin:16px 0;display:flex;gap:12px;">
      <el-button type="primary" @click="openAddDialog">新增用户</el-button>
      <el-input v-model="searchKey" placeholder="输入账号搜索" style="width:260px;" clearable></el-input>
      <el-button type="success" @click="loadUserList">搜索</el-button>
    </div>

    <el-table :data="tableData" border stripe style="width:100%;" v-loading="loading">
      <el-table-column prop="id" label="ID" width="80"/>
      <el-table-column prop="username" label="账号"/>
      <el-table-column prop="realName" label="姓名"/>
      <el-table-column prop="role" label="角色">
        <template #default="scope">
          <el-tag v-if="scope.row.role==='admin'" type="danger">管理员</el-tag>
          <el-tag v-if="scope.row.role==='doctor'" type="primary">医生</el-tag>
          <el-tag v-if="scope.row.role==='patient'" type="success">患者</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态">
        <template #default="scope">
          <el-tag v-if="scope.row.status===1" type="success">启用</el-tag>
          <el-tag v-else type="info">禁用</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间"/>
      <el-table-column label="操作" width="260">
        <template #default="scope">
          <el-button size="small" @click="openEditDialog(scope.row)">编辑</el-button>
          <el-button size="small" type="warning" @click="resetPwd(scope.row)">重置密码</el-button>
          <el-button size="small" :type="scope.row.status===1?'danger':'success'" @click="toggleStatus(scope.row)">
            {{ scope.row.status===1?'禁用':'启用' }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
        v-model:current-page="pageNum"
        v-model:page-size="pageSize"
        :total="total"
        layout="total, prev, pager, next"
        @change="loadUserList"
        style="margin-top:16px;"
    />

    <!-- 新增/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" title="用户信息" width="480px">
      <el-form :model="form" label-width="90px">
        <el-form-item label="账号" prop="username">
          <el-input v-model="form.username" :disabled="isEdit"></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="realName">
          <el-input v-model="form.realName"></el-input>
        </el-form-item>
        <el-form-item label="角色" prop="role">
          <el-select v-model="form.role" style="width:100%">
            <el-option label="管理员" value="admin"/>
            <el-option label="医生" value="doctor"/>
            <el-option label="患者" value="patient"/>
          </el-select>
        </el-form-item>
        <el-form-item label="密码" v-if="!isEdit">
          <el-input v-model="form.password" show-password></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="form.status" active-value="1" inactive-value="0"></el-switch>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible=false">取消</el-button>
        <el-button type="primary" @click="submitForm">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'

const loading = ref(false)
const tableData = ref([])
const searchKey = ref('')
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)

const dialogVisible = ref(false)
const isEdit = ref(false)
const form = ref({
  id: null,
  username: '',
  realName: '',
  role: 'patient',
  password: '',
  status: 1
})

// 加载列表
const loadUserList = async () => {
  loading.value = true
  try {
    // 这里替换成你的后端用户分页接口
    const res = await axios.get('/api/user/list', {
      params: { pageNum: pageNum.value, pageSize: pageSize.value, key: searchKey.value }
    })
    if(res.data.code === 200){
      tableData.value = res.data.data.records
      total.value = res.data.data.total
    }
  } catch (e) {
    ElMessage.error('加载用户列表失败')
  } finally {
    loading.value = false
  }
}

// 新增
const openAddDialog = () => {
  isEdit.value = false
  form.value = { id:null, username:'', realName:'', role:'patient', password:'', status:1 }
  dialogVisible.value = true
}

// 编辑
const openEditDialog = (row) => {
  isEdit.value = true
  form.value = {...row}
  dialogVisible.value = true
}

// 提交
const submitForm = async () => {
  if(!form.value.username || !form.value.realName || !form.value.role){
    ElMessage.warning('请填写必填项')
    return
  }
  try {
    let res
    if(isEdit.value){
      res = await axios.put('/api/user/update', form.value)
    }else{
      res = await axios.post('/api/user/add', form.value)
    }
    if(res.data.code === 200){
      ElMessage.success('保存成功')
      dialogVisible.value = false
      loadUserList()
    }
  } catch (e) {
    ElMessage.error('保存失败')
  }
}

// 重置密码
const resetPwd = async (row) => {
  ElMessageBox.confirm('确定重置该用户密码？默认密码：123456','提示').then(async ()=>{
    const res = await axios.post('/api/user/resetPwd', {id:row.id})
    if(res.data.code ===200){
      ElMessage.success('密码重置成功')
    }
  })
}

// 启用禁用
const toggleStatus = async (row) => {
  ElMessageBox.confirm('确定修改状态？','提示').then(async ()=>{
    const res = await axios.post('/api/user/status', {id:row.id, status: row.status===1?0:1})
    if(res.data.code ===200){
      ElMessage.success('状态修改成功')
      loadUserList()
    }
  })
}

onMounted(()=>{
  loadUserList()
})
</script>
