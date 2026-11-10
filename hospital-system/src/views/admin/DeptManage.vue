<template>
  <div style="padding:20px">
    <h2>科室管理</h2>

    <!-- 搜索栏 -->
    <el-form :inline="true" class="search-form">
      <el-form-item label="科室名称">
        <el-input v-model="searchName" placeholder="输入科室名称搜索" clearable />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="loadData">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
        <el-button type="success" @click="openAddDialog">新增科室</el-button>
      </el-form-item>
    </el-form>

    <!-- 表格 -->
    <el-table :data="tableData" border stripe v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="科室名称" />
      <el-table-column prop="code" label="科室编码" />
      <el-table-column prop="description" label="科室说明" />
      <el-table-column prop="status" label="状态">
        <template #default="scope">
          <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
            {{ scope.row.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180">
        <template #default="scope">
          <el-button size="small" type="primary" @click="openEditDialog(scope.row)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
        v-model:current-page="pageNum"
        v-model:page-size="pageSize"
        :total="total"
        :page-sizes="[5,10,20]"
        layout="total, sizes, prev, pager, next, jumper"
        @change="loadData"
        style="margin-top:15px;justify-content:flex-end"
    />

    <!-- 新增/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑科室' : '新增科室'" width="500px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="科室名称">
          <el-input v-model="form.name" placeholder="请输入科室名称" />
        </el-form-item>
        <el-form-item label="科室编码">
          <el-input v-model="form.code" placeholder="例如 DEPT001" />
        </el-form-item>
        <el-form-item label="科室说明">
          <el-input v-model="form.description" type="textarea" rows="3" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'

// 搜索
const searchName = ref('')
// 分页
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const loading = ref(false)
const tableData = ref([])

// 弹窗
const dialogVisible = ref(false)
const isEdit = ref(false)
const form = reactive({
  id: null,
  name: '',
  code: '',
  description: '',
  status: 1
})

// 加载列表
const loadData = async () => {
  loading.value = true
  try {
    const res = await request.get('/department/page', {
      params: {
        pageNum: pageNum.value,
        pageSize: pageSize.value,
        name: searchName.value
      }
    })
    tableData.value = res.records
    total.value = res.total
  } catch (err) {
    ElMessage.error('加载科室列表失败')
  } finally {
    loading.value = false
  }
}

// 重置搜索
const resetSearch = () => {
  searchName.value = ''
  pageNum.value = 1
  loadData()
}

// 打开新增
const openAddDialog = () => {
  isEdit.value = false
  form.id = null
  form.name = ''
  form.code = ''
  form.description = ''
  form.status = 1
  dialogVisible.value = true
}

// 打开编辑
const openEditDialog = (row) => {
  isEdit.value = true
  form.id = row.id
  form.name = row.name
  form.code = row.code
  form.description = row.description
  form.status = row.status
  dialogVisible.value = true
}

// 提交
const submitForm = async () => {
  if (!form.name || !form.code) {
    return ElMessage.warning('科室名称、编码不能为空')
  }
  try {
    if (isEdit.value) {
      await request.put('/department', form)
      ElMessage.success('修改成功')
    } else {
      await request.post('/department', form)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    loadData()
  } catch (err) {
    ElMessage.error('保存失败')
  }
}

// 删除
const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该科室吗？', '提示', {
    type: 'warning'
  }).then(async () => {
    await request.delete(`/department/${row.id}`)
    ElMessage.success('删除成功')
    loadData()
  }).catch(() => {})
}

// 页面初始化加载
loadData()
</script>

<style scoped>
.search-form {
  margin-bottom:15px;
}
</style>
