<template>
  <div class="medicine-manage">
    <el-card shadow="hover">
      <div class="card-header">
        <h3>药品管理</h3>
        <el-button type="primary" @click="openAdd">新增药品</el-button>
      </div>

      <!-- 搜索栏 -->
      <el-form :model="searchForm" inline style="margin-bottom:16px">
        <el-form-item label="药品名称">
          <el-input v-model="searchForm.name" placeholder="请输入药品名称" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadList">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 表格 -->
      <el-table :data="tableData" border stripe style="width:100%">
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="name" label="药品名称" />
        <el-table-column prop="specification" label="规格" />
        <el-table-column prop="unit" label="单位" />
        <el-table-column prop="unitPrice" label="单价" />
        <el-table-column prop="stock" label="库存" />
        <el-table-column prop="warningStock" label="预警库存" />
        <el-table-column label="操作" width="160">
          <template #default="scope">
            <el-button size="small" @click="openEdit(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
          v-model:current-page="pageNum"
          v-model:page-size="pageSize"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @change="loadList"
          style="margin-top:16px; text-align:right"
      />
    </el-card>

    <!-- 新增/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" title="药品信息" width="500px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="药品名称">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="规格">
          <el-input v-model="form.specification" />
        </el-form-item>
        <el-form-item label="单位">
          <el-input v-model="form.unit" />
        </el-form-item>
        <el-form-item label="单价">
          <el-input v-model="form.unitPrice" />
        </el-form-item>
        <el-form-item label="库存">
          <el-input v-model="form.stock" />
        </el-form-item>
        <el-form-item label="预警库存">
          <el-input v-model="form.warningStock" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '@/utils/request'
import { ElMessage, ElMessageBox } from 'element-plus'

// 搜索
const searchForm = ref({
  name: ''
})
// 分页
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const tableData = ref([])

// 弹窗
const dialogVisible = ref(false)
const form = ref({
  id: null,
  name: '',
  specification: '',
  unit: '',
  unitPrice: '',
  stock: '',
  warningStock: ''
})

// 加载列表
const loadList = async () => {
  try {
    // baseURL已经是/api，这里直接写 /medicine/page
    const data = await request.get('/medicine/page', {
      params: {
        pageNum: pageNum.value,
        pageSize: pageSize.value,
        name: searchForm.value.name
      }
    })
    console.log('返回data：', data)
    tableData.value = data.records
    total.value = data.total
  } catch (err) {
    console.error('请求出错：', err)
    ElMessage.error('接口请求异常，请检查后端')
  }
}

// 重置搜索
const resetSearch = () => {
  searchForm.value.name = ''
  pageNum.value = 1
  loadList()
}

// 新增
const openAdd = () => {
  form.value = { id: null, name: '', specification: '', unit: '', unitPrice: '', stock: '', warningStock: '' }
  dialogVisible.value = true
}

// 编辑
const openEdit = (row) => {
  form.value = { ...row }
  dialogVisible.value = true
}

// 提交保存
const submitForm = async () => {
  try {
    if (form.value.id) {
      await request.put('/medicine/update', form.value)
    } else {
      await request.post('/medicine/add', form.value)
    }
    ElMessage.success('操作成功')
    dialogVisible.value = false
    loadList()
  } catch (err) {
    console.error(err)
    ElMessage.error('保存失败')
  }
}

// 删除
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除这条药品吗？', '提示', { type: 'warning' })
    await request.delete(`/medicine/${row.id}`)
    ElMessage.success('删除成功')
    loadList()
  } catch (err) {
    if (err !== 'cancel') {
      console.error(err)
      ElMessage.error('删除失败')
    }
  }
}

onMounted(() => loadList())
</script>


<style scoped>
.medicine-manage {
  padding: 16px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}
</style>
