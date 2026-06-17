<template>
  <div class="tag-manage">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>标签管理</span>
          <el-button type="primary" @click="openDialog()">新增标签</el-button>
        </div>
      </template>
      <el-table :data="tags" v-loading="loading" style="width:100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="标签名称" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="160">
          <template #default="{ row }">
            <el-button link type="primary" @click="openDialog(row)">编辑</el-button>
            <el-popconfirm title="确定删除?" @confirm="handleDelete(row.id)">
              <template #reference>
                <el-button link type="danger">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="editId ? '编辑标签' : '新增标签'" width="400px">
      <el-form ref="dialogFormRef" :model="dialogForm" :rules="dialogRules" label-width="80px">
        <el-form-item label="名称" prop="name">
          <el-input v-model="dialogForm.name" placeholder="标签名称" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getTags, createTag, updateTag, deleteTag } from '../api/tag'
import { ElMessage } from 'element-plus'

const tags = ref([])
const loading = ref(false)
const saving = ref(false)
const dialogVisible = ref(false)
const editId = ref(null)
const dialogFormRef = ref()
const dialogForm = reactive({ name: '' })
const dialogRules = { name: [{ required: true, message: '请输入标签名称', trigger: 'blur' }] }

async function loadTags() {
  loading.value = true
  try {
    const res = await getTags()
    tags.value = res.data || []
  } catch (e) {
    ElMessage.error('加载标签失败')
  } finally {
    loading.value = false
  }
}

function openDialog(row) {
  editId.value = row ? row.id : null
  dialogForm.name = row ? row.name : ''
  dialogVisible.value = true
}

async function handleSave() {
  const valid = await dialogFormRef.value.validate().catch(() => false)
  if (!valid) return
  saving.value = true
  try {
    if (editId.value) {
      await updateTag(editId.value, { name: dialogForm.name })
      ElMessage.success('更新成功')
    } else {
      await createTag({ name: dialogForm.name })
      ElMessage.success('创建成功')
    }
    dialogVisible.value = false
    loadTags()
  } catch (e) {
    ElMessage.error('保存失败')
  } finally {
    saving.value = false
  }
}

async function handleDelete(id) {
  try {
    await deleteTag(id)
    ElMessage.success('删除成功')
    loadTags()
  } catch (e) {
    ElMessage.error('删除失败')
  }
}

onMounted(loadTags)
</script>

<style scoped>
.tag-manage { padding: 20px; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
</style>
