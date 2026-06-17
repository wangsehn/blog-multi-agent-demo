<template>
  <div class="tag-manage">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>标签管理</span>
          <el-button type="primary" @click="openDialog()">新增标签</el-button>
        </div>
      </template>
      <el-table :data="tagList" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="标签名称" min-width="150" />
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="openDialog(row)">编辑</el-button>
            <el-popconfirm title="确定删除？" @confirm="handleDelete(row.id)">
              <template #reference>
                <el-button size="small" type="danger">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="editingId ? '编辑标签' : '新增标签'" width="500px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入标签名称" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="handleSave">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getTagList, createTag, updateTag, deleteTag } from '../../api/tag'
import { ElMessage } from 'element-plus'

const tagList = ref([])
const loading = ref(false)
const saving = ref(false)
const dialogVisible = ref(false)
const editingId = ref(null)
const formRef = ref(null)

const form = reactive({
  name: ''
})

const rules = {
  name: [{ required: true, message: '请输入标签名称', trigger: 'blur' }]
}

async function fetchTags() {
  loading.value = true
  try {
    const res = await getTagList()
    tagList.value = res.data?.records || res.data || []
  } finally {
    loading.value = false
  }
}

function openDialog(row) {
  if (row) {
    editingId.value = row.id
    form.name = row.name
  } else {
    editingId.value = null
    form.name = ''
  }
  dialogVisible.value = true
}

async function handleSave() {
  await formRef.value.validate()
  saving.value = true
  try {
    if (editingId.value) {
      await updateTag(editingId.value, { ...form })
    } else {
      await createTag({ ...form })
    }
    ElMessage.success('保存成功')
    dialogVisible.value = false
    fetchTags()
  } finally {
    saving.value = false
  }
}

async function handleDelete(id) {
  await deleteTag(id)
  ElMessage.success('删除成功')
  fetchTags()
}

onMounted(fetchTags)
</script>

<style scoped>
.tag-manage {
  padding: 20px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
