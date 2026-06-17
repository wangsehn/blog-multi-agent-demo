<template>
  <div class="article-list">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>文章管理</span>
          <el-button type="primary" @click="$router.push('/articles/edit')">新建文章</el-button>
        </div>
      </template>
      <div class="search-bar">
        <el-input v-model="searchTitle" placeholder="搜索文章标题" clearable style="width:240px" @clear="loadArticles" @keyup.enter="loadArticles" />
        <el-select v-model="searchStatus" placeholder="状态" clearable style="width:120px;margin-left:12px" @change="loadArticles">
          <el-option label="草稿" :value="0" />
          <el-option label="已发布" :value="1" />
        </el-select>
        <el-button style="margin-left:12px" @click="loadArticles">搜索</el-button>
      </div>
      <el-table :data="articles" v-loading="loading" style="width:100%;margin-top:16px;">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="title" label="标题" min-width="200" />
        <el-table-column prop="authorName" label="作者" width="120" />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">{{ row.status === 1 ? '已发布' : '草稿' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="$router.push(`/articles/edit/${row.id}`)">编辑</el-button>
            <el-popconfirm title="确定删除?" @confirm="handleDelete(row.id)">
              <template #reference>
                <el-button link type="danger">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        style="margin-top:16px;justify-content:flex-end;"
        :current-page="page"
        :page-size="pageSize"
        :total="total"
        layout="total, prev, pager, next"
        @current-change="(p) => { page = p; loadArticles() }"
      />
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getArticles, deleteArticle } from '../api/article'
import { ElMessage } from 'element-plus'

const articles = ref([])
const loading = ref(false)
const page = ref(1)
const pageSize = ref(10)
const total = ref(0)
const searchTitle = ref('')
const searchStatus = ref('')

async function loadArticles() {
  loading.value = true
  try {
    const params = { page: page.value, pageSize: pageSize.value }
    if (searchTitle.value) params.title = searchTitle.value
    if (searchStatus.value !== '') params.status = searchStatus.value
    const res = await getArticles(params)
    articles.value = res.data?.records || res.data || []
    total.value = res.data?.total || 0
  } catch (e) {
    ElMessage.error('加载文章失败')
  } finally {
    loading.value = false
  }
}

async function handleDelete(id) {
  try {
    await deleteArticle(id)
    ElMessage.success('删除成功')
    loadArticles()
  } catch (e) {
    ElMessage.error('删除失败')
  }
}

onMounted(loadArticles)
</script>

<style scoped>
.article-list { padding: 20px; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
.search-bar { display: flex; align-items: center; }
</style>
