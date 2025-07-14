<template>
  <view class="page">
    <!-- 状态栏占位 -->
    <view class="status-bar"></view>

    <view class="container">
      <!-- Logo区域 -->
      <view class="logo-section">
        <view class="logo">
          <text class="logo-text">M</text>
        </view>
        <text class="app-name">Mars Admin</text>
      </view>

      <!-- 登录表单 -->
      <view class="login-form">
        <view class="input-item">
          <input
            v-model="form.username"
            class="input"
            placeholder="用户名"
            type="text"
          />
        </view>

        <view class="input-item">
          <input
            v-model="form.password"
            class="input"
            placeholder="密码"
            :type="showPassword ? 'text' : 'password'"
          />
          <view class="eye-icon" @click="togglePassword">
            <text>{{ showPassword ? '👁️' : '👁️‍🗨️' }}</text>
          </view>
        </view>

        <view class="form-options">
          <label class="remember-label">
            <checkbox v-model="rememberPassword" />
            <text>记住密码</text>
          </label>
          <text class="forget-link" @click="forgetPassword">忘记密码?</text>
        </view>

        <button class="login-btn" :disabled="loading" @click="login">
          {{ loading ? '登录中...' : '登录' }}
        </button>
      </view>

      <!-- 底部版权 -->
      <view class="footer">
        <text class="copyright">Copyright © 2024 Mars Admin</text>
      </view>
    </view>
  </view>
</template>

<script>
import { login as loginUser, isLoggedIn } from '@/utils/permission'
import { token, userInfo } from '@/utils/storage'

export default {
  data() {
    return {
      form: {
        username: 'admin',
        password: '123456'
      },
      showPassword: false,
      loading: false,
      rememberPassword: false
    }
  },
  onLoad() {
    // 检查是否有保存的账号密码
    this.checkSavedCredentials();
  },
  methods: {
    // 检查保存的登录凭据
    checkSavedCredentials() {
      const savedUsername = uni.getStorageSync('savedUsername');
      const savedPassword = uni.getStorageSync('savedPassword');

      if (savedUsername && savedPassword) {
        this.form.username = savedUsername;
        this.form.password = savedPassword;
        this.rememberPassword = true;
      }
    },

    // 切换密码显示状态
    togglePassword() {
      this.showPassword = !this.showPassword;
    },

    // 登录处理
    async login() {
      // 简单验证
      if (!this.form.username.trim()) {
        uni.showToast({
          title: '请输入用户名',
          icon: 'none'
        });
        return;
      }

      if (!this.form.password.trim()) {
        uni.showToast({
          title: '请输入密码',
          icon: 'none'
        });
        return;
      }

      this.loading = true;

      try {
        // 模拟登录成功，保存登录状态
        const mockUserData = {
          id: 1,
          username: this.form.username,
          nickname: '管理员',
          avatar: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?ixlib=rb-4.0.3&auto=format&fit=crop&w=200&q=80',
          roles: ['admin'],
          permissions: ['*:*:*']
        };

        const mockToken = `mock_token_${Date.now()}`;

        // 保存登录状态
        loginUser(mockUserData, mockToken);

        // 验证登录状态是否保存成功
        console.log('登录状态保存后验证:');
        console.log('Token:', token.get());
        console.log('UserInfo:', userInfo.get());
        console.log('IsLoggedIn:', isLoggedIn());

        // 如果记住密码，保存到本地
        if (this.rememberPassword) {
          uni.setStorageSync('savedUsername', this.form.username);
          uni.setStorageSync('savedPassword', this.form.password);
        } else {
          uni.removeStorageSync('savedUsername');
          uni.removeStorageSync('savedPassword');
        }

        this.loading = false;
        uni.showToast({
          title: '登录成功',
          icon: 'success'
        });

        // 跳转到首页
        setTimeout(() => {
          uni.switchTab({
            url: '/pages/index/index'
          });
        }, 1500);

      } catch (error) {
        this.loading = false;
        console.error('登录失败:', error);
        uni.showToast({
          title: '登录失败',
          icon: 'none'
        });
      }
    },
    // 忘记密码
    forgetPassword() {
      uni.showToast({
        title: '请联系管理员',
        icon: 'none'
      });
    }
  }
}
</script>

<style lang="scss" scoped>
.page {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.status-bar {
  height: 88rpx;
  background: transparent;
}

.container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  min-height: calc(100vh - 88rpx);
  padding: 40rpx;
  box-sizing: border-box;
}

.logo-section {
  text-align: center;
  margin-bottom: 80rpx;
}

.logo {
  width: 100rpx;
  height: 100rpx;
  background: #4CAF50;
  border-radius: 20rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 30rpx;
  box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.2);
}

.logo-text {
  font-size: 50rpx;
  font-weight: bold;
  color: white;
}

.app-name {
  font-size: 48rpx;
  font-weight: bold;
  color: white;
  text-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.3);
}

.login-form {
  width: 100%;
  max-width: 600rpx;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 24rpx;
  padding: 60rpx 40rpx;
  box-shadow: 0 20rpx 60rpx rgba(0, 0, 0, 0.15);
}

.input-item {
  margin-bottom: 30rpx;
  position: relative;
}

.input {
  width: 100%;
  height: 88rpx;
  background: #f8f9fa;
  border: 2rpx solid #e9ecef;
  border-radius: 12rpx;
  padding: 0 30rpx;
  font-size: 32rpx;
  color: #333;
  box-sizing: border-box;
}

.input:focus {
  border-color: #667eea;
  background: white;
}

.eye-icon {
  position: absolute;
  right: 30rpx;
  top: 50%;
  transform: translateY(-50%);
  font-size: 32rpx;
}

.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 40rpx;
  font-size: 28rpx;
}

.remember-label {
  display: flex;
  align-items: center;
  color: #666;
}

.remember-label checkbox {
  margin-right: 10rpx;
}

.forget-link {
  color: #667eea;
}

.login-btn {
  width: 100%;
  height: 88rpx;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  border-radius: 12rpx;
  color: white;
  font-size: 32rpx;
  font-weight: bold;
}

.login-btn:disabled {
  opacity: 0.6;
}

.footer {
  position: absolute;
  bottom: 40rpx;
  left: 0;
  right: 0;
  text-align: center;
}

.copyright {
  font-size: 24rpx;
  color: rgba(255, 255, 255, 0.8);
}
</style>