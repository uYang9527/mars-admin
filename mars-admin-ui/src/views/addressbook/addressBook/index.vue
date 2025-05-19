<template>
  <div class="table-box">
    <ProTable
      ref="proTableRef"
      title="地址管理"
      :indent="20"
      :columns="columns"
      :search-columns="searchColumns"
      :request-api="getTableList"
      row-key="id"
    >
      <!-- 表格 header 按钮 -->
      <template #tableHeader="scope">
        <el-button type="primary"
          v-auth="'address.book.create'"
          :icon="CirclePlus"
          @click="openAddEdit('新增地址管理')"
        >
          新增
        </el-button>
        <el-button
          v-auth="'address.book.remove'"
          type="danger"
          :icon="Delete"
          plain
          :disabled="!scope.isSelected"
          @click="batchDelete(scope.selectedListIds)"
        >
          批量删除
        </el-button>
        <el-button
          v-auth="'address.book.import'"
          type="primary"
          :icon="Upload"
          plain
          @click="importData"
        >
          导入
        </el-button>
        <el-button
          v-auth="'address.book.export'"
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
          v-auth="'address.book.update'"
          type="primary"
          link
          :icon="EditPen"
          @click="openAddEdit('编辑地址管理', row, false)"
        >
          编辑
        </el-button>
        <el-button
            v-auth="'address.book.remove'"
          type="primary"
          link
          :icon="Delete"
          @click="deleteInfo(row)"
        >
          删除
        </el-button>
      </template>
    </ProTable>
    <AddressBookForm ref="addressBookRef" />
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
  createAddressBookApi,
  removeAddressBookApi,
  updateAddressBookApi,
  getAddressBookListApi,
  getAddressBookDetailApi,
  importAddressBookExcelApi,
  exportAddressBookExcelApi,
} from '@/api/modules/addressbook/addressBook';
import { useHandleData } from '@/hooks/useHandleData';
import AddressBookForm from '@/views/addressbook/addressBook/components/AddressBookForm.vue';
import type { ColumnProps, ProTableInstance, SearchProps } from '@/components/ProTable/interface';
import type { IAddressBook } from '@/api/interface/addressbook/addressBook';
import ImportExcel from '@/components/ImportExcel/index.vue';
import { downloadTemplate } from '@/api/modules/system/common';
import { ElMessageBox } from "element-plus";
import { useDownload } from "@/hooks/useDownload";
defineOptions({
  name: 'AddressBookView'
})
const proTableRef = ref<ProTableInstance>();
// 表格配置项
const columns: ColumnProps<IAddressBook.Row>[] = [
  { type: 'selection', width: 80 },
  { prop: 'userId', label: '用户id' },
  { prop: 'consignee', label: '收货人' },
  { prop: 'sex', label: '性别 0 女 1 男' },
  { prop: 'phone', label: '手机号' },
  { prop: 'provinceCode', label: '省级区划编号' },
  { prop: 'provinceName', label: '省级名称' },
  { prop: 'cityCode', label: '市级区划编号' },
  { prop: 'cityName', label: '市级名称' },
  { prop: 'districtCode', label: '区级区划编号' },
  { prop: 'districtName', label: '区级名称' },
  { prop: 'detail', label: '详细地址' },
  { prop: 'label', label: '标签' },
  { prop: 'isDefault', label: '默认 0 否 1是' },
  { prop: 'createUser', label: '创建人' },
  { prop: 'updateUser', label: '修改人' },
  { prop: 'isDeleted', label: '是否删除' },
  { prop: 'operation', label: '操作', width: 250, fixed: 'right' }
]
// 搜索条件项
const searchColumns: SearchProps[] = [
  { prop: 'userId', label: '用户id', el: 'input-number' },
  { prop: 'consignee', label: '收货人', el: 'input' },
  { prop: 'sex', label: '性别 0 女 1 男', el: 'select' },
  { prop: 'phone', label: '手机号', el: 'input' },
  { prop: 'provinceCode', label: '省级区划编号', el: 'input' },
  { prop: 'provinceName', label: '省级名称', el: 'input' },
  { prop: 'cityCode', label: '市级区划编号', el: 'input' },
  { prop: 'cityName', label: '市级名称', el: 'input' },
  { prop: 'districtCode', label: '区级区划编号', el: 'input' },
  { prop: 'districtName', label: '区级名称', el: 'input' },
  { prop: 'detail', label: '详细地址', el: 'input' },
  { prop: 'label', label: '标签', el: 'input' },
  { prop: 'isDefault', label: '默认 0 否 1是', el: 'input-number' },
  { prop: 'createUser', label: '创建人', el: 'input-number' },
  { prop: 'updateUser', label: '修改人', el: 'input-number' },
  { prop: 'isDeleted', label: '是否删除', el: 'input-number' },
]
// 获取table列表
const getTableList = (params: IAddressBook.Query) => {
  let newParams = formatParams(params);
  return getAddressBookListApi(newParams);
};
const formatParams = (params: IAddressBook.Query) =>{
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
const addressBookRef = ref<InstanceType<typeof AddressBookForm>>()
const openAddEdit = async(title: string, row: any = {}, isAdd = true) => {
  if (!isAdd) {
    const record = await getAddressBookDetailApi({ id: row?.id })
    row = record?.data
  }
  const params = {
    title,
    row: { ...row },
    api: isAdd ? createAddressBookApi : updateAddressBookApi,
    getTableList: proTableRef.value?.getTableList
  }
  addressBookRef.value?.acceptParams(params)
}
// 删除信息
const deleteInfo = async (params: IAddressBook.Row) => {
  await useHandleData(
    removeAddressBookApi,
    { ids: [params.id] },
    `删除【${params.id}】地址管理`
  )
  proTableRef.value?.getTableList()
}
// 批量删除信息
const batchDelete = async (ids: string[]) => {
  await useHandleData(removeAddressBookApi, { ids }, '删除所选地址管理')
  proTableRef.value?.clearSelection()
  proTableRef.value?.getTableList()
}
// 导入
const ImportExcelRef = ref<InstanceType<typeof ImportExcel>>()
const importData = () => {
  const params = {
    title: '地址管理',
    templateName: '地址管理',
    tempApi: downloadTemplate,
    importApi: importAddressBookExcelApi,
    getTableList: proTableRef.value?.getTableList
  }
  ImportExcelRef.value?.acceptParams(params)
}
// 导出
const downloadFile = async () => {
  let newParams = formatParams(proTableRef.value?.searchParam as IAddressBook.Query);
  useDownload(exportAddressBookExcelApi, "地址管理", newParams);
};
</script>