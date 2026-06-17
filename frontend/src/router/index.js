import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/',
    component: () => import('../views/Layout.vue'),
    meta: { requiresAuth: true },
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('../views/Dashboard.vue')
      },
      {
        path: 'articles',
        name: 'ArticleList',
        component: () => import('../views/ArticleList.vue')
      },
      {
        path: 'articles/edit/:id?',
        name: 'ArticleEdit',
        component: () => import('../views/ArticleEdit.vue')
      },
      {
        path: 'categories',
        name: 'CategoryManage',
        component: () => import('../views/CategoryManage.vue')
      },
      {
        path: 'tags',
        name: 'TagManage',
        component: () => import('../views/TagManage.vue')
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()
  if (to.meta.requiresAuth !== false && !authStore.token) {
    next('/login')
  } else {
    next()
  }
})

export default router
