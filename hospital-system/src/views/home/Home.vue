<template>
  <div style="padding: 20px;">
    <h2>欢迎回来，患者 {{ realName }}！</h2>
    <p>当前是患者端首页，请点击左侧菜单进行挂号或查看处方。</p>
  </div>
</template>
<script setup>
import { ref } from 'vue'
const realName = ref(localStorage.getItem('realName') || '张三')
</script>