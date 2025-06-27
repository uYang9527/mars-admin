<template>
  <div class="min-h-500px flex-col-stretch gap-16px overflow-hidden lt-sm:overflow-auto">
    <ACard title="参数设置" :bordered="false" size="small" class="sm:flex-1-hidden card-wrapper">
      <div class="flex-center p-20">
        <h3>参数设置页面</h3>
        <p>功能开发中...</p>
      </div>
    </ACard>
  </div>
</template>

<script setup lang="ts">
defineOptions({
  name: 'ManageConfig'
});
</script>

<style scoped>
.card-wrapper {
  @apply flex-col-stretch gap-16px overflow-hidden;
}
</style> 