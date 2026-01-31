<template>
  <div 
    class="page-container"
    @dragover.prevent="handleDragOver"
    @dragleave.prevent="handleDragLeave"
    @drop.prevent="handleDrop"
  >
    <!-- 拖拽上传遮罩 -->
    <Transition name="fade">
      <div v-if="isDragging" class="drag-overlay">
        <div class="drag-content">
          <n-icon size="64" color="#111827"><CloudUploadOutline /></n-icon>
          <h3>松开鼠标上传文件</h3>
          <p>支持多文件同时上传</p>
        </div>
      </div>
    </Transition>
    
    <n-card>
      <!-- 搜索表单 -->
      <div class="search-form">
        <n-form inline :model="searchForm" label-placement="left">
          <n-form-item label="文件名">
            <n-input v-model:value="searchForm.originalName" placeholder="请输入文件名" clearable />
          </n-form-item>
          <n-form-item label="文件类型">
            <n-select
              v-model:value="searchForm.fileType"
              placeholder="请选择文件类型"
              :options="fileTypeOptions"
              clearable
              style="width: 150px"
            />
          </n-form-item>
          <n-form-item>
            <n-space>
              <n-button type="primary" @click="handleSearch">
                <template #icon><n-icon><SearchOutline /></n-icon></template>
                搜索
              </n-button>
              <n-button @click="handleReset">
                <template #icon><n-icon><RefreshOutline /></n-icon></template>
                重置
              </n-button>
            </n-space>
          </n-form-item>
        </n-form>
      </div>
      
      <!-- 工具栏 -->
      <div class="table-toolbar">
        <n-space>
          <n-upload
            :custom-request="handleUpload"
            :show-file-list="false"
            :multiple="true"
          >
            <n-button type="primary">
              <template #icon><n-icon><CloudUploadOutline /></n-icon></template>
              上传文件
            </n-button>
          </n-upload>
          <n-button
            type="error"
            :disabled="selectedIds.length === 0"
            @click="handleBatchDelete"
          >
            <template #icon><n-icon><TrashOutline /></n-icon></template>
            批量删除
          </n-button>
        </n-space>
        <span class="drag-tip">支持拖拽文件到此页面上传</span>
      </div>
      
      <!-- 表格 -->
      <n-data-table
        :columns="columns"
        :data="tableData"
        :loading="loading"
        :pagination="pagination"
        :row-key="(row: SysFile) => row.id"
        @update:page="handlePageChange"
        @update:page-size="handlePageSizeChange"
        @update:checked-row-keys="handleCheck"
      />
    </n-card>
    
    <!-- 预览弹窗 -->
    <n-modal v-model:show="previewVisible" preset="card" title="文件预览" style="width: 800px">
      <div class="preview-container">
        <img v-if="isImage(previewFile)" :src="previewUrl" alt="预览" class="preview-image" />
        <video v-else-if="isVideo(previewFile)" :src="previewUrl" controls class="preview-video" />
        <audio v-else-if="isAudio(previewFile)" :src="previewUrl" controls />
        <div v-else class="preview-other">
          <n-icon size="64"><DocumentOutline /></n-icon>
          <p>{{ previewFile?.originalName }}</p>
          <n-button type="primary" @click="handleDownload(previewFile!)">下载文件</n-button>
        </div>
      </div>
    </n-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, h, onMounted } from 'vue'
import { NButton, NTag, NSpace, NIcon, useMessage, useDialog, type DataTableColumns, type UploadCustomRequestOptions } from 'naive-ui'
import { SearchOutline, RefreshOutline, CloudUploadOutline, TrashOutline, EyeOutline, DownloadOutline, DocumentOutline } from '@vicons/ionicons5'
import { fileApi, type SysFile } from '@/api/system'

const message = useMessage()
const dialog = useDialog()

// 搜索表单
const searchForm = reactive({
  originalName: '',
  fileType: null as string | null
})

// 文件类型选项
const fileTypeOptions = [
  { label: '图片', value: 'image/' },
  { label: '视频', value: 'video/' },
  { label: '音频', value: 'audio/' },
  { label: '文档', value: 'application/' },
  { label: '文本', value: 'text/' }
]

// 表格数据
const tableData = ref<SysFile[]>([])
const loading = ref(false)
const selectedIds = ref<number[]>([])
const pagination = reactive({
  page: 1,
  pageSize: 10,
  itemCount: 0,
  showSizePicker: true,
  pageSizes: [10, 20, 50]
})

// 预览
const previewVisible = ref(false)
const previewFile = ref<SysFile | null>(null)
const previewUrl = ref('')

// 拖拽上传
const isDragging = ref(false)
let dragCounter = 0

function handleDragOver(e: DragEvent) {
  dragCounter++
  isDragging.value = true
}

function handleDragLeave(e: DragEvent) {
  dragCounter--
  if (dragCounter === 0) {
    isDragging.value = false
  }
}

async function handleDrop(e: DragEvent) {
  isDragging.value = false
  dragCounter = 0
  
  const files = e.dataTransfer?.files
  if (!files || files.length === 0) return
  
  // 上传所有文件
  const uploadPromises: Promise<void>[] = []
  for (let i = 0; i < files.length; i++) {
    uploadPromises.push(uploadFile(files[i]))
  }
  
  await Promise.all(uploadPromises)
  loadData()
}

async function uploadFile(file: File) {
  try {
    await fileApi.upload(file)
    message.success(`${file.name} 上传成功`)
  } catch (error) {
    message.error(`${file.name} 上传失败`)
  }
}

// 表格列
const columns: DataTableColumns<SysFile> = [
  { type: 'selection' },
  { title: 'ID', key: 'id', width: 80 },
  {
    title: '预览',
    key: 'preview',
    width: 80,
    render(row) {
      if (isImage(row)) {
        return h('img', {
          src: row.url,
          style: { width: '40px', height: '40px', objectFit: 'cover', cursor: 'pointer', borderRadius: '4px' },
          onClick: () => handlePreview(row)
        })
      }
      return h(NIcon, { size: 24, style: { cursor: 'pointer' }, onClick: () => handlePreview(row) }, { default: () => h(DocumentOutline) })
    }
  },
  { title: '文件名', key: 'originalName', ellipsis: { tooltip: true } },
  {
    title: '文件大小',
    key: 'fileSize',
    width: 100,
    render(row) {
      return formatFileSize(row.fileSize)
    }
  },
  {
    title: '存储类型',
    key: 'storageType',
    width: 100,
    render(row) {
      const typeMap: Record<string, { text: string; type: 'default' | 'success' | 'info' | 'warning' }> = {
        local: { text: '本地', type: 'default' },
        minio: { text: 'MinIO', type: 'success' },
        aliyun: { text: '阿里云OSS', type: 'info' }
      }
      const config = typeMap[row.storageType] || { text: row.storageType, type: 'default' as const }
      return h(NTag, { type: config.type, size: 'small' }, { default: () => config.text })
    }
  },
  { title: '上传者', key: 'createBy', width: 100 },
  { title: '上传时间', key: 'createTime', width: 180 },
  {
    title: '操作',
    key: 'actions',
    width: 180,
    fixed: 'right',
    render(row) {
      return h(NSpace, null, {
        default: () => [
          h(NButton, { size: 'small', quaternary: true, onClick: () => handlePreview(row) }, {
            default: () => [h(NIcon, null, { default: () => h(EyeOutline) }), ' 预览']
          }),
          h(NButton, { size: 'small', quaternary: true, onClick: () => handleDownload(row) }, {
            default: () => [h(NIcon, null, { default: () => h(DownloadOutline) }), ' 下载']
          }),
          h(NButton, { size: 'small', quaternary: true, type: 'error', onClick: () => handleDelete(row) }, {
            default: () => [h(NIcon, null, { default: () => h(TrashOutline) }), ' 删除']
          })
        ]
      })
    }
  }
]

// 判断文件类型
function isImage(file: SysFile | null): boolean {
  return file?.fileType?.startsWith('image/') || false
}

function isVideo(file: SysFile | null): boolean {
  return file?.fileType?.startsWith('video/') || false
}

function isAudio(file: SysFile | null): boolean {
  return file?.fileType?.startsWith('audio/') || false
}

// 格式化文件大小
function formatFileSize(bytes: number): string {
  if (bytes === 0) return '0 B'
  const k = 1024
  const sizes = ['B', 'KB', 'MB', 'GB', 'TB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
}

// 加载数据
async function loadData() {
  loading.value = true
  try {
    const res = await fileApi.page({
      page: pagination.page,
      pageSize: pagination.pageSize,
      originalName: searchForm.originalName || undefined,
      fileType: searchForm.fileType || undefined
    })
    tableData.value = res.list
    pagination.itemCount = res.total
  } catch (error) {
    // 错误已在拦截器处理
  } finally {
    loading.value = false
  }
}

// 搜索
function handleSearch() {
  pagination.page = 1
  loadData()
}

// 重置
function handleReset() {
  searchForm.originalName = ''
  searchForm.fileType = null
  handleSearch()
}

// 分页
function handlePageChange(page: number) {
  pagination.page = page
  loadData()
}

function handlePageSizeChange(pageSize: number) {
  pagination.pageSize = pageSize
  pagination.page = 1
  loadData()
}

// 选择
function handleCheck(keys: Array<string | number>) {
  selectedIds.value = keys as number[]
}

// 上传
async function handleUpload(options: UploadCustomRequestOptions) {
  const { file, onFinish, onError } = options
  try {
    await fileApi.upload(file.file as File)
    message.success('上传成功')
    onFinish()
    loadData()
  } catch (error) {
    onError()
  }
}

// 预览
function handlePreview(row: SysFile) {
  previewFile.value = row
  previewUrl.value = fileApi.getPreviewUrl(row.id!)
  previewVisible.value = true
}

// 下载
function handleDownload(row: SysFile) {
  const link = document.createElement('a')
  link.href = fileApi.getDownloadUrl(row.id!)
  link.download = row.originalName
  link.click()
}

// 删除
function handleDelete(row: SysFile) {
  dialog.warning({
    title: '提示',
    content: `确定要删除文件"${row.originalName}"吗？`,
    positiveText: '确定',
    negativeText: '取消',
    onPositiveClick: async () => {
      try {
        await fileApi.delete(row.id!)
        message.success('删除成功')
        loadData()
      } catch (error) {
        // 错误已在拦截器处理
      }
    }
  })
}

// 批量删除
function handleBatchDelete() {
  dialog.warning({
    title: '提示',
    content: `确定要删除选中的 ${selectedIds.value.length} 个文件吗？`,
    positiveText: '确定',
    negativeText: '取消',
    onPositiveClick: async () => {
      try {
        await fileApi.deleteBatch(selectedIds.value)
        message.success('删除成功')
        selectedIds.value = []
        loadData()
      } catch (error) {
        // 错误已在拦截器处理
      }
    }
  })
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.page-container {
  position: relative;
}

.search-form {
  margin-bottom: 16px;
}

.table-toolbar {
  margin-bottom: 16px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.drag-tip {
  color: #9CA3AF;
  font-size: 13px;
}

.preview-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 300px;
}

.preview-image {
  max-width: 100%;
  max-height: 500px;
}

.preview-video {
  max-width: 100%;
  max-height: 500px;
}

.preview-other {
  text-align: center;
  color: #666;
}

.preview-other p {
  margin: 16px 0;
}

/* 拖拽上传遮罩 */
.drag-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(17, 24, 39, 0.85);
  backdrop-filter: blur(4px);
  z-index: 1000;
  display: flex;
  align-items: center;
  justify-content: center;
}

.drag-content {
  text-align: center;
  color: #fff;
  padding: 60px 80px;
  border: 3px dashed rgba(255, 255, 255, 0.4);
  border-radius: 24px;
  background: rgba(255, 255, 255, 0.05);
}

.drag-content h3 {
  font-size: 24px;
  font-weight: 600;
  margin: 20px 0 8px;
}

.drag-content p {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.6);
  margin: 0;
}

/* 过渡动画 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
