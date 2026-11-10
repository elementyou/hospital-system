<template>
  <div class="doctor-manage">
    <el-card shadow="hover">
      <div class="card-header">
        <h3>医生管理</h3>
        <el-button type="primary" @click="openAdd">新增医生</el-button>
      </div>

      <el-form :model="searchForm" inline style="margin-bottom:16px">
        <el-form-item label="医生姓名">
          <el-input v-model="searchForm.name" placeholder="输入姓名" clearable/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadList">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" border stripe style="width:100%">
        <el-table-column prop="id" label="ID" width="60"/>
        <el-table-column prop="name" label="姓名"/>
        <el-table-column prop="department_id" label="科室ID"/>
        <el-table-column prop="title" label="职称"/>
        <el-table-column prop="phone" label="手机号"/>
        <el-table-column prop="introduction" label="简介"/>
        <el-table-column label="状态">
          <template #default="scope">
            <el-tag :type="scope.row.status===1?'success':'danger'">
              {{ scope.row.status===1?'启用':'禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160">
          <template #default="scope">
            <el-button size="small" @click="openEdit(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
          v-model:current-page="pageNum"
          v-model:page-size="pageSize"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @change="loadList"
          style="margin-top:16px;text-align:right"
      />
    </el-card>

    <el-dialog v-model="dialogVisible" title="医生信息" width="520px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="姓名">
          <el-input v-model="form.name"/>
        </el-form-item>
        <el-form-item label="科室ID">
          <el-input v-model="form.department_id"/>
        </el-form-item>
        <el-form-item label="职称">
          <el-input v-model="form.title"/>
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="form.phone"/>
        </el-form-item>
        <el-form-item label="简介">
          <el-input v-model="form.introduction" type="textarea"/>
        </el-form-item>
        <el-form-item label="状态">
          <el-radio v-model="form.status" :label="1">启用</el-radio>
          <el-radio v-model="form.status" :label="0">禁用</el-radio>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible=false">取消</el-button>
        <el-button type="primary" @click="submit">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {ref,onMounted} from 'vue'
import request from '@/utils/request'
import {ElMessage, ElMessageBox} from 'element-plus'

const searchForm = ref({name:''})
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const tableData = ref([])
const dialogVisible = ref(false)
const form = ref({
  id:null,
  name:'',
  department_id:'',
  title:'',
  phone:'',
  introduction:'',
  status:1
})

const loadList = async ()=>{
  try{
    const data = await request.get('/doctor/page',{
      params:{pageNum:pageNum.value,pageSize:pageSize.value,name:searchForm.value.name}
    })
    tableData.value = data.records
    total.value = data.total
  }catch(e){
    ElMessage.error('加载医生列表失败')
  }
}
const resetSearch = ()=>{
  searchForm.value.name=''
  pageNum.value=1
  loadList()
}
const openAdd = ()=>{
  form.value={id:null,name:'',department_id:'',title:'',phone:'',introduction:'',status:1}
  dialogVisible.value=true
}
const openEdit = (row)=>{
  form.value={...row}
  dialogVisible.value=true
}
const submit = async ()=>{
  try{
    if(form.value.id){
      await request.put('/doctor/update',form.value)
    }else{
      await request.post('/doctor/add',form.value)
    }
    ElMessage.success('操作成功')
    dialogVisible.value=false
    loadList()
  }catch(e){
    ElMessage.error('保存失败')
  }
}
const handleDelete = async (row)=>{
  try{
    await ElMessageBox.confirm('确定删除该医生？','提示',{type:'warning'})
    await request.delete(`/doctor/${row.id}`)
    ElMessage.success('删除成功')
    loadList()
  }catch(e){
    if(e!=='cancel') ElMessage.error('删除失败')
  }
}
onMounted(loadList)
</script>
<style scoped>
.card-header{display:flex;justify-content:space-between;align-items:center;margin-bottom:16px;}
</style>
