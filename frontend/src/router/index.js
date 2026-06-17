import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', name: 'Login', component: () => import('../views/login/Login.vue') },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: () => import('../views/dashboard/Dashboard.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/articles',
    name: 'ArticleList',
    component: () => import('../views/article/ArticleList.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/articles/create',
    name: 'ArticleCreate',
    component: () => import('../views/article/ArticleEdit.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/articles/:id/edit',
    name: 'ArticleEdit',
    component: () => import('../views/article/ArticleEdit.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/categories',
    name: 'CategoryManage',
    component: () => import('../views/category/CategoryManage.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/tags',
    name: 'TagManage',
    component: () => import('../views/tag/TagManage.vue'),
    meta: { requiresAuth: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()
  if (to.meta.requiresAuth && !authStore.token) {
    next('/login')
  } else {
    next()
  }
})

export default router
