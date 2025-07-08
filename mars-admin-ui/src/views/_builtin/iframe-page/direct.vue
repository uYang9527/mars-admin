<script setup lang="ts">
import { onActivated, onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';

// 从路由查询参数获取URL
const route = useRoute();
const externalUrl = ref('');

onMounted(() => {
  // 从查询参数中获取URL
  const url = route.query.url as string;
  if (url) {
    console.log('iframe-direct 接收到URL:', url);
    try {
      externalUrl.value = url;
      console.log('iframe-direct 设置URL:', externalUrl.value);
    } catch (error) {
      console.error('URL解码错误:', error);
    }
  } else {
    console.error('未提供外部URL');
  }
});

onActivated(() => {
  console.log('iframe-direct activated, URL:', externalUrl.value);
});
</script>

<template>
  <div class="h-full">
    <iframe v-if="externalUrl" id="iframePage" class="size-full" :src="externalUrl"></iframe>
    <div v-else class="flex-center h-full">
      <h2 class="text-error">未提供有效的外部URL</h2>
    </div>
  </div>
</template>

<style scoped></style> 