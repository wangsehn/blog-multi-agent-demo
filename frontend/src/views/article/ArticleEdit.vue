<template>
  <div class="article-edit">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>{{ isEdit ? '编辑文章' : '新建文章' }}</span>
          <div>
            <el-button @click="router.push('/articles')">返回列表</el-button>
          </div>
        </div>
      </template>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入文章标题" />
        </el-form-item>
        <el-form-item label="分类" prop="categoryId">
          <el-select v-model="form.categoryId" placeholder="请选择分类" style="width: 100%;">
            <el-option v-for="item in categories" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="标签" prop="tagIds">
          <el-select v-model="form.tagIds" multiple placeholder="请选择标签" style="width: 100%;">
            <el-option v-for="item in tags" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <MdEditor v-model="form.content" style="height: 500px;" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="saving" @click="handleSave(0)">保存草稿</el-button>
          <el-button type="success" :loading="saving" @click="handleSave(1)">发布</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { MdEditor } from 'md-editor-v3'
import 'md-editor-v3/lib/style.css'
import { getArticleById, createArticle, updateArticle } from '../../api/article'
import { getCategoryList } from '../../api/category'
import { getTagList } from '../../api/tag'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const formRef = ref(null)
const saving = ref(false)
const categories = ref([])
const tags = ref([])

const isEdit = computed(() => !!route.params.id)

const form = reactive({
  title: '',
  categoryId: null,
  tagIds: [],
  content: '',
  status: 0
})

const rules = {
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  categoryId: [{ required: true, message: '请选择分类', trigger: 'change' }],
  content: [{ required: true, message: '请输入内容', trigger: 'blur' }]
}

async function fetchCategories() {
  const res = await getCategoryList()
  categories.value = res.data?.records || res.data || []
}

async function fetchTags() {
  const res = await getTagList()
  tags.value = res.data?.records || res.data || []
}

async function fetchArticle() {
  if (!route.params.id) return
  const res = await getArticleById(route.params.id)
  const data = res.data || res
  form.title = data.title
  form.categoryId = data.categoryId
  form.tagIds = data.tagIds || []
  form.content = data.content
  form.status = data.status
}

async function handleSave(status) {
  await formRef.value.validate()
  saving.value = true
  try {
    const payload = { ...form, status }
    if (isEdit.value) {
      await updateArticle(route.params.id, payload)
    } else {
      await createArticle(payload)
    }
    ElMessage.success('保存成功')
    router.push('/articles')
  } finally {
    saving.value = false
  }
}

onMounted(() => {
  fetchCategories()
  fetchTags()
  fetchArticle()
})
</script>

<style scoped>
.article-edit {
  padding: 20px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
