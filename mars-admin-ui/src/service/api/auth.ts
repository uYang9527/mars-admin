import { request } from '../request';

/**
 * 登录
 *
 * @param username 用户名
 * @param password 密码
 */
export function fetchLogin(username: string, password: string) {
  return request<Api.Auth.LoginToken>({
    url: '/auth/login',
    method: 'post',
    data: {
      username,
      password
    }
  });
}

/** 获取用户信息 */
export function fetchGetUserInfo() {
  return request<Api.Auth.UserInfo>({
    url: '/auth/userinfo',
    method: 'get'
  });
}

/**
 * 刷新token
 *
 * @param refreshToken 刷新token
 */
export function fetchRefreshToken(refreshToken: string) {
  return request<Api.Auth.LoginToken>({
    url: '/auth/refresh',
    method: 'post',
    data: {
      refreshToken
    }
  });
}

/** 退出登录 */
export function fetchLogout() {
  return request({
    url: '/auth/logout',
    method: 'post'
  });
}

/** 获取验证码 */
export function fetchCaptcha() {
  return request<Api.Auth.CaptchaData>({
    url: '/auth/captcha',
    method: 'get'
  });
}

/**
 * 修改密码
 *
 * @param oldPassword 旧密码
 * @param newPassword 新密码
 */
export function fetchChangePassword(oldPassword: string, newPassword: string) {
  return request({
    url: '/auth/changePassword',
    method: 'post',
    data: {
      oldPassword,
      newPassword
    }
  });
}

/**
 * return custom backend error
 *
 * @param code error code
 * @param msg error message
 */
export function fetchCustomBackendError(code: string, msg: string) {
  return request({ url: '/auth/error', params: { code, msg } });
}
