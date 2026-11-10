<template>
  <div class="schedule-manage">
    <el-card shadow="hover">
      <div class="card-header">
        <h3>医生排班管理</h3>
        <el-button type="primary" @click="openAdd">新增排班</el-button>
      </div>

      <el-form :model="searchForm" inline style="margin-bottom:16px">
        <el-form-item label="医生">
          <el-select v-model="searchForm.doctor_id" placeholder="请选择医生" clearable>
            <el-option v-for="item in doctorList" :key="item.id" :label="item.name" :value="item.id"/>
          </el-select>
        </el-form-item>
        <el-form-item label="日期">
          <el-date-picker v-model="searchForm.work_date" type="date" placeholder="选择日期" clearable/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadList">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" border stripe style="width:100%">
        <el-table-column prop="id" label="ID" width="60"/>
        <el-table-column prop="doctor_name" label="医生姓名"/>
        <el-table-column prop="work_date" label="出诊日期"/>
        <el-table-column prop="time_slot" label="时段"/>
        <el-table-column prop="total_count" label="总号源"/>
        <el-table-column prop="remain_count" label="剩余号源"/>
        <el-table-column label="状态">
          <template #default="scope">
            <el-tag :type="scope.row.status===1?'success':'info'">
              {{ scope.row.status===1?'正常':'停诊' }}
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

    <el-dialog v-model="dialogVisible" title="排班信息" width="500px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="医生">
          <el-select v-model="form.doctor_id" placeholder="选择医生">
            <el-option v-for="item in doctorList" :key="item.id" :label="item.name" :value="item.id"/>
          </el-select>
        </el-form-item>
        <el-form-item label="出诊日期">
          <el-date-picker v-model="form.work_date" type="date"/>
        </el-form-item>
        <el-form-item label="时段">
          <el-radio v-model="form.time_slot" label="上午">上午</el-radio>
          <el-radio v-model="form.time_slot" label="下午">下午</el-radio>
        </el-form-item>
        <el-form-item label="总号源">
          <el-input v-model="form.total_count"/>
        </el-form-item>
        <el-form-item label="剩余号源">
          <el-input v-model="form.remain_count"/>
        </el-form-item>
        <el-form-item label="状态">
          <el-radio v-model="form.status" :label="1">正常</el-radio>
          <el-radio v-model="form.status" :label="2">停诊</el-radio>
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

const searchForm = ref({doctor_id:null,work_date:''})
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const tableData = ref([])
const doctorList = ref([]) // 下拉医生列表
const dialogVisible = ref(false)
const form = ref({
  id:null,
  doctor_id:null,
  work_date:'',
  time_slot:'上午',
  total_count:30,
  remain_count:30,
  status:1
})

// 加载排班列表（后端要连表查，返回doctor_name）
const loadList = async ()=>{
  try{
    const data = await request.get('/schedule/page',{
      params:{pageNum:pageNum.value,pageSize:pageSize.value,...searchForm.value}
    })
    tableData.value = data.records
    total.value = data.total
  }catch(e){
    ElMessage.error('排班列表加载失败')
  }
}
// 加载全部医生，用于下拉框
const loadDoctorAll = async ()=>{
  try{
    const data = await request.get('/doctor/listAll')
    doctorList.value = data
  }catch(e){
    ElMessage.error('医生下拉加载失败')
  }
}
const resetSearch = ()=>{
  searchForm.value={doctor_id:null,work_date:''}
  pageNum.value=1
  loadList()
}
const openAdd = ()=>{
  form.value={id:null,doctor_id:null,work_date:'',time_slot:'上午',total_count:30,remain_count:30,status:1}
  dialogVisible.value=true
}
const openEdit = (row)=>{
  form.value={...row}
  dialogVisible.value=true
}
const submit = async ()=>{
  try{
    if(form.value.id){
      await request.put('/schedule/update',form.value)
    }else{
      await request.post('/schedule/add',form.value)
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
    await ElMessageBox.confirm('确定删除排班？','提示',{type:'warning'})
    await request.delete(`/schedule/${row.id}`)
    ElMessage.success('删除成功')
    loadList()
  }catch(e){
    if(e!=='cancel') ElMessage.error('删除失败')
  }
}
onMounted(()=>{
  loadDoctorAll()
  loadList()
})
</script>
<style scoped>
.card-header{display:flex;justify-content:space-between;align-items:center;margin-bottom:16px;}
</style>
