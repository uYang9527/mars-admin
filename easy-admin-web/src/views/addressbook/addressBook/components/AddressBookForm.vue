<template>
  <el-dialog
    v-model="visible"
    :title="`${paramsProps.title}`"
    :destroy-on-close="true"
    width="580px"
    draggable
  >
    <el-form
      ref="ruleFormRef"
      label-width="140px"
      label-suffix=" :"
      :rules="rules"
      :model="paramsProps.row"
      @submit.enter.prevent="handleSubmit"
    >
      <el-form-item label="用户id" prop="userId">
        <el-input-number
          v-model="paramsProps.row.userId" :precision="0" :min="1" :max="999999" />
      </el-form-item>
      <el-form-item label="收货人" prop="consignee">
        <el-input
          v-model="paramsProps.row.consignee"
          placeholder="请填写收货人"
          clearable
        ></el-input>
      </el-form-item>
      <el-form-item label="性别 0 女 1 男" prop="sex">
        <el-select v-model="paramsProps.row.sex" clearable placeholder="请选择性别 0 女 1 男">
          <el-option
            v-for="item in optionsStore.getDictOptions('')"
            :key="item.id"
            :label="item.codeName"
              :value="Number(item.id)"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="手机号" prop="phone">
        <el-input
          v-model="paramsProps.row.phone"
          placeholder="请填写手机号"
          clearable
        ></el-input>
      </el-form-item>
      <el-form-item label="省级区划编号" prop="provinceCode">
        <el-input
          v-model="paramsProps.row.provinceCode"
          placeholder="请填写省级区划编号"
          clearable
        ></el-input>
      </el-form-item>
      <el-form-item label="省级名称" prop="provinceName">
        <el-input
          v-model="paramsProps.row.provinceName"
          placeholder="请填写省级名称"
          clearable
        ></el-input>
      </el-form-item>
      <el-form-item label="市级区划编号" prop="cityCode">
        <el-input
          v-model="paramsProps.row.cityCode"
          placeholder="请填写市级区划编号"
          clearable
        ></el-input>
      </el-form-item>
      <el-form-item label="市级名称" prop="cityName">
        <el-input
          v-model="paramsProps.row.cityName"
          placeholder="请填写市级名称"
          clearable
        ></el-input>
      </el-form-item>
      <el-form-item label="区级区划编号" prop="districtCode">
        <el-input
          v-model="paramsProps.row.districtCode"
          placeholder="请填写区级区划编号"
          clearable
        ></el-input>
      </el-form-item>
      <el-form-item label="区级名称" prop="districtName">
        <el-input
          v-model="paramsProps.row.districtName"
          placeholder="请填写区级名称"
          clearable
        ></el-input>
      </el-form-item>
      <el-form-item label="详细地址" prop="detail">
        <el-input
          v-model="paramsProps.row.detail"
          placeholder="请填写详细地址"
          clearable
        ></el-input>
      </el-form-item>
      <el-form-item label="标签" prop="label">
        <el-input
          v-model="paramsProps.row.label"
          placeholder="请填写标签"
          clearable
        ></el-input>
      </el-form-item>
      <el-form-item label="默认 0 否 1是" prop="isDefault">
        <el-input-number
          v-model="paramsProps.row.isDefault" :precision="0" :min="1" :max="999999" />
      </el-form-item>
      <el-form-item label="创建人" prop="createUser">
        <el-input-number
          v-model="paramsProps.row.createUser" :precision="0" :min="1" :max="999999" />
      </el-form-item>
      <el-form-item label="修改人" prop="updateUser">
        <el-input-number
          v-model="paramsProps.row.updateUser" :precision="0" :min="1" :max="999999" />
      </el-form-item>
      <el-form-item label="是否删除" prop="isDeleted">
        <el-input-number
          v-model="paramsProps.row.isDeleted" :precision="0" :min="1" :max="999999" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="visible = false"> 取消</el-button>
      <el-button type="primary" @click="handleSubmit"> 确定</el-button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { type ElForm, ElMessage } from 'element-plus'
import { useOptionsStore } from '@/stores/modules/options'

defineOptions({
    name: 'AddressBookForm'
})

const optionsStore = useOptionsStore()

const rules = reactive({
  userId: [{ required: true, message: '请填写用户id' }],
  consignee: [{ required: true, message: '请填写收货人' }],
  sex: [{ required: true, message: '请填写性别 0 女 1 男' }],
  phone: [{ required: true, message: '请填写手机号' }],
  isDefault: [{ required: true, message: '请填写默认 0 否 1是' }],
  createTime: [{ required: true, message: '请填写创建时间' }],
  updateTime: [{ required: true, message: '请填写更新时间' }],
  createUser: [{ required: true, message: '请填写创建人' }],
  updateUser: [{ required: true, message: '请填写修改人' }],
  isDeleted: [{ required: true, message: '请填写是否删除' }],
})

const visible = ref(false)
const paramsProps = ref<View.DefaultParams>({
  title: '',
  row: {},
  api: undefined,
  getTableList: undefined
})

// 接收父组件传过来的参数
const acceptParams = (params: View.DefaultParams) => {
  paramsProps.value = params
  visible.value = true
}

// 提交数据（新增/编辑）
const ruleFormRef = ref<InstanceType<typeof ElForm>>()
const handleSubmit = () => {
  ruleFormRef.value!.validate(async (valid) => {
    if (!valid) return
    try {
      await paramsProps.value.api!(paramsProps.value.row)
      ElMessage.success({ message: `${paramsProps.value.title}成功！` })
      paramsProps.value.getTableList!()
      visible.value = false
    } catch (error) {
      console.log(error)
    }
  })
}

defineExpose({
  acceptParams
})
</script>

<style scoped lang="scss"></style>