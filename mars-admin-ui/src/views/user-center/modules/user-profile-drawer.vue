<script setup lang="ts">
import { computed, reactive, ref, watch } from 'vue';
import { useAntdForm, useFormRules } from '@/hooks/common/form';
import { fetchChangePassword, fetchGetUserInfo } from '@/service/api/auth';
import { fetchCheckEmailUnique, fetchCheckPhoneUnique, fetchUpdateUser } from '@/service/api/system-manage';
import { useAuthStore } from '@/store/modules/auth';
import { $t } from '@/locales';

defineOptions({
  name: 'UserProfileDrawer'
});

interface Emits {
  (e: 'refresh'): void;
}

const emit = defineEmits<Emits>();

const visible = defineModel<boolean>('visible', {
  default: false
});

const authStore = useAuthStore();
const { formRef, validate, resetFields } = useAntdForm();
const { formRules } = useFormRules();

// 添加加载状态
const loading = ref(false);
const passwordLoading = ref(false);

// 当前激活的标签页
const activeTab = ref('profile');

// 用户信息表单
type UserProfileModel = {
  id?: number;
  username: string;
  nickname: string;
  realName: string;
  email: string;
  phone: string;
  gender: number;
  avatar: string;
  birthday: string;
  remark: string;
};

const userModel: UserProfileModel = reactive({
  username: '',
  nickname: '',
  realName: '',
  email: '',
  phone: '',
  gender: 0,
  avatar: '',
  birthday: '',
  remark: ''
});

// 密码修改表单
const passwordModel = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
});

const { formRef: passwordFormRef, validate: validatePassword, resetFields: resetPasswordFields } = useAntdForm();

// 表单验证规则
const userRules = computed(() => {
  const { patternRules } = useFormRules();

  return {
    nickname: formRules.nickName,
    realName: [
      { required: true, message: '请输入真实姓名', trigger: 'blur' },
      { min: 2, max: 20, message: '真实姓名长度在 2 到 20 个字符', trigger: 'blur' }
    ],
    phone: [
      {
        validator: async (_, value) => {
          // 允许脱敏格式通过
          if (!value) return Promise.resolve();
          // 脱敏格式：如 184****8369
          if (/^1\d{2}\*{4}\d{4}$/.test(value)) return Promise.resolve();
          // 完整手机号格式
          if (!/^1[3-9]\d{9}$/.test(value)) {
            return Promise.reject(new Error('请输入正确的手机号'));
          }
          // 唯一性校验
          const isUnique = await fetchCheckPhoneUnique(value, userModel.id);
          if (!isUnique.data) {
            return Promise.reject(new Error('手机号已存在'));
          }
          return Promise.resolve();
        },
        trigger: 'blur'
      }
    ],
    email: [
      patternRules.email,
      {
        validator: async (_, value) => {
          if (!value) return Promise.resolve();

          const isUnique = await fetchCheckEmailUnique(value, userModel.id);
          if (!isUnique.data) {
            return Promise.reject(new Error('邮箱已存在'));
          }
          return Promise.resolve();
        },
        trigger: 'blur'
      }
    ]
  };
});

const passwordRules = {
  oldPassword: [{ required: true, message: '请输入当前密码', trigger: 'blur' }],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    {
      validator: (_, value) => {
        if (value !== passwordModel.newPassword) {
          return Promise.reject(new Error('两次输入的密码不一致'));
        }
        return Promise.resolve();
      },
      trigger: 'blur'
    }
  ]
};

const genderOptions = [
  { label: '未知', value: 0 },
  { label: '男', value: 1 },
  { label: '女', value: 2 }
];

const uploadAction = '/proxy-default/file/upload';
const uploadHeaders = computed(() => ({
  Authorization: `Bearer ${localStorage.getItem('token') || ''}`
}));

function beforeAvatarUpload(file: File) {
  const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png';
  if (!isJpgOrPng) {
    window.$message?.error('只支持 JPG/PNG 格式!');
    return false;
  }
  const isLt2M = file.size / 1024 / 1024 < 2;
  if (!isLt2M) {
    window.$message?.error('图片大小不能超过 2MB!');
    return false;
  }
  return true;
}

async function handleAvatarChange(info: any) {
  const { status, response } = info.file;
  if (status === 'done') {
    const url = response?.data?.url || response?.url;
    if (url) {
      userModel.avatar = url;
      window.$message?.success('头像上传成功');
      await handleUpdateProfile();
    }
  } else if (status === 'error') {
    window.$message?.error('头像上传失败');
  }
}

// 加载用户信息
async function loadUserInfo() {
  try {
    const { data } = await fetchGetUserInfo();
    if (data) {
      Object.assign(userModel, {
        id: data.id || data.userId,
        username: data.userName || data.username,
        nickname: data.nickName || data.nickname || '',
        realName: data.realName || '',
        email: data.email || '',
        phone: data.phone || '',
        gender: data.gender || 0,
        avatar: data.avatar || '',
        birthday: data.birthday || '',
        remark: data.remark || ''
      });
    }
  } catch (error) {
    console.error('加载用户信息失败:', error);
    console.error('加载用户信息失败:', error);
  }
}

// 更新用户信息
async function handleUpdateProfile() {
  try {
    await validate();
    loading.value = true;

    const { error } = await fetchUpdateUser({
      id: userModel.id,
      nickname: userModel.nickname,
      realName: userModel.realName,
      email: userModel.email,
      phone: userModel.phone,
      gender: userModel.gender,
      avatar: userModel.avatar,
      birthday: userModel.birthday,
      remark: userModel.remark
    });

    if (!error) {
      window.$message?.success('个人信息更新成功');
      // 刷新认证store中的用户信息
      await authStore.initUserInfo();
      emit('refresh');
      // 关闭抽屉
      closeDrawer();
    }
  } catch (error) {
    console.error('更新个人信息失败:', error);
  } finally {
    loading.value = false;
  }
}

// 修改密码
async function handleChangePassword() {
  try {
    await validatePassword();
    passwordLoading.value = true;

    const { error } = await fetchChangePassword(passwordModel.oldPassword, passwordModel.newPassword);

    if (!error) {
      window.$message?.success('密码修改成功');
      resetPasswordFields();
      // 清空密码表单
      Object.assign(passwordModel, {
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
      });
      emit('refresh');
      // 关闭抽屉
      closeDrawer();
    }
  } catch (error) {
    console.error('修改密码失败:', error);
  } finally {
    passwordLoading.value = false;
  }
}

// 关闭抽屉
function closeDrawer() {
  visible.value = false;
  activeTab.value = 'profile';
}

// 监听抽屉显示状态
watch(visible, newVisible => {
  if (newVisible) {
    loadUserInfo();
    resetFields();
    resetPasswordFields();
  }
});
</script>

<template>
  <ADrawer
    v-model:visible="visible"
    title="个人中心"
    :width="500"
    :body-style="{ paddingRight: '20px' }"
    @close="closeDrawer"
  >
    <ATabs v-model:active-key="activeTab" type="card">
      <!-- 个人信息 -->
      <ATabPane key="profile" tab="个人信息">
        <div class="mb-10px">
          <AForm
            ref="formRef"
            :model="userModel"
            :rules="userRules"
            :label-col="{ span: 4 }"
            :wrapper-col="{ span: 20 }"
          >
            <!-- 头像 -->
            <AFormItem label="头像">
              <div class="flex-y-center gap-16px">
                <AAvatar :size="80" :src="userModel.avatar" class="relative overflow-hidden border-2 border-gray-200">
                  <template #icon>
                    <span class="absolute left-1/2 top-1/2 block -translate-x-1/2 -translate-y-1/2">
                      <SvgIcon icon="ph:user-circle" class="text-40px" />
                    </span>
                  </template>
                </AAvatar>
                <div>
                  <AUpload
                    name="file"
                    :show-upload-list="false"
                    :action="uploadAction"
                    :headers="uploadHeaders"
                    :data="{ configKey: 'minio' }"
                    :before-upload="beforeAvatarUpload"
                    @change="handleAvatarChange"
                  >
                    <AButton type="primary" size="small" class="inline-flex items-center">
                      <SvgIcon icon="ph:upload" class="mr-4px" />
                      更换头像
                    </AButton>
                  </AUpload>
                  <div class="mt-4px text-12px text-gray-500">支持 JPG、PNG 格式，文件大小不超过 2MB</div>
                </div>
              </div>
            </AFormItem>

            <!-- 用户名 -->
            <AFormItem label="用户名">
              <AInput v-model:value="userModel.username" disabled />
            </AFormItem>
            <div>
              <!-- 昵称 -->
              <AFormItem label="昵称" name="nickname">
                <AInput v-model:value="userModel.nickname" placeholder="请输入昵称" />
              </AFormItem>

              <!-- 真实姓名 -->
              <AFormItem label="真实姓名" name="realName">
                <AInput v-model:value="userModel.realName" placeholder="请输入真实姓名" />
              </AFormItem>
            </div>

            <!-- 性别 -->
            <AFormItem label="性别">
              <ARadioGroup v-model:value="userModel.gender" :options="genderOptions" />
            </AFormItem>

            <!-- 手机号 -->
            <AFormItem label="手机号" name="phone">
              <AInput v-model:value="userModel.phone" placeholder="请输入手机号" />
            </AFormItem>

            <!-- 邮箱 -->
            <AFormItem label="邮箱" name="email">
              <AInput v-model:value="userModel.email" placeholder="请输入邮箱" />
            </AFormItem>

            <!-- 个人简介 -->
            <AFormItem label="个人简介">
              <ATextarea
                v-model:value="userModel.remark"
                placeholder="请输入个人简介"
                :rows="3"
                :max-length="200"
                show-count
              />
            </AFormItem>
          </AForm>

          <div class="mt-15px text-right">
            <ASpace>
              <AButton @click="closeDrawer">取消</AButton>
              <AButton type="primary" :loading="loading" class="inline-flex items-center" @click="handleUpdateProfile">
                <SvgIcon icon="ph:check" class="mr-4px" />
                保存修改
              </AButton>
            </ASpace>
          </div>
        </div>
      </ATabPane>

      <!-- 修改密码 -->
      <ATabPane key="password" tab="修改密码">
        <div class="mb-24px">
          <AForm
            ref="passwordFormRef"
            :model="passwordModel"
            :rules="passwordRules"
            :label-col="{ span: 6 }"
            :wrapper-col="{ span: 18 }"
          >
            <AFormItem label="当前密码" name="oldPassword">
              <AInputPassword
                v-model:value="passwordModel.oldPassword"
                placeholder="请输入当前密码"
                autocomplete="current-password"
              />
            </AFormItem>

            <AFormItem label="新密码" name="newPassword">
              <AInputPassword
                v-model:value="passwordModel.newPassword"
                placeholder="请输入新密码"
                autocomplete="new-password"
              />
            </AFormItem>

            <AFormItem label="确认密码" name="confirmPassword">
              <AInputPassword
                v-model:value="passwordModel.confirmPassword"
                placeholder="请再次输入新密码"
                autocomplete="new-password"
              />
            </AFormItem>
          </AForm>

          <div class="mt-24px text-right">
            <ASpace>
              <AButton @click="closeDrawer">取消</AButton>
              <AButton
                type="primary"
                :loading="passwordLoading"
                class="inline-flex items-center"
                @click="handleChangePassword"
              >
                <SvgIcon icon="ph:key" class="mr-4px" />
                修改密码
              </AButton>
            </ASpace>
          </div>
        </div>

        <!-- 密码安全提示 -->
        <AAlert
          type="info"
          show-icon
          message="密码安全提示"
          description="为了您的账户安全，建议您定期更换密码，密码长度在6-20个字符之间，建议包含数字、字母和特殊字符。"
        />
      </ATabPane>

      <!-- 账户信息 -->
      <ATabPane key="account" tab="账户信息">
        <div class="space-y-16px">
          <ACard title="基本信息" size="small">
            <ADescriptions :column="1" bordered>
              <ADescriptionsItem label="用户ID">
                {{ userModel.id || '-' }}
              </ADescriptionsItem>
              <ADescriptionsItem label="用户名">
                {{ userModel.username || '-' }}
              </ADescriptionsItem>
              <ADescriptionsItem label="注册时间">
                {{ authStore.userInfo.createTime || '-' }}
              </ADescriptionsItem>
              <ADescriptionsItem label="最后登录">
                {{ authStore.userInfo.lastLoginTime || '-' }}
              </ADescriptionsItem>
              <ADescriptionsItem label="登录次数">{{ authStore.userInfo.loginCount || 0 }} 次</ADescriptionsItem>
            </ADescriptions>
          </ACard>

          <ACard title="权限信息" size="small">
            <ADescriptions :column="1" bordered>
              <ADescriptionsItem label="用户角色">
                <ASpace wrap>
                  <ATag v-for="role in authStore.userInfo.roles" :key="role" color="blue">
                    {{ role }}
                  </ATag>
                  <span v-if="!authStore.userInfo.roles?.length" class="text-gray-400">暂无角色</span>
                </ASpace>
              </ADescriptionsItem>
            </ADescriptions>
          </ACard>
        </div>
      </ATabPane>
    </ATabs>
  </ADrawer>
</template>

<style scoped>
:deep(.ant-tabs-content-holder) {
  padding-top: 16px;
}

:deep(.ant-descriptions-item-label) {
  background-color: #fafafa;
  font-weight: 500;
}
</style>
