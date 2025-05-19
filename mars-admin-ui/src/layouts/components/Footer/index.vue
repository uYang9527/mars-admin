<template>
  <div class="footer flx-center">
    <a href="" target="_blank">
      Easy-Admin v{{ getVersion() }} | Copyright © 2022-{{ new Date().getFullYear() }}
    </a>
  </div>
</template>
<script setup lang="ts">
import PackageJson from 'package.json'

const getVersion = () => {
  return PackageJson.version
}
</script>
<style scoped lang="scss">
@import './index.scss';
</style>
