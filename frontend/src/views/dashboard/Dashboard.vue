<template>
  <div class="dashboard-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>Dashboard</span>
          <el-button @click="handleLogout">退出登录</el-button>
        </div>
      </template>
      <p>欢迎，{{ authStore.userInfo.nickname || authStore.userInfo.username || '管理员' }}</p>
    </el-card>
  </div>
</template>

<script setup>
import { useAuthStore } from '../../stores/auth'
import { useRouter } from 'vue-router'

const authStore = useAuthStore()
const router = useRouter()

function handleLogout() {
  authStore.logout()
  router.push('/login')
}
</script>

<style scoped>
.dashboard-container {
  padding: 20px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
