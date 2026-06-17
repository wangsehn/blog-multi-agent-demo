<template>
  <div class="dashboard">
    <el-card>
      <h2>欢迎使用博客管理后台</h2>
      <p style="color:#909399;margin-top:12px;">你好，{{ authStore.userInfo?.username || '管理员' }}！</p>
    </el-card>
  </div>
</template>

<script setup>
import { useAuthStore } from '../stores/auth'
const authStore = useAuthStore()
</script>

<style scoped>
.dashboard { padding: 20px; }
</style>
