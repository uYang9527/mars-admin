<script setup lang="ts">
import { onActivated, onMounted, ref } from 'vue';

interface Props {
  url: string;
}

const props = defineProps<Props>();
const decodedUrl = ref('');

onMounted(() => {
  console.log('iframe-page mounted, 原始URL参数:', props.url);
  decodedUrl.value = decodeURIComponent(props.url);
  console.log('iframe-page 解码后的URL:', decodedUrl.value);
});

onActivated(() => {
  console.log('iframe-page activated, URL:', decodedUrl.value);
});
</script>

<template>
  <div class="h-full">
    <iframe id="iframePage" class="size-full" :src="decodedUrl"></iframe>
  </div>
</template>

<style scoped></style>
