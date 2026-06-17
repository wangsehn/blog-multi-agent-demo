import axios from 'axios'
import { useAuthStore } from '../stores/auth'
import { ElMessage } from 'element-plus'

const request = axios.create({
  baseURL: '/api',
  timeout: 10000
})

request.interceptors.request.use(
  config => {
    const authStore = useAuthStore()
    if (authStore.token) {
      config.headers.Authorization = `Bearer ${authStore.token}`
    }
    return config
  },
  error => Promise.reject(error)
)

request.interceptors.response.use(
  response => response.data,
  error => {
    const message = error.response?.data?.message || '请求失败'
    ElMessage.error(message)
    if (error.response?.status === 401) {
      const authStore = useAuthStore()
      authStore.logout()
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

export default request
