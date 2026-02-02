<template>
  <div class="file-manager" @dragover.prevent="handleDragOver" @dragleave.prevent="handleDragLeave" @drop.prevent="handleDrop">
    <!-- 拖拽上传遮罩 -->
    <Transition name="fade">
      <div v-if="isDragging" class="drag-overlay">
        <div class="drag-content">
          <n-icon size="64" color="#fff"><CloudUploadOutline /></n-icon>
          <h3>松开鼠标上传文件</h3>
          <p>支持多文件同时上传</p>
        </div>
      </div>
    </Transition>

    <!-- 左侧分组列表 -->
    <div class="sidebar">
      <!-- 文件类型标签 -->
      <div class="type-tabs">
        <div 
          v-for="tab in typeTabs" 
          :key="tab.value"
          :class="['type-tab', { active: activeType === tab.value }]"
          @click="activeType = tab.value; loadFiles()"
        >
          {{ tab.label }}
        </div>
      </div>

      <!-- 分组列表 -->
      <div class="group-list">
        <div 
          :class="['group-item', { active: activeGroupId === -1 }]"
          @click="selectGroup(-1)"
        >
          <n-icon><FolderOutline /></n-icon>
          <span class="group-name">全部</span>
        </div>
        <div 
          :class="['group-item', { active: activeGroupId === null }]"
          @click="selectGroup(null)"
        >
          <n-icon><FolderOutline /></n-icon>
          <span class="group-name">未分组</span>
          <span v-if="ungroupedCount > 0" class="group-count">{{ ungroupedCount }}</span>
        </div>
        <div 
          v-for="group in groups" 
          :key="group.id"
          :class="['group-item', { active: activeGroupId === group.id }]"
          @click="selectGroup(group.id!)"
          @contextmenu.prevent="showGroupMenu($event, group)"
        >
          <n-icon><FolderOutline /></n-icon>
          <span class="group-name">{{ group.name }}</span>
          <span v-if="group.fileCount && group.fileCount > 0" class="group-count">{{ group.fileCount }}</span>
          <n-dropdown 
            trigger="click" 
            :options="groupMenuOptions" 
            @select="(key: string) => handleGroupAction(key, group)"
          >
            <n-icon class="group-more" @click.stop><EllipsisHorizontalOutline /></n-icon>
          </n-dropdown>
        </div>
      </div>

      <!-- 新增分组按钮 -->
      <div class="add-group" @click="showGroupModal = true">
        <n-icon><AddOutline /></n-icon>
        新增分组
      </div>
    </div>

    <!-- 右侧主内容 -->
    <div class="main-content">
      <!-- 工具栏 -->
      <div class="toolbar">
        <div class="toolbar-left">
          <n-upload
            v-if="hasPermission('sys:file:upload')"
            :custom-request="handleUpload"
            :show-file-list="false"
            :multiple="true"
          >
            <n-button type="primary">
              <template #icon><n-icon><CloudUploadOutline /></n-icon></template>
              上传
            </n-button>
          </n-upload>
          <n-button :disabled="selectedIds.length === 0" @click="handleBatchDelete">
            删除
          </n-button>
          <n-button :disabled="selectedIds.length === 0" @click="showMoveModal = true">
            移动
          </n-button>
        </div>
        <div class="toolbar-right">
          <n-input 
            v-model:value="searchName" 
            placeholder="请输入文件名称" 
            clearable
            style="width: 200px"
            @keyup.enter="loadFiles"
          >
            <template #suffix>
              <n-icon style="cursor: pointer" @click="loadFiles"><SearchOutline /></n-icon>
            </template>
          </n-input>
          <n-button-group>
            <n-button :type="viewMode === 'list' ? 'primary' : 'default'" @click="viewMode = 'list'">
              <template #icon><n-icon><ListOutline /></n-icon></template>
            </n-button>
            <n-button :type="viewMode === 'grid' ? 'primary' : 'default'" @click="viewMode = 'grid'">
              <template #icon><n-icon><GridOutline /></n-icon></template>
            </n-button>
          </n-button-group>
        </div>
      </div>

      <!-- 全选 -->
      <div class="select-all">
        <n-checkbox 
          :checked="isAllSelected" 
          :indeterminate="isIndeterminate"
          @update:checked="handleSelectAll"
        >
          全选
        </n-checkbox>
      </div>

      <!-- 文件列表 -->
      <n-spin :show="loading">
        <div v-if="files.length === 0" class="empty-state">
          <n-empty description="无数据~" />
        </div>
        
        <!-- 平铺视图 -->
        <div v-else-if="viewMode === 'grid'" class="file-grid">
          <div 
            v-for="file in files" 
            :key="file.id"
            :class="['file-card', { selected: selectedIds.includes(file.id!) }]"
            @click="toggleSelect(file)"
          >
            <div class="file-checkbox" @click.stop>
              <n-checkbox :checked="selectedIds.includes(file.id!)" @update:checked="toggleSelect(file)" />
            </div>
            <div class="file-preview" @click.stop="handlePreview(file)">
              <img v-if="isImage(file)" :src="file.url" alt="" />
              <video v-else-if="isVideo(file)" :src="file.url" />
              <div v-else class="file-icon">
                <n-icon size="48" :color="getFileIconColor(file)">
                  <component :is="getFileIcon(file)" />
                </n-icon>
              </div>
            </div>
            <div class="file-name" :title="file.originalName">{{ file.originalName }}</div>
            <div class="file-actions">
              <a @click.stop="handleRename(file)">重命名</a>
              <span>|</span>
              <a @click.stop="handleDownload(file)">下载</a>
              <span v-if="isPreviewable(file)">|</span>
              <a v-if="isPreviewable(file)" @click.stop="handlePreview(file)">查看</a>
            </div>
          </div>
        </div>

        <!-- 列表视图 -->
        <div v-else class="file-list">
          <div 
            v-for="file in files" 
            :key="file.id"
            :class="['file-row', { selected: selectedIds.includes(file.id!) }]"
            @click="toggleSelect(file)"
          >
            <div class="file-checkbox" @click.stop>
              <n-checkbox :checked="selectedIds.includes(file.id!)" @update:checked="toggleSelect(file)" />
            </div>
            <div class="file-preview-small" @click.stop="handlePreview(file)">
              <img v-if="isImage(file)" :src="file.url" alt="" />
              <n-icon v-else size="32" :color="getFileIconColor(file)">
                <component :is="getFileIcon(file)" />
              </n-icon>
            </div>
            <div class="file-info">
              <div class="file-name">{{ file.originalName }}</div>
              <div class="file-meta">
                <span>{{ formatFileSize(file.fileSize) }}</span>
                <span>{{ file.createTime }}</span>
              </div>
            </div>
            <div class="file-actions" @click.stop>
              <n-button size="small" quaternary @click="handlePreview(file)">预览</n-button>
              <n-button size="small" quaternary @click="handleDownload(file)">下载</n-button>
              <n-button size="small" quaternary @click="handleRename(file)">重命名</n-button>
              <n-button size="small" quaternary type="error" @click="handleDelete(file)">删除</n-button>
            </div>
          </div>
        </div>
      </n-spin>

      <!-- 分页 -->
      <div class="pagination">
        <span>共 {{ pagination.itemCount }} 条</span>
        <n-pagination
          v-model:page="pagination.page"
          :page-count="Math.ceil(pagination.itemCount / pagination.pageSize)"
          :page-slot="5"
          @update:page="loadFiles"
        />
        <span>前往</span>
        <n-input-number 
          v-model:value="gotoPage" 
          :min="1" 
          :max="Math.ceil(pagination.itemCount / pagination.pageSize)"
          size="small"
          style="width: 60px"
          @keyup.enter="pagination.page = gotoPage || 1; loadFiles()"
        />
        <span>页</span>
      </div>
    </div>

    <!-- 新增/编辑分组弹窗 -->
    <n-modal v-model:show="showGroupModal" preset="dialog" :title="editingGroup ? '编辑分组' : '新增分组'">
      <n-form :model="groupForm">
        <n-form-item label="分组名称" required>
          <n-input v-model:value="groupForm.name" placeholder="请输入分组名称" />
        </n-form-item>
      </n-form>
      <template #action>
        <n-space>
          <n-button @click="showGroupModal = false">取消</n-button>
          <n-button type="primary" @click="handleSaveGroup">确定</n-button>
        </n-space>
      </template>
    </n-modal>

    <!-- 移动到分组弹窗 -->
    <n-modal v-model:show="showMoveModal" preset="dialog" title="移动到分组">
      <n-form>
        <n-form-item label="目标分组">
          <n-select 
            v-model:value="moveTargetGroupId" 
            :options="moveGroupOptions" 
            placeholder="请选择分组"
          />
        </n-form-item>
      </n-form>
      <template #action>
        <n-space>
          <n-button @click="showMoveModal = false">取消</n-button>
          <n-button type="primary" @click="handleMoveFiles">确定</n-button>
        </n-space>
      </template>
    </n-modal>

    <!-- 重命名弹窗 -->
    <n-modal v-model:show="showRenameModal" preset="dialog" title="重命名">
      <n-form>
        <n-form-item label="文件名">
          <n-input v-model:value="renameValue" placeholder="请输入新文件名" />
        </n-form-item>
      </n-form>
      <template #action>
        <n-space>
          <n-button @click="showRenameModal = false">取消</n-button>
          <n-button type="primary" @click="handleSaveRename">确定</n-button>
        </n-space>
      </template>
    </n-modal>

    <!-- 预览弹窗 -->
    <n-modal v-model:show="previewVisible" preset="card" title="文件预览" :style="previewModalStyle">
      <div class="preview-container">
        <!-- 图片预览 -->
        <img v-if="isImage(previewFile)" :src="previewUrl" alt="预览" class="preview-image" />
        <!-- 视频预览 -->
        <video v-else-if="isVideo(previewFile)" :src="previewUrl" controls class="preview-video" />
        <!-- 音频预览 -->
        <audio v-else-if="isAudio(previewFile)" :src="previewUrl" controls />
        <!-- PDF预览 -->
        <iframe v-else-if="isPdf(previewFile)" :src="previewUrl" class="preview-pdf" />
        <!-- 文本/代码预览 -->
        <div v-else-if="isText(previewFile)" class="preview-text">
          <n-spin :show="textLoading">
            <n-code :code="previewText" :language="getCodeLanguage(previewFile)" show-line-numbers />
          </n-spin>
        </div>
        <!-- Office文档预览 (使用微软在线查看器) -->
        <div v-else-if="isOffice(previewFile)" class="preview-office">
          <iframe :src="getOfficePreviewUrl(previewFile)" class="preview-office-frame" />
        </div>
        <!-- 其他文件 -->
        <div v-else class="preview-other">
          <n-icon size="64"><DocumentOutline /></n-icon>
          <p>{{ previewFile?.originalName }}</p>
          <p class="preview-tip">该文件类型暂不支持预览</p>
          <n-button type="primary" @click="handleDownload(previewFile!)">下载文件</n-button>
        </div>
      </div>
    </n-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, h } from 'vue'
import { useMessage, useDialog, type UploadCustomRequestOptions } from 'naive-ui'
import { 
  CloudUploadOutline, SearchOutline, ListOutline, GridOutline, FolderOutline,
  AddOutline, EllipsisHorizontalOutline, DocumentOutline, DocumentTextOutline,
  ImageOutline, VideocamOutline, MusicalNotesOutline, CodeSlashOutline
} from '@vicons/ionicons5'
import { fileApi, fileGroupApi, type SysFile, type SysFileGroup } from '@/api/system'
import { useUserStore } from '@/stores/user'

const message = useMessage()
const dialog = useDialog()
const userStore = useUserStore()
const hasPermission = (permission: string) => userStore.hasPermission(permission)

// 文件类型标签
const typeTabs = [
  { label: '图片', value: 'image' },
  { label: '视频', value: 'video' },
  { label: '文件', value: 'other' }
]
const activeType = ref('image')

// 分组相关
const groups = ref<SysFileGroup[]>([])
const ungroupedCount = ref(0)
const activeGroupId = ref<number | null>(-1) // -1 表示全部

// 视图模式
const viewMode = ref<'list' | 'grid'>('grid')

// 搜索
const searchName = ref('')

// 文件列表
const files = ref<SysFile[]>([])
const loading = ref(false)
const selectedIds = ref<number[]>([])
const pagination = reactive({
  page: 1,
  pageSize: 20,
  itemCount: 0
})
const gotoPage = ref<number | null>(1)

// 分组弹窗
const showGroupModal = ref(false)
const editingGroup = ref<SysFileGroup | null>(null)
const groupForm = reactive({ name: '' })

// 移动弹窗
const showMoveModal = ref(false)
const moveTargetGroupId = ref<number | null>(null)

// 重命名弹窗
const showRenameModal = ref(false)
const renameValue = ref('')
const renamingFile = ref<SysFile | null>(null)

// 预览
const previewVisible = ref(false)
const previewFile = ref<SysFile | null>(null)
const previewUrl = ref('')
const previewText = ref('')
const textLoading = ref(false)

// 预览弹窗样式（根据文件类型调整大小）
const previewModalStyle = computed(() => {
  if (!previewFile.value) return { width: '800px' }
  if (isPdf(previewFile.value) || isOffice(previewFile.value)) {
    return { width: '90vw', height: '90vh' }
  }
  if (isText(previewFile.value)) {
    return { width: '900px', maxHeight: '80vh' }
  }
  return { width: '800px' }
})

// 拖拽上传
const isDragging = ref(false)
let dragCounter = 0

// 分组菜单选项
const groupMenuOptions = [
  { label: '编辑', key: 'edit' },
  { label: '删除', key: 'delete' }
]

// 移动分组选项
const moveGroupOptions = computed(() => {
  return [
    { label: '未分组', value: null },
    ...groups.value.map(g => ({ label: g.name, value: g.id }))
  ]
})

// 全选相关
const isAllSelected = computed(() => files.value.length > 0 && selectedIds.value.length === files.value.length)
const isIndeterminate = computed(() => selectedIds.value.length > 0 && selectedIds.value.length < files.value.length)

// 加载分组
async function loadGroups() {
  try {
    const res = await fileGroupApi.list()
    groups.value = res.groups
    ungroupedCount.value = res.ungroupedCount
  } catch (error) {
    // 错误已在拦截器处理
  }
}

// 加载文件
async function loadFiles() {
  loading.value = true
  selectedIds.value = []
  try {
    const res = await fileApi.pageByGroup({
      page: pagination.page,
      pageSize: pagination.pageSize,
      groupId: activeGroupId.value === -1 ? undefined : activeGroupId.value,
      fileCategory: activeType.value,
      originalName: searchName.value || undefined
    })
    files.value = res.list
    pagination.itemCount = res.total
  } catch (error) {
    // 错误已在拦截器处理
  } finally {
    loading.value = false
  }
}

// 选择分组
function selectGroup(groupId: number | null) {
  activeGroupId.value = groupId
  pagination.page = 1
  loadFiles()
}

// 选择/取消选择文件
function toggleSelect(file: SysFile) {
  const idx = selectedIds.value.indexOf(file.id!)
  if (idx === -1) {
    selectedIds.value.push(file.id!)
  } else {
    selectedIds.value.splice(idx, 1)
  }
}

// 全选/取消全选
function handleSelectAll(checked: boolean) {
  if (checked) {
    selectedIds.value = files.value.map(f => f.id!)
  } else {
    selectedIds.value = []
  }
}

// 分组操作
function handleGroupAction(key: string, group: SysFileGroup) {
  if (key === 'edit') {
    editingGroup.value = group
    groupForm.name = group.name
    showGroupModal.value = true
  } else if (key === 'delete') {
    dialog.warning({
      title: '提示',
      content: `确定要删除分组"${group.name}"吗？分组内的文件将移动到"未分组"。`,
      positiveText: '确定',
      negativeText: '取消',
      onPositiveClick: async () => {
        try {
          await fileGroupApi.delete(group.id!)
          message.success('删除成功')
          loadGroups()
          if (activeGroupId.value === group.id) {
            selectGroup(-1)
          }
        } catch (error) {
          // 错误已在拦截器处理
        }
      }
    })
  }
}

function showGroupMenu(e: MouseEvent, group: SysFileGroup) {
  // 右键菜单暂不实现，使用下拉菜单
}

// 保存分组
async function handleSaveGroup() {
  if (!groupForm.name.trim()) {
    message.warning('请输入分组名称')
    return
  }
  try {
    if (editingGroup.value) {
      await fileGroupApi.update({ id: editingGroup.value.id, name: groupForm.name })
      message.success('更新成功')
    } else {
      await fileGroupApi.create({ name: groupForm.name })
      message.success('创建成功')
    }
    showGroupModal.value = false
    editingGroup.value = null
    groupForm.name = ''
    loadGroups()
  } catch (error) {
    // 错误已在拦截器处理
  }
}

// 获取当前上传目标分组ID
function getUploadGroupId(): number | null {
  // 如果选中的是"全部"或"未分组"，则不设置分组
  if (activeGroupId.value === -1 || activeGroupId.value === null) {
    return null
  }
  return activeGroupId.value
}

// 上传
async function handleUpload(options: UploadCustomRequestOptions) {
  const { file, onFinish, onError } = options
  try {
    await fileApi.upload(file.file as File, undefined, getUploadGroupId())
    message.success('上传成功')
    onFinish()
    loadFiles()
    loadGroups()
  } catch (error) {
    onError()
  }
}

// 拖拽上传
function handleDragOver() {
  dragCounter++
  isDragging.value = true
}

function handleDragLeave() {
  dragCounter--
  if (dragCounter === 0) {
    isDragging.value = false
  }
}

async function handleDrop(e: DragEvent) {
  isDragging.value = false
  dragCounter = 0
  const droppedFiles = e.dataTransfer?.files
  if (!droppedFiles || droppedFiles.length === 0) return
  const uploadGroupId = getUploadGroupId()
  for (let i = 0; i < droppedFiles.length; i++) {
    try {
      await fileApi.upload(droppedFiles[i], undefined, uploadGroupId)
      message.success(`${droppedFiles[i].name} 上传成功`)
    } catch (error) {
      message.error(`${droppedFiles[i].name} 上传失败`)
    }
  }
  loadFiles()
  loadGroups()
}

// 预览
async function handlePreview(file: SysFile) {
  previewFile.value = file
  previewUrl.value = fileApi.getPreviewUrl(file.id!)
  previewText.value = ''
  
  // 如果是文本文件，获取内容
  if (isText(file)) {
    textLoading.value = true
    try {
      const text = await fileApi.getTextContent(file.id!)
      previewText.value = text
    } catch (error) {
      previewText.value = '无法加载文件内容'
    } finally {
      textLoading.value = false
    }
  }
  
  previewVisible.value = true
}

// 下载
function handleDownload(file: SysFile) {
  const link = document.createElement('a')
  link.href = fileApi.getDownloadUrl(file.id!)
  link.download = file.originalName
  link.click()
}

// 重命名
function handleRename(file: SysFile) {
  renamingFile.value = file
  renameValue.value = file.originalName
  showRenameModal.value = true
}

async function handleSaveRename() {
  if (!renameValue.value.trim()) {
    message.warning('请输入文件名')
    return
  }
  try {
    await fileApi.rename(renamingFile.value!.id!, renameValue.value)
    message.success('重命名成功')
    showRenameModal.value = false
    loadFiles()
  } catch (error) {
    // 错误已在拦截器处理
  }
}

// 删除
function handleDelete(file: SysFile) {
  dialog.warning({
    title: '提示',
    content: `确定要删除文件"${file.originalName}"吗？`,
    positiveText: '确定',
    negativeText: '取消',
    onPositiveClick: async () => {
      try {
        await fileApi.delete(file.id!)
        message.success('删除成功')
        loadFiles()
        loadGroups()
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
        loadFiles()
        loadGroups()
      } catch (error) {
        // 错误已在拦截器处理
      }
    }
  })
}

// 移动文件
async function handleMoveFiles() {
  try {
    await fileApi.moveToGroup(selectedIds.value, moveTargetGroupId.value)
    message.success('移动成功')
    showMoveModal.value = false
    selectedIds.value = []
    loadFiles()
    loadGroups()
  } catch (error) {
    // 错误已在拦截器处理
  }
}

// 文件类型判断
function isImage(file: SysFile | null): boolean {
  return file?.fileType?.startsWith('image/') || false
}

function isVideo(file: SysFile | null): boolean {
  return file?.fileType?.startsWith('video/') || false
}

function isAudio(file: SysFile | null): boolean {
  return file?.fileType?.startsWith('audio/') || false
}

function isPdf(file: SysFile | null): boolean {
  return file?.fileType === 'application/pdf' || file?.fileSuffix?.toLowerCase() === '.pdf'
}

function isOffice(file: SysFile | null): boolean {
  const officeSuffixes = ['.doc', '.docx', '.xls', '.xlsx', '.ppt', '.pptx']
  return officeSuffixes.includes(file?.fileSuffix?.toLowerCase() || '')
}

function isText(file: SysFile | null): boolean {
  if (!file) return false
  const textTypes = ['text/', 'application/json', 'application/xml', 'application/javascript']
  const textSuffixes = ['.txt', '.md', '.json', '.xml', '.yaml', '.yml', '.ini', '.conf', '.cfg', '.properties',
    '.js', '.ts', '.vue', '.jsx', '.tsx', '.css', '.scss', '.less', '.html', '.htm',
    '.java', '.py', '.go', '.rs', '.c', '.cpp', '.h', '.hpp', '.cs', '.php', '.rb', '.swift', '.kt',
    '.sql', '.sh', '.bat', '.ps1', '.log', '.csv']
  return textTypes.some(t => file.fileType?.startsWith(t)) || 
         textSuffixes.includes(file.fileSuffix?.toLowerCase() || '')
}

function isPreviewable(file: SysFile): boolean {
  return isImage(file) || isVideo(file) || isAudio(file) || isPdf(file) || isText(file) || isOffice(file)
}

// 获取代码语言（用于语法高亮）
function getCodeLanguage(file: SysFile | null): string {
  const suffix = file?.fileSuffix?.toLowerCase() || ''
  const langMap: Record<string, string> = {
    '.js': 'javascript', '.ts': 'typescript', '.vue': 'vue', '.jsx': 'jsx', '.tsx': 'tsx',
    '.css': 'css', '.scss': 'scss', '.less': 'less', '.html': 'html', '.htm': 'html',
    '.json': 'json', '.xml': 'xml', '.yaml': 'yaml', '.yml': 'yaml',
    '.java': 'java', '.py': 'python', '.go': 'go', '.rs': 'rust',
    '.c': 'c', '.cpp': 'cpp', '.h': 'c', '.hpp': 'cpp', '.cs': 'csharp',
    '.php': 'php', '.rb': 'ruby', '.swift': 'swift', '.kt': 'kotlin',
    '.sql': 'sql', '.sh': 'bash', '.bat': 'batch', '.ps1': 'powershell',
    '.md': 'markdown', '.txt': 'text', '.log': 'text', '.csv': 'text',
    '.ini': 'ini', '.conf': 'text', '.cfg': 'text', '.properties': 'properties'
  }
  return langMap[suffix] || 'text'
}

// 获取 Office 在线预览 URL（使用微软 Office Online）
function getOfficePreviewUrl(file: SysFile | null): string {
  if (!file) return ''
  // 需要文件有公网可访问的 URL
  const fileUrl = encodeURIComponent(file.url)
  return `https://view.officeapps.live.com/op/embed.aspx?src=${fileUrl}`
}

// 获取文件图标
function getFileIcon(file: SysFile) {
  const suffix = file.fileSuffix?.toLowerCase() || ''
  if (['.doc', '.docx'].includes(suffix)) return DocumentTextOutline
  if (['.xls', '.xlsx'].includes(suffix)) return DocumentTextOutline
  if (['.pdf'].includes(suffix)) return DocumentTextOutline
  if (['.txt', '.md'].includes(suffix)) return DocumentTextOutline
  if (['.js', '.ts', '.vue', '.html', '.css', '.json'].includes(suffix)) return CodeSlashOutline
  if (file.fileType?.startsWith('image/')) return ImageOutline
  if (file.fileType?.startsWith('video/')) return VideocamOutline
  if (file.fileType?.startsWith('audio/')) return MusicalNotesOutline
  return DocumentOutline
}

function getFileIconColor(file: SysFile) {
  const suffix = file.fileSuffix?.toLowerCase() || ''
  if (['.doc', '.docx'].includes(suffix)) return '#2b579a'
  if (['.xls', '.xlsx'].includes(suffix)) return '#217346'
  if (['.pdf'].includes(suffix)) return '#f40f02'
  if (['.txt', '.md'].includes(suffix)) return '#6b7280'
  return '#9ca3af'
}

// 格式化文件大小
function formatFileSize(bytes: number): string {
  if (bytes === 0) return '0 B'
  const k = 1024
  const sizes = ['B', 'KB', 'MB', 'GB', 'TB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
}

onMounted(() => {
  loadGroups()
  loadFiles()
})
</script>

<style scoped>
.file-manager {
  display: flex;
  height: calc(100vh - 120px);
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  position: relative;
}

/* 左侧边栏 */
.sidebar {
  width: 200px;
  border-right: 1px solid #e5e7eb;
  display: flex;
  flex-direction: column;
  background: #f9fafb;
}

.type-tabs {
  display: flex;
  padding: 12px;
  gap: 8px;
  border-bottom: 1px solid #e5e7eb;
}

.type-tab {
  padding: 6px 12px;
  cursor: pointer;
  font-size: 14px;
  color: #6b7280;
  border-radius: 4px;
  transition: all 0.2s;
}

.type-tab:hover {
  color: #3b82f6;
}

.type-tab.active {
  color: #3b82f6;
  font-weight: 500;
}

.group-list {
  flex: 1;
  overflow-y: auto;
  padding: 8px 0;
}

.group-item {
  display: flex;
  align-items: center;
  padding: 10px 16px;
  cursor: pointer;
  gap: 8px;
  color: #374151;
  transition: all 0.2s;
}

.group-item:hover {
  background: #e5e7eb;
}

.group-item.active {
  background: #dbeafe;
  color: #2563eb;
}

.group-name {
  flex: 1;
  font-size: 14px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.group-count {
  font-size: 12px;
  color: #9ca3af;
}

.group-more {
  opacity: 0;
  transition: opacity 0.2s;
}

.group-item:hover .group-more {
  opacity: 1;
}

.add-group {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
  padding: 12px;
  cursor: pointer;
  color: #6b7280;
  border-top: 1px solid #e5e7eb;
  font-size: 14px;
  transition: all 0.2s;
}

.add-group:hover {
  color: #3b82f6;
  background: #f3f4f6;
}

/* 主内容区 */
.main-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  border-bottom: 1px solid #e5e7eb;
}

.toolbar-left,
.toolbar-right {
  display: flex;
  align-items: center;
  gap: 8px;
}

.select-all {
  padding: 8px 16px;
  border-bottom: 1px solid #f3f4f6;
}

/* 空状态 */
.empty-state {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
}

/* 平铺视图 */
.file-grid {
  flex: 1;
  overflow-y: auto;
  padding: 16px;
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(140px, 1fr));
  gap: 16px;
  align-content: start;
}

.file-card {
  background: #fff;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  padding: 12px;
  cursor: pointer;
  position: relative;
  transition: all 0.2s;
}

.file-card:hover {
  border-color: #3b82f6;
  box-shadow: 0 2px 8px rgba(59, 130, 246, 0.1);
}

.file-card.selected {
  border-color: #3b82f6;
  background: #eff6ff;
}

.file-checkbox {
  position: absolute;
  top: 8px;
  left: 8px;
  z-index: 1;
}

.file-preview {
  width: 100%;
  height: 100px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f9fafb;
  border-radius: 4px;
  overflow: hidden;
  margin-bottom: 8px;
}

.file-preview img {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
}

.file-preview video {
  max-width: 100%;
  max-height: 100%;
}

.file-icon {
  display: flex;
  align-items: center;
  justify-content: center;
}

.file-card .file-name {
  font-size: 13px;
  color: #374151;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  margin-bottom: 4px;
}

.file-card .file-actions {
  display: flex;
  gap: 4px;
  font-size: 12px;
}

.file-card .file-actions a {
  color: #3b82f6;
  cursor: pointer;
}

.file-card .file-actions a:hover {
  text-decoration: underline;
}

.file-card .file-actions span {
  color: #d1d5db;
}

/* 列表视图 */
.file-list {
  flex: 1;
  overflow-y: auto;
}

.file-row {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  border-bottom: 1px solid #f3f4f6;
  cursor: pointer;
  gap: 12px;
  transition: background 0.2s;
}

.file-row:hover {
  background: #f9fafb;
}

.file-row.selected {
  background: #eff6ff;
}

.file-preview-small {
  width: 48px;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f3f4f6;
  border-radius: 4px;
  overflow: hidden;
}

.file-preview-small img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.file-info {
  flex: 1;
  min-width: 0;
}

.file-row .file-name {
  font-size: 14px;
  color: #374151;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.file-meta {
  display: flex;
  gap: 16px;
  font-size: 12px;
  color: #9ca3af;
  margin-top: 4px;
}

.file-row .file-actions {
  display: flex;
  gap: 4px;
}

/* 分页 */
.pagination {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 8px;
  padding: 12px 16px;
  border-top: 1px solid #e5e7eb;
  font-size: 14px;
  color: #6b7280;
}

/* 预览 */
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

.preview-tip {
  color: #9ca3af;
  font-size: 14px;
}

/* PDF预览 */
.preview-pdf {
  width: 100%;
  height: 80vh;
  border: none;
}

/* 文本/代码预览 */
.preview-text {
  max-height: 70vh;
  overflow: auto;
  background: #1e1e1e;
  border-radius: 8px;
}

.preview-text :deep(.n-code) {
  font-size: 13px;
  line-height: 1.6;
}

/* Office文档预览 */
.preview-office {
  width: 100%;
  height: 80vh;
}

.preview-office-frame {
  width: 100%;
  height: 100%;
  border: none;
}

/* 拖拽上传遮罩 */
.drag-overlay {
  position: absolute;
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

<!-- 暗黑模式样式（非 scoped） -->
<style>
body.dark-theme .file-manager {
  background: #18181c !important;
}

body.dark-theme .file-manager .sidebar {
  background: #1f1f23;
  border-right-color: #2d2d30;
}

body.dark-theme .file-manager .type-tabs {
  border-bottom-color: #2d2d30;
}

body.dark-theme .file-manager .type-tab {
  color: #a1a1aa;
}

body.dark-theme .file-manager .type-tab:hover,
body.dark-theme .file-manager .type-tab.active {
  color: #60a5fa;
}

body.dark-theme .file-manager .group-item {
  color: #d4d4d8;
}

body.dark-theme .file-manager .group-item:hover {
  background: #27272a;
}

body.dark-theme .file-manager .group-item.active {
  background: #1e3a5f;
  color: #60a5fa;
}

body.dark-theme .file-manager .group-count {
  color: #71717a;
}

body.dark-theme .file-manager .add-group {
  border-top-color: #2d2d30;
  color: #a1a1aa;
}

body.dark-theme .file-manager .add-group:hover {
  color: #60a5fa;
  background: #27272a;
}

body.dark-theme .file-manager .toolbar {
  border-bottom-color: #2d2d30;
}

body.dark-theme .file-manager .select-all {
  border-bottom-color: #27272a;
}

body.dark-theme .file-manager .file-card {
  background: #1f1f23;
  border-color: #2d2d30;
}

body.dark-theme .file-manager .file-card:hover {
  border-color: #3b82f6;
  box-shadow: 0 2px 8px rgba(59, 130, 246, 0.2);
}

body.dark-theme .file-manager .file-card.selected {
  border-color: #3b82f6;
  background: #1e3a5f;
}

body.dark-theme .file-manager .file-preview {
  background: #27272a;
}

body.dark-theme .file-manager .file-card .file-name {
  color: #e4e4e7;
}

body.dark-theme .file-manager .file-card .file-actions a {
  color: #60a5fa;
}

body.dark-theme .file-manager .file-card .file-actions span {
  color: #3f3f46;
}

body.dark-theme .file-manager .file-row {
  border-bottom-color: #27272a;
}

body.dark-theme .file-manager .file-row:hover {
  background: #27272a;
}

body.dark-theme .file-manager .file-row.selected {
  background: #1e3a5f;
}

body.dark-theme .file-manager .file-preview-small {
  background: #27272a;
}

body.dark-theme .file-manager .file-row .file-name {
  color: #e4e4e7;
}

body.dark-theme .file-manager .file-meta {
  color: #71717a;
}

body.dark-theme .file-manager .pagination {
  border-top-color: #2d2d30;
  color: #a1a1aa;
}

body.dark-theme .file-manager .preview-other {
  color: #a1a1aa;
}

body.dark-theme .file-manager .preview-tip {
  color: #71717a;
}
</style>
