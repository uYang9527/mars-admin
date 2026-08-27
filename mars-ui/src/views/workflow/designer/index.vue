<template>
  <div class="designer-container" ref="containerRef">
    <iframe ref="iframeRef" :src="iframeUrl" class="designer-iframe" frameborder="0" @load="loading = false" />
    <n-spin v-if="loading" class="designer-loading" description="设计器加载中..." />
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, onUnmounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const loading = ref(true)
const iframeRef = ref<HTMLIFrameElement | null>(null)

const iframeUrl = computed(() => {
  const id = route.query.id as string || ''
  const onlyDesignShow = route.query.onlyDesignShow as string || 'false'
  const disabled = route.query.disabled as string || 'false'
  const token = userStore.token || ''
  const params = new URLSearchParams({
    onlyDesignShow,
    disabled
  })
  if (id) {
    params.set('id', id)
  }
  if (token) {
    params.set('Authorization', token.startsWith('Bearer ') ? token : `Bearer ${token}`)
  }
  return `/warm-flow-ui/index.html?${params.toString()}`
})

function handleMessage(event: MessageEvent) {
  if (event.data?.method === 'close') {
    router.push({ path: '/workflow/definition', query: { t: String(Date.now()) } })
  }
}

onMounted(() => {
  window.addEventListener('message', handleMessage)
  setTimeout(() => { loading.value = false }, 8000)
})

onUnmounted(() => {
  window.removeEventListener('message', handleMessage)
})
</script>

<style scoped lang="scss">
.designer-container {
  width: 100%;
  height: calc(100vh - 96px);
  min-height: 640px;
  position: relative;
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
}

.designer-iframe {
  width: 100%;
  height: 100%;
}

.designer-loading {
  position: absolute;
  inset: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, 0.8);
}
</style>
