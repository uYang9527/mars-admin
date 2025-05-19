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
      <el-form-item label="类型   1 菜品分类 2 套餐分类" prop="type">
        <el-select v-model="paramsProps.row.type" clearable placeholder="请选择类型   1 菜品分类 2 套餐分类">
          <el-option
            v-for="item in optionsStore.getDictOptions('')"
            :key="item.id"
            :label="item.codeName"
              :value="Number(item.id)"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="分类名称" prop="name">
        <el-input
          v-model="paramsProps.row.name"
          placeholder="请填写分类名称"
          clearable
        ></el-input>
      </el-form-item>
      <el-form-item label="顺序" prop="sort">
        <el-input-number
          v-model="paramsProps.row.sort" :precision="0" :min="1" :max="999999" />
      </el-form-item>
      <el-form-item label="创建人" prop="createUser">
        <el-input-number
          v-model="paramsProps.row.createUser" :precision="0" :min="1" :max="999999" />
      </el-form-item>
      <el-form-item label="修改人" prop="updateUser">
        <el-input-number
          v-model="paramsProps.row.updateUser" :precision="0" :min="1" :max="999999" />
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
    name: 'CategoryForm'
})

const optionsStore = useOptionsStore()

const rules = reactive({
  name: [{ required: true, message: '请填写分类名称' }],
  sort: [{ required: true, message: '请填写顺序' }],
  createTime: [{ required: true, message: '请填写创建时间' }],
  updateTime: [{ required: true, message: '请填写更新时间' }],
  createUser: [{ required: true, message: '请填写创建人' }],
  updateUser: [{ required: true, message: '请填写修改人' }],
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