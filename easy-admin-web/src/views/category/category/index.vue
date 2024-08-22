<template>
  <div class="table-box">
    <ProTable
      ref="proTableRef"
      title="菜品及套餐分类"
      :indent="20"
      :columns="columns"
      :search-columns="searchColumns"
      :request-api="getTableList"
      row-key="id"
    >
      <!-- 表格 header 按钮 -->
      <template #tableHeader="scope">
        <el-button type="primary"
          v-auth="'category.create'"
          :icon="CirclePlus"
          @click="openAddEdit('新增菜品及套餐分类')"
        >
          新增
        </el-button>
        <el-button
          v-auth="'category.remove'"
          type="danger"
          :icon="Delete"
          plain
          :disabled="!scope.isSelected"
          @click="batchDelete(scope.selectedListIds)"
        >
          批量删除
        </el-button>
        <el-button
          v-auth="'category.import'"
          type="primary"
          :icon="Upload"
          plain
          @click="importData"
        >
          导入
        </el-button>
        <el-button
          v-auth="'category.export'"
          type="primary"
          :icon="Download"
          plain
          @click="downloadFile"
        >
          导出
        </el-button>
      </template>
      <template #operation="{ row }">
        <el-button
          v-auth="'category.update'"
          type="primary"
          link
          :icon="EditPen"
          @click="openAddEdit('编辑菜品及套餐分类', row, false)"
        >
          编辑
        </el-button>
        <el-button
            v-auth="'category.remove'"
          type="primary"
          link
          :icon="Delete"
          @click="deleteInfo(row)"
        >
          删除
        </el-button>
      </template>
    </ProTable>
    <CategoryForm ref="categoryRef" />
    <ImportExcel ref="ImportExcelRef" />
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import {
  CirclePlus,
  Delete,
  EditPen,
  Upload,
  Download,
} from '@element-plus/icons-vue'
import ProTable from '@/components/ProTable/index.vue'
import {
  createCategoryApi,
  removeCategoryApi,
  updateCategoryApi,
  getCategoryListApi,
  getCategoryDetailApi,
  importCategoryExcelApi,
  exportCategoryExcelApi,
} from '@/api/modules/category/category';
import { useHandleData } from '@/hooks/useHandleData';
import CategoryForm from '@/views/category/category/components/CategoryForm.vue';
import type { ColumnProps, ProTableInstance, SearchProps } from '@/components/ProTable/interface';
import type { ICategory } from '@/api/interface/category/category';
import ImportExcel from '@/components/ImportExcel/index.vue';
import { downloadTemplate } from '@/api/modules/system/common';
import { ElMessageBox } from "element-plus";
import { useDownload } from "@/hooks/useDownload";
defineOptions({
  name: 'CategoryView'
})
const proTableRef = ref<ProTableInstance>();
// 表格配置项
const columns: ColumnProps<ICategory.Row>[] = [
  { type: 'selection', width: 80 },
  { prop: 'type', label: '类型   1 菜品分类 2 套餐分类' },
  { prop: 'name', label: '分类名称' },
  { prop: 'sort', label: '顺序' },
  { prop: 'createUser', label: '创建人' },
  { prop: 'updateUser', label: '修改人' },
  { prop: 'operation', label: '操作', width: 250, fixed: 'right' }
]
// 搜索条件项
const searchColumns: SearchProps[] = [
  { prop: 'type', label: '类型   1 菜品分类 2 套餐分类', el: 'select' },
  { prop: 'name', label: '分类名称', el: 'input' },
  { prop: 'sort', label: '顺序', el: 'input-number' },
  { prop: 'createUser', label: '创建人', el: 'input-number' },
  { prop: 'updateUser', label: '修改人', el: 'input-number' },
]
// 获取table列表
const getTableList = (params: ICategory.Query) => {
  let newParams = formatParams(params);
  return getCategoryListApi(newParams);
};
const formatParams = (params: ICategory.Query) =>{
  let newParams = JSON.parse(JSON.stringify(params));
  newParams.createTime && (newParams.createTimeStart = newParams.createTime[0]);
  newParams.createTime && (newParams.createTimeEnd = newParams.createTime[1]);
  delete newParams.createTime;
  newParams.updateTime && (newParams.updateTimeStart = newParams.updateTime[0]);
  newParams.updateTime && (newParams.updateTimeEnd = newParams.updateTime[1]);
  delete newParams.updateTime;
  return newParams;
}
// 打开 drawer(新增、查看、编辑)
const categoryRef = ref<InstanceType<typeof CategoryForm>>()
const openAddEdit = async(title: string, row: any = {}, isAdd = true) => {
  if (!isAdd) {
    const record = await getCategoryDetailApi({ id: row?.id })
    row = record?.data
  }
  const params = {
    title,
    row: { ...row },
    api: isAdd ? createCategoryApi : updateCategoryApi,
    getTableList: proTableRef.value?.getTableList
  }
  categoryRef.value?.acceptParams(params)
}
// 删除信息
const deleteInfo = async (params: ICategory.Row) => {
  await useHandleData(
    removeCategoryApi,
    { ids: [params.id] },
    `删除【${params.id}】菜品及套餐分类`
  )
  proTableRef.value?.getTableList()
}
// 批量删除信息
const batchDelete = async (ids: string[]) => {
  await useHandleData(removeCategoryApi, { ids }, '删除所选菜品及套餐分类')
  proTableRef.value?.clearSelection()
  proTableRef.value?.getTableList()
}
// 导入
const ImportExcelRef = ref<InstanceType<typeof ImportExcel>>()
const importData = () => {
  const params = {
    title: '菜品及套餐分类',
    templateName: '菜品及套餐分类',
    tempApi: downloadTemplate,
    importApi: importCategoryExcelApi,
    getTableList: proTableRef.value?.getTableList
  }
  ImportExcelRef.value?.acceptParams(params)
}
// 导出
const downloadFile = async () => {
  let newParams = formatParams(proTableRef.value?.searchParam as ICategory.Query);
  useDownload(exportCategoryExcelApi, "菜品及套餐分类", newParams);
};
</script>