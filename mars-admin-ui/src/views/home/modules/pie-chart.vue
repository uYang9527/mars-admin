<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { useEcharts } from '@/hooks/common/echarts';

defineOptions({
  name: 'PieChart'
});

const { domRef, updateOptions } = useEcharts(() => ({
  tooltip: {
    trigger: 'item',
    formatter: '{a} <br/>{b}: {c} ({d}%)'
  },
  legend: {
    bottom: '8%',
    left: 'center',
    itemStyle: {
      borderWidth: 0
    },
    textStyle: {
      fontSize: 12
    }
  },
  series: [
    {
      color: ['#5da8ff', '#8e9dff', '#fedc69', '#26deca'],
      name: '模块使用情况',
      type: 'pie',
      radius: ['40%', '70%'],
      center: ['50%', '42%'],
      avoidLabelOverlap: false,
      itemStyle: {
        borderRadius: 8,
        borderColor: '#fff',
        borderWidth: 2
      },
      label: {
        show: true,
        position: 'outside',
        fontSize: 11,
        formatter: '{b}\n{d}%'
      },
      emphasis: {
        label: {
          show: true,
          fontSize: 12,
          fontWeight: 'bold'
        },
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
      },
      labelLine: {
        show: true,
        length: 10,
        length2: 8,
        smooth: true
      },
      data: [
        { name: '用户管理', value: 35 },
        { name: '权限管理', value: 25 },
        { name: '系统配置', value: 20 },
        { name: '数据统计', value: 20 }
      ]
    }
  ]
}));

onMounted(() => {
  // 确保图表能够正确渲染
  setTimeout(() => {
    updateOptions(opts => opts);
  }, 100);
});
</script>

<template>
  <div ref="domRef" class="w-full h-360px"></div>
</template>

<style scoped></style>
