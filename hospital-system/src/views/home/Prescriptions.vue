<template>
  <div style="padding: 20px; background-color: #fff; border-radius: 8px; min-height: 400px;">
    <h2 style="margin-bottom: 20px;">我的处方列表</h2>

    <el-empty v-if="tableData.length === 0" description="暂无处方记录"></el-empty>

    <!-- 处方列表 -->
    <div v-for="item in tableData" :key="item.id" style="margin-bottom: 15px;">
      <el-card shadow="hover">
        <template #header>
          <div style="display: flex; justify-content: space-between; align-items: center;">
            <span>
              <el-tag type="primary">{{ item.dept }}</el-tag>
              <span style="margin-left: 10px; font-weight: bold;">{{ item.doctorName }}</span>
            </span>
            <span style="color: #999; font-size: 14px;">开具时间：{{ item.date }}</span>
          </div>
        </template>

        <div style="padding: 10px 0;">
          <div style="margin-bottom: 8px;">
            <span style="font-weight: bold; color: #303133;">诊断结果：</span>
            {{ item.diagnosis }}
          </div>
          <div>
            <span style="font-weight: bold; color: #303133;">用药说明：</span>
            {{ item.prescription }}
          </div>
        </div>

        <template #footer>
          <el-button type="success" size="small">支付药费</el-button>
          <el-button size="small">查看详情</el-button>
        </template>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

// 1. 模拟的处方数据
const tableData = ref([
  {
    id: 1001,
    dept: '呼吸内科',
    doctorName: '张医生',
    date: '2026-10-25 14:30',
    diagnosis: '急性上呼吸道感染',
    prescription: '阿莫西林胶囊 1盒，每日3次，每次1粒；感冒灵颗粒 2盒，开水冲服。'
  },
  {
    id: 1002,
    dept: '皮肤科',
    doctorName: '王医生',
    date: '2026-10-28 09:15',
    diagnosis: '湿疹',
    prescription: '氯雷他定片 1盒，每日1次；炉甘石洗剂 1瓶，外用涂抹患处。'
  }
])
</script>