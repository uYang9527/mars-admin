<template>
  <div class="page-container">
    <n-card class="config-card">
      <!-- Tab 导航 -->
      <n-tabs v-model:value="activeTab" type="line" animated @update:value="handleTabChange">
        <n-tab-pane v-for="group in configGroups" :key="group.groupCode" :name="group.groupCode" :tab="group.groupName">
          <div class="config-content">
            <!-- 系统配置 -->
            <template v-if="group.groupCode === 'system'">
              <n-form :model="configs.system" label-placement="left" label-width="120">
                <n-form-item label="站点名称">
                  <n-input v-model:value="configs.system.siteName" placeholder="请输入站点名称" />
                </n-form-item>
                <n-form-item label="站点描述">
                  <n-input v-model:value="configs.system.siteDescription" type="textarea" placeholder="请输入站点描述" />
                </n-form-item>
                <n-form-item label="站点Logo">
                  <div class="logo-upload">
                    <div class="logo-preview" v-if="configs.system.siteLogo">
                      <img :src="configs.system.siteLogo" alt="Logo" />
                      <n-button size="small" quaternary type="error" class="logo-delete" @click="configs.system.siteLogo = ''">
                        <template #icon><n-icon><CloseOutline /></n-icon></template>
                      </n-button>
                    </div>
                    <n-upload
                      v-else
                      :custom-request="handleLogoUpload"
                      :show-file-list="false"
                      accept="image/*"
                    >
                      <div class="logo-upload-trigger">
                        <n-icon size="24"><ImageOutline /></n-icon>
                        <span>点击上传Logo</span>
                      </div>
                    </n-upload>
                    <n-input v-model:value="configs.system.siteLogo" placeholder="或输入Logo地址" style="margin-top: 8px" />
                  </div>
                </n-form-item>
                <n-form-item label="版权信息">
                  <n-input v-model:value="configs.system.copyright" placeholder="请输入版权信息" />
                </n-form-item>
                <n-form-item label="ICP备案号">
                  <n-input v-model:value="configs.system.icp" placeholder="请输入ICP备案号" />
                </n-form-item>
              </n-form>
            </template>

            <!-- 注册配置 -->
            <template v-else-if="group.groupCode === 'register'">
              <n-form :model="configs.register" label-placement="left" label-width="120">
                <n-form-item label="开放注册">
                  <n-switch v-model:value="configs.register.enabled" />
                </n-form-item>
                <n-form-item label="邮箱验证">
                  <n-switch v-model:value="configs.register.verifyEmail" />
                </n-form-item>
                <n-form-item label="手机验证">
                  <n-switch v-model:value="configs.register.verifyPhone" />
                </n-form-item>
                <n-form-item label="默认角色">
                  <n-input v-model:value="configs.register.defaultRole" placeholder="请输入默认角色编码" />
                </n-form-item>
                <n-form-item label="需要审核">
                  <n-switch v-model:value="configs.register.needAudit" />
                </n-form-item>
              </n-form>
            </template>

            <!-- 登录配置 -->
            <template v-else-if="group.groupCode === 'login'">
              <n-form :model="configs.login" label-placement="left" label-width="120">
                <n-form-item label="验证码">
                  <n-switch v-model:value="configs.login.captchaEnabled" />
                </n-form-item>
                <n-form-item label="验证码类型" v-if="configs.login.captchaEnabled">
                  <n-select v-model:value="configs.login.captchaType" :options="captchaTypeOptions" style="width: 200px" />
                </n-form-item>
                <n-form-item label="最大重试次数">
                  <n-input-number v-model:value="configs.login.maxRetryCount" :min="1" :max="10" />
                </n-form-item>
                <n-form-item label="锁定时间(分钟)">
                  <n-input-number v-model:value="configs.login.lockTime" :min="1" :max="1440" />
                </n-form-item>
                <n-form-item label="记住我">
                  <n-switch v-model:value="configs.login.rememberMe" />
                </n-form-item>

              </n-form>
            </template>

            <!-- 密码配置 -->
            <template v-else-if="group.groupCode === 'password'">
              <n-form :model="configs.password" label-placement="left" label-width="150">
                <n-form-item label="最小长度">
                  <n-input-number v-model:value="configs.password.minLength" :min="4" :max="32" />
                </n-form-item>
                <n-form-item label="最大长度">
                  <n-input-number v-model:value="configs.password.maxLength" :min="6" :max="64" />
                </n-form-item>
                <n-form-item label="必须包含大写字母">
                  <n-switch v-model:value="configs.password.requireUppercase" />
                </n-form-item>
                <n-form-item label="必须包含小写字母">
                  <n-switch v-model:value="configs.password.requireLowercase" />
                </n-form-item>
                <n-form-item label="必须包含数字">
                  <n-switch v-model:value="configs.password.requireNumber" />
                </n-form-item>
                <n-form-item label="必须包含特殊字符">
                  <n-switch v-model:value="configs.password.requireSpecial" />
                </n-form-item>
                <n-form-item label="密码过期天数">
                  <n-input-number v-model:value="configs.password.expireDays" :min="0" :max="365" />
                  <span class="form-hint">0表示永不过期</span>
                </n-form-item>
              </n-form>
            </template>

            <!-- 邮件配置 -->
            <template v-else-if="group.groupCode === 'email'">
              <n-form :model="configs.email" label-placement="left" label-width="120">
                <n-form-item label="启用邮件">
                  <n-switch v-model:value="configs.email.enabled" />
                </n-form-item>
                <n-form-item label="SMTP服务器">
                  <n-input v-model:value="configs.email.host" placeholder="如: smtp.qq.com" />
                </n-form-item>
                <n-form-item label="端口">
                  <n-input-number v-model:value="configs.email.port" :min="1" :max="65535" />
                </n-form-item>
                <n-form-item label="用户名">
                  <n-input v-model:value="configs.email.username" placeholder="发件人邮箱" />
                </n-form-item>
                <n-form-item label="密码/授权码">
                  <n-input v-model:value="configs.email.password" type="password" show-password-on="click" placeholder="邮箱密码或授权码" />
                </n-form-item>
                <n-form-item label="发件人名称">
                  <n-input v-model:value="configs.email.fromName" placeholder="显示的发件人名称" />
                </n-form-item>
                <n-form-item label="SSL加密">
                  <n-switch v-model:value="configs.email.ssl" />
                </n-form-item>
                <n-divider />
                <n-form-item label="测试邮件">
                  <n-input-group>
                    <n-input v-model:value="testEmailAddress" placeholder="输入收件人邮箱" style="width: 280px" />
                    <n-button type="primary" @click="handleTestEmail" :loading="emailTesting" :disabled="!configs.email.enabled">
                      发送测试邮件
                    </n-button>
                  </n-input-group>
                </n-form-item>
              </n-form>
            </template>

            <!-- 邮件模板 -->
            <template v-else-if="group.groupCode === 'emailTemplate'">
              <n-form :model="configs.emailTemplate" label-placement="top">
                <n-form-item label="验证码邮件模板">
                  <n-input v-model:value="configs.emailTemplate.verifyCode" type="textarea" :rows="3" placeholder="支持变量: {code}, {expire}" />
                </n-form-item>
                <n-form-item label="重置密码邮件模板">
                  <n-input v-model:value="configs.emailTemplate.resetPassword" type="textarea" :rows="3" placeholder="支持变量: {code}, {expire}" />
                </n-form-item>
                <n-form-item label="欢迎邮件模板">
                  <n-input v-model:value="configs.emailTemplate.welcome" type="textarea" :rows="3" placeholder="支持变量: {siteName}, {username}" />
                </n-form-item>
              </n-form>
            </template>

            <!-- 短信配置 -->
            <template v-else-if="group.groupCode === 'sms'">
              <n-form :model="configs.sms" label-placement="left" label-width="120">
                <n-form-item label="启用短信">
                  <n-switch v-model:value="configs.sms.enabled" />
                </n-form-item>
                <n-form-item label="短信服务商">
                  <n-select v-model:value="configs.sms.provider" :options="smsProviderOptions" style="width: 200px" />
                </n-form-item>
                <n-form-item label="AccessKeyId">
                  <n-input v-model:value="configs.sms.accessKeyId" placeholder="请输入AccessKeyId" />
                </n-form-item>
                <n-form-item label="AccessKeySecret">
                  <n-input v-model:value="configs.sms.accessKeySecret" type="password" show-password-on="click" placeholder="请输入AccessKeySecret" />
                </n-form-item>
                <n-form-item label="签名">
                  <n-input v-model:value="configs.sms.signName" placeholder="短信签名" />
                </n-form-item>
              </n-form>
            </template>

            <!-- 短信模板 -->
            <template v-else-if="group.groupCode === 'smsTemplate'">
              <n-form :model="configs.smsTemplate" label-placement="left" label-width="150">
                <n-form-item label="验证码模板ID">
                  <n-input v-model:value="configs.smsTemplate.verifyCode" placeholder="请输入短信模板ID" />
                </n-form-item>
                <n-form-item label="重置密码模板ID">
                  <n-input v-model:value="configs.smsTemplate.resetPassword" placeholder="请输入短信模板ID" />
                </n-form-item>
                <n-form-item label="通知模板ID">
                  <n-input v-model:value="configs.smsTemplate.notification" placeholder="请输入短信模板ID" />
                </n-form-item>
              </n-form>
            </template>

            <!-- 文件配置 -->
            <template v-else-if="group.groupCode === 'storage'">
              <n-form :model="configs.storage" label-placement="left" label-width="150">
                <n-form-item label="存储方式">
                  <n-select v-model:value="configs.storage.provider" :options="storageProviderOptions" style="width: 200px" />
                </n-form-item>
                <n-form-item label="访问域名">
                  <n-input v-model:value="configs.storage.domain" placeholder="如: http://localhost:8080" />
                </n-form-item>
                <n-form-item label="最大文件大小(MB)">
                  <n-input-number v-model:value="configs.storage.maxSize" :min="1" :max="1024" />
                </n-form-item>
                <n-form-item label="允许的文件类型">
                  <n-input v-model:value="configs.storage.allowTypes" placeholder="如: jpg,png,pdf,doc,docx,xls,xlsx" />
                </n-form-item>
                
                <!-- 本地存储配置 -->
                <template v-if="configs.storage.provider === 'local'">
                  <n-divider>本地存储配置</n-divider>
                  <n-form-item label="存储路径">
                    <n-input v-model:value="configs.storage.localPath" placeholder="如: ./uploads" />
                  </n-form-item>
                </template>
                
                <!-- MinIO配置 -->
                <template v-if="configs.storage.provider === 'minio'">
                  <n-divider>MinIO配置</n-divider>
                  <n-form-item label="服务端点">
                    <n-input v-model:value="configs.storage.minioEndpoint" placeholder="如: http://localhost:9000" />
                  </n-form-item>
                  <n-form-item label="AccessKey">
                    <n-input v-model:value="configs.storage.minioAccessKey" placeholder="请输入AccessKey" />
                  </n-form-item>
                  <n-form-item label="SecretKey">
                    <n-input v-model:value="configs.storage.minioSecretKey" type="password" show-password-on="click" placeholder="请输入SecretKey" />
                  </n-form-item>
                  <n-form-item label="存储桶名称">
                    <n-input v-model:value="configs.storage.minioBucket" placeholder="请输入存储桶名称" />
                  </n-form-item>
                </template>
                
                <!-- 阿里云OSS配置 -->
                <template v-if="configs.storage.provider === 'aliyun'">
                  <n-divider>阿里云OSS配置</n-divider>
                  <n-form-item label="Endpoint">
                    <n-input v-model:value="configs.storage.aliyunEndpoint" placeholder="如: https://oss-cn-hangzhou.aliyuncs.com" />
                  </n-form-item>
                  <n-form-item label="AccessKeyId">
                    <n-input v-model:value="configs.storage.aliyunAccessKey" placeholder="请输入AccessKeyId" />
                  </n-form-item>
                  <n-form-item label="AccessKeySecret">
                    <n-input v-model:value="configs.storage.aliyunSecretKey" type="password" show-password-on="click" placeholder="请输入AccessKeySecret" />
                  </n-form-item>
                  <n-form-item label="存储桶名称">
                    <n-input v-model:value="configs.storage.aliyunBucket" placeholder="请输入Bucket名称" />
                  </n-form-item>
                </template>
                
                <!-- 腾讯云COS配置 -->
                <template v-if="configs.storage.provider === 'tencent'">
                  <n-divider>腾讯云COS配置</n-divider>
                  <n-form-item label="SecretId">
                    <n-input v-model:value="configs.storage.tencentSecretId" placeholder="请输入SecretId" />
                  </n-form-item>
                  <n-form-item label="SecretKey">
                    <n-input v-model:value="configs.storage.tencentSecretKey" type="password" show-password-on="click" placeholder="请输入SecretKey" />
                  </n-form-item>
                  <n-form-item label="存储桶名称">
                    <n-input v-model:value="configs.storage.tencentBucket" placeholder="请输入Bucket名称" />
                  </n-form-item>
                  <n-form-item label="地域">
                    <n-input v-model:value="configs.storage.tencentRegion" placeholder="如: ap-guangzhou" />
                  </n-form-item>
                </template>
              </n-form>
            </template>

            <!-- 推送配置 -->
            <template v-else-if="group.groupCode === 'push'">
              <n-form :model="configs.push" label-placement="left" label-width="120">
                <n-form-item label="启用推送">
                  <n-switch v-model:value="configs.push.enabled" />
                </n-form-item>
                <n-form-item label="推送服务商">
                  <n-select v-model:value="configs.push.provider" :options="pushProviderOptions" style="width: 200px" />
                </n-form-item>
                <n-form-item label="AppKey">
                  <n-input v-model:value="configs.push.appKey" placeholder="请输入AppKey" />
                </n-form-item>
                <n-form-item label="MasterSecret">
                  <n-input v-model:value="configs.push.masterSecret" type="password" show-password-on="click" placeholder="请输入MasterSecret" />
                </n-form-item>
              </n-form>
            </template>

            <!-- 第三方配置 -->
            <template v-else-if="group.groupCode === 'thirdParty'">
              <n-collapse>
                <n-collapse-item title="微信登录" name="wechat">
                  <n-form :model="configs.thirdParty.wechat" label-placement="left" label-width="100">
                    <n-form-item label="启用">
                      <n-switch v-model:value="configs.thirdParty.wechat.enabled" />
                    </n-form-item>
                    <n-form-item label="AppID">
                      <n-input v-model:value="configs.thirdParty.wechat.appId" placeholder="请输入AppID" />
                    </n-form-item>
                    <n-form-item label="AppSecret">
                      <n-input v-model:value="configs.thirdParty.wechat.appSecret" type="password" show-password-on="click" />
                    </n-form-item>
                  </n-form>
                </n-collapse-item>
                <n-collapse-item title="支付宝登录" name="alipay">
                  <n-form :model="configs.thirdParty.alipay" label-placement="left" label-width="100">
                    <n-form-item label="启用">
                      <n-switch v-model:value="configs.thirdParty.alipay.enabled" />
                    </n-form-item>
                    <n-form-item label="AppID">
                      <n-input v-model:value="configs.thirdParty.alipay.appId" placeholder="请输入AppID" />
                    </n-form-item>
                    <n-form-item label="私钥">
                      <n-input v-model:value="configs.thirdParty.alipay.privateKey" type="textarea" :rows="2" />
                    </n-form-item>
                    <n-form-item label="公钥">
                      <n-input v-model:value="configs.thirdParty.alipay.publicKey" type="textarea" :rows="2" />
                    </n-form-item>
                  </n-form>
                </n-collapse-item>
                <n-collapse-item title="GitHub登录" name="github">
                  <n-form :model="configs.thirdParty.github" label-placement="left" label-width="100">
                    <n-form-item label="启用">
                      <n-switch v-model:value="configs.thirdParty.github.enabled" />
                    </n-form-item>
                    <n-form-item label="Client ID">
                      <n-input v-model:value="configs.thirdParty.github.clientId" placeholder="请输入Client ID" />
                    </n-form-item>
                    <n-form-item label="Client Secret">
                      <n-input v-model:value="configs.thirdParty.github.clientSecret" type="password" show-password-on="click" />
                    </n-form-item>
                  </n-form>
                </n-collapse-item>
              </n-collapse>
            </template>

            <!-- 支付配置 -->
            <template v-else-if="group.groupCode === 'payment'">
              <n-collapse>
                <n-collapse-item title="微信支付" name="wechatPay">
                  <n-form :model="configs.payment.wechatPay" label-placement="left" label-width="120">
                    <n-form-item label="启用">
                      <n-switch v-model:value="configs.payment.wechatPay.enabled" />
                    </n-form-item>
                    <n-form-item label="商户号">
                      <n-input v-model:value="configs.payment.wechatPay.mchId" placeholder="请输入商户号" />
                    </n-form-item>
                    <n-form-item label="AppID">
                      <n-input v-model:value="configs.payment.wechatPay.appId" placeholder="请输入AppID" />
                    </n-form-item>
                    <n-form-item label="APIv3密钥">
                      <n-input v-model:value="configs.payment.wechatPay.apiV3Key" type="password" show-password-on="click" placeholder="请输入APIv3密钥" />
                    </n-form-item>
                    <n-form-item label="商户私钥">
                      <n-input v-model:value="configs.payment.wechatPay.privateKey" type="textarea" :rows="3" placeholder="请输入商户私钥" />
                    </n-form-item>
                    <n-form-item label="证书序列号">
                      <n-input v-model:value="configs.payment.wechatPay.certSerialNo" placeholder="请输入证书序列号" />
                    </n-form-item>
                    <n-form-item label="回调地址">
                      <n-input v-model:value="configs.payment.wechatPay.notifyUrl" placeholder="请输入支付回调地址" />
                    </n-form-item>
                    <n-form-item label="测试支付" v-if="configs.payment.wechatPay.enabled">
                      <n-button type="primary" @click="testPayment('wechat')" :loading="paymentTesting">
                        生成测试订单
                      </n-button>
                    </n-form-item>
                  </n-form>
                </n-collapse-item>
                <n-collapse-item title="支付宝支付" name="alipay">
                  <n-form :model="configs.payment.alipay" label-placement="left" label-width="120">
                    <n-form-item label="启用">
                      <n-switch v-model:value="configs.payment.alipay.enabled" />
                    </n-form-item>
                    <n-form-item label="AppID">
                      <n-input v-model:value="configs.payment.alipay.appId" placeholder="请输入AppID" />
                    </n-form-item>
                    <n-form-item label="应用私钥">
                      <n-input v-model:value="configs.payment.alipay.privateKey" type="textarea" :rows="3" placeholder="请输入应用私钥" />
                    </n-form-item>
                    <n-form-item label="支付宝公钥">
                      <n-input v-model:value="configs.payment.alipay.publicKey" type="textarea" :rows="3" placeholder="请输入支付宝公钥" />
                    </n-form-item>
                    <n-form-item label="签名类型">
                      <n-select v-model:value="configs.payment.alipay.signType" :options="[{label:'RSA2',value:'RSA2'},{label:'RSA',value:'RSA'}]" style="width: 150px" />
                    </n-form-item>
                    <n-form-item label="网关地址">
                      <n-select v-model:value="configs.payment.alipay.gatewayUrl" :options="alipayGatewayOptions" />
                    </n-form-item>
                    <n-form-item label="回调地址">
                      <n-input v-model:value="configs.payment.alipay.notifyUrl" placeholder="请输入支付回调地址" />
                    </n-form-item>
                    <n-form-item label="测试支付" v-if="configs.payment.alipay.enabled">
                      <n-button type="primary" @click="testPayment('alipay')" :loading="paymentTesting">
                        生成测试订单
                      </n-button>
                    </n-form-item>
                  </n-form>
                </n-collapse-item>
              </n-collapse>
              
              <!-- 测试支付弹窗 -->
              <n-modal v-model:show="showPaymentModal" preset="card" title="测试支付" style="width: 400px">
                <div class="payment-test-modal">
                  <div class="payment-info">
                    <p>支付方式：{{ paymentResult.type === 'wechat' ? '微信支付' : '支付宝' }}</p>
                    <p>订单号：{{ paymentResult.orderNo }}</p>
                    <p>金额：<span class="amount">¥ 0.01</span></p>
                  </div>
                  <div class="qrcode-container" v-if="paymentResult.qrcode">
                    <img :src="paymentResult.qrcode" alt="支付二维码" class="qrcode-img" />
                    <p class="qrcode-tip">请使用{{ paymentResult.type === 'wechat' ? '微信' : '支付宝' }}扫码支付</p>
                  </div>
                  <div class="payment-link" v-if="paymentResult.payUrl && paymentResult.type === 'alipay'">
                    <n-button type="primary" tag="a" :href="paymentResult.payUrl" target="_blank">
                      点击跳转支付
                    </n-button>
                  </div>
                </div>
              </n-modal>
            </template>

            <!-- 安全配置 -->
            <template v-else-if="group.groupCode === 'security'">
              <n-form :model="configs.security" label-placement="left" label-width="150">
                <n-form-item label="接口加密">
                  <n-switch v-model:value="configs.security.encryptEnabled" @update:value="handleEncryptChange" />
                </n-form-item>
                <template v-if="configs.security.encryptEnabled">
                  <n-form-item label="加密范围">
                    <n-radio-group v-model:value="configs.security.encryptScope">
                      <n-space>
                        <n-radio value="partial">
                          <span>部分加密</span>
                          <span class="form-hint">（仅加密带 @EncryptResponse 注解的接口）</span>
                        </n-radio>
                        <n-radio value="global">
                          <span>全局加密</span>
                          <span class="form-hint">（所有接口返回都加密）</span>
                        </n-radio>
                      </n-space>
                    </n-radio-group>
                  </n-form-item>
                  <n-form-item label="RSA公钥">
                    <n-input v-model:value="configs.security.encryptPublicKey" type="textarea" :rows="3" readonly placeholder="点击下方按钮生成密钥" />
                  </n-form-item>
                  <n-form-item label="RSA私钥">
                    <n-input v-model:value="configs.security.encryptPrivateKey" type="textarea" :rows="3" readonly placeholder="点击下方按钮生成密钥" />
                  </n-form-item>
                  <n-form-item label="生成密钥">
                    <n-button type="primary" @click="handleGenerateKeys" :loading="generatingKeys">
                      {{ configs.security.encryptPublicKey ? '重新生成密钥' : '生成RSA密钥对' }}
                    </n-button>
                    <span class="form-hint" v-if="!configs.security.encryptPublicKey" style="color: #f5222d">
                      请先生成密钥才能使用接口加密功能
                    </span>
                  </n-form-item>
                </template>
                <n-form-item label="XSS过滤">
                  <n-switch v-model:value="configs.security.xssFilter" />
                </n-form-item>
                <n-form-item label="SQL注入防护">
                  <n-switch v-model:value="configs.security.sqlInject" />
                </n-form-item>
              </n-form>
            </template>

            <!-- 其他配置 -->
            <template v-else-if="group.groupCode === 'other'">
              <n-empty description="暂无其他配置项" />
            </template>

            <!-- 保存按钮 -->
            <div class="config-footer">
              <n-button type="primary" :loading="saving" @click="handleSave">
                保存配置
              </n-button>
              <n-button @click="handleRefresh" style="margin-left: 12px">
                刷新缓存
              </n-button>
            </div>
          </div>
        </n-tab-pane>
      </n-tabs>
    </n-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useMessage, type UploadCustomRequestOptions } from 'naive-ui'
import { ImageOutline, CloseOutline } from '@vicons/ionicons5'
import { configGroupApi, type SysConfigGroup } from '@/api/org'
import { fileApi } from '@/api/system'
import { useSiteStore } from '@/stores/site'

const message = useMessage()
const siteStore = useSiteStore()

// 配置分组列表
const configGroups = ref<SysConfigGroup[]>([])
const activeTab = ref('system')
const saving = ref(false)

// 所有配置数据
const configs = reactive<Record<string, any>>({
  system: { siteName: '', siteDescription: '', siteLogo: '', copyright: '', icp: '' },
  register: { enabled: true, verifyEmail: false, verifyPhone: false, defaultRole: 'user', needAudit: false },
  login: { captchaEnabled: false, captchaType: 'image', maxRetryCount: 5, lockTime: 30, rememberMe: true, singleLogin: false },
  password: { minLength: 6, maxLength: 20, requireUppercase: false, requireLowercase: false, requireNumber: false, requireSpecial: false, expireDays: 0 },
  email: { host: '', port: 465, username: '', password: '', fromName: '', ssl: true, enabled: false },
  emailTemplate: { verifyCode: '', resetPassword: '', welcome: '' },
  sms: { provider: 'aliyun', accessKeyId: '', accessKeySecret: '', signName: '', enabled: false },
  smsTemplate: { verifyCode: '', resetPassword: '', notification: '' },
  storage: { 
    provider: 'local', 
    domain: 'http://localhost:8080',
    localPath: './uploads', 
    maxSize: 10, 
    allowTypes: 'jpg,jpeg,png,gif,pdf,doc,docx,xls,xlsx',
    // MinIO配置
    minioEndpoint: '',
    minioAccessKey: '',
    minioSecretKey: '',
    minioBucket: '',
    // 阿里云OSS配置
    aliyunEndpoint: '',
    aliyunAccessKey: '',
    aliyunSecretKey: '',
    aliyunBucket: '',
    // 腾讯云COS配置
    tencentSecretId: '',
    tencentSecretKey: '',
    tencentBucket: '',
    tencentRegion: ''
  },
  push: { enabled: false, provider: 'jpush', appKey: '', masterSecret: '' },
  thirdParty: {
    wechat: { enabled: false, appId: '', appSecret: '' },
    alipay: { enabled: false, appId: '', privateKey: '', publicKey: '' },
    github: { enabled: false, clientId: '', clientSecret: '' }
  },
  payment: {
    wechatPay: { enabled: false, mchId: '', appId: '', apiV3Key: '', privateKey: '', certSerialNo: '', notifyUrl: '' },
    alipay: { enabled: false, appId: '', privateKey: '', publicKey: '', signType: 'RSA2', gatewayUrl: 'https://openapi.alipay.com/gateway.do', notifyUrl: '', returnUrl: '' }
  },
  security: { encryptEnabled: false, encryptScope: 'partial', encryptPublicKey: '', encryptPrivateKey: '', xssFilter: true, sqlInject: true },
  other: {}
})

// 选项数据
const captchaTypeOptions = [
  { label: '图片验证码', value: 'image' },
  { label: '滑块验证码', value: 'slider' },
  { label: '短信验证码', value: 'sms' }
]

const smsProviderOptions = [
  { label: '阿里云', value: 'aliyun' },
  { label: '腾讯云', value: 'tencent' },
  { label: '七牛云', value: 'qiniu' }
]

const storageProviderOptions = [
  { label: '本地存储', value: 'local' },
  { label: 'MinIO', value: 'minio' },
  { label: '阿里云OSS', value: 'aliyun' },
  { label: '腾讯云COS', value: 'tencent' }
]

const pushProviderOptions = [
  { label: '极光推送', value: 'jpush' },
  { label: '友盟推送', value: 'umeng' },
  { label: '个推', value: 'getui' }
]

const alipayGatewayOptions = [
  { label: '正式环境', value: 'https://openapi.alipay.com/gateway.do' },
  { label: '沙箱环境', value: 'https://openapi-sandbox.dl.alipaydev.com/gateway.do' }
]

// 邮件测试相关
const emailTesting = ref(false)
const testEmailAddress = ref('')

// 密钥生成相关
const generatingKeys = ref(false)

// 开启加密时检查是否有密钥
function handleEncryptChange(enabled: boolean) {
  if (enabled && !configs.security.encryptPublicKey) {
    message.warning('请生成RSA密钥对后再保存配置')
  }
}

// 生成RSA密钥对
async function handleGenerateKeys() {
  generatingKeys.value = true
  try {
    const keys = await configGroupApi.generateKeys()
    configs.security.encryptPublicKey = keys.publicKey
    configs.security.encryptPrivateKey = keys.privateKey
    message.success('密钥生成成功，请点击保存配置')
  } catch (error: any) {
    message.error(error.message || '生成密钥失败')
  } finally {
    generatingKeys.value = false
  }
}

// 测试发送邮件
async function handleTestEmail() {
  if (!testEmailAddress.value) {
    message.warning('请输入收件人邮箱')
    return
  }
  
  // 简单的邮箱格式验证
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  if (!emailRegex.test(testEmailAddress.value)) {
    message.warning('请输入正确的邮箱格式')
    return
  }
  
  emailTesting.value = true
  try {
    await configGroupApi.testEmail(testEmailAddress.value)
    message.success('测试邮件发送成功，请查收')
  } catch (error: any) {
    message.error(error.message || '发送测试邮件失败')
  } finally {
    emailTesting.value = false
  }
}

// 支付测试相关
const paymentTesting = ref(false)
const showPaymentModal = ref(false)
const paymentResult = ref<{
  type: string
  orderNo: string
  qrcode: string
  payUrl: string
}>({
  type: '',
  orderNo: '',
  qrcode: '',
  payUrl: ''
})

// 测试支付
async function testPayment(type: 'wechat' | 'alipay') {
  paymentTesting.value = true
  try {
    const result = await configGroupApi.testPayment(type)
    paymentResult.value = {
      type,
      orderNo: result.orderNo,
      qrcode: result.qrcode || '',
      payUrl: result.payUrl || ''
    }
    showPaymentModal.value = true
  } catch (error: any) {
    message.error(error.message || '创建测试订单失败')
  } finally {
    paymentTesting.value = false
  }
}

// 加载配置分组
async function loadGroups() {
  try {
    configGroups.value = await configGroupApi.list()
    if (configGroups.value.length > 0) {
      activeTab.value = configGroups.value[0].groupCode
      await loadConfig(activeTab.value)
    }
  } catch (error) {
    // 错误已在拦截器处理
  }
}

// 深度合并对象（保持响应性）
function deepMergeReactive(target: any, source: any): void {
  for (const key in source) {
    if (source[key] !== null && typeof source[key] === 'object' && !Array.isArray(source[key])) {
      if (!target[key] || typeof target[key] !== 'object') {
        target[key] = {}
      }
      deepMergeReactive(target[key], source[key])
    } else {
      target[key] = source[key]
    }
  }
}

// 加载指定分组配置
async function loadConfig(groupCode: string) {
  try {
    const group = await configGroupApi.getByCode(groupCode)
    if (group && group.configValue) {
      const value = JSON.parse(group.configValue)
      // 使用深度合并，保持响应性
      if (!configs[groupCode]) {
        configs[groupCode] = {}
      }
      deepMergeReactive(configs[groupCode], value)
    }
  } catch (error) {
    console.error('加载配置失败:', error)
  }
}

// Tab 切换
async function handleTabChange(tab: string) {
  await loadConfig(tab)
}

// 保存配置
async function handleSave() {
  saving.value = true
  try {
    await configGroupApi.save(activeTab.value, configs[activeTab.value])
    message.success('保存成功')

    // 如果是系统配置，刷新站点信息
    if (activeTab.value === 'system') {
      await siteStore.loadConfig()
    }
  } catch (error) {
    // 错误已在拦截器处理
  } finally {
    saving.value = false
  }
}

// 刷新缓存
async function handleRefresh() {
  try {
    await configGroupApi.refresh()
    await siteStore.loadConfig()
    message.success('缓存刷新成功')
  } catch (error) {
    // 错误已在拦截器处理
  }
}

// Logo 上传
async function handleLogoUpload(options: UploadCustomRequestOptions) {
  const { file, onFinish, onError } = options
  try {
    const result = await fileApi.upload(file.file as File)
    configs.system.siteLogo = result.url
    message.success('Logo上传成功')
    onFinish()
  } catch (error) {
    message.error('Logo上传失败')
    onError()
  }
}

onMounted(() => {
  loadGroups()
})
</script>

<style scoped>
.config-card {
  min-height: calc(100vh - 120px);
}

.config-content {
  padding: 20px 0;
  max-width: 800px;
}

.config-footer {
  margin-top: 32px;
  padding-top: 20px;
  border-top: 1px solid #e5e7eb;
}

.form-hint {
  margin-left: 12px;
  color: #9ca3af;
  font-size: 13px;
}

:deep(.n-tabs-nav) {
  padding: 0 16px;
}

:deep(.n-tab-pane) {
  padding: 0 16px;
}

:deep(.n-collapse) {
  margin-bottom: 16px;
}

.logo-upload {
  width: 100%;
}

.logo-preview {
  position: relative;
  display: inline-block;

  img {
    max-width: 200px;
    max-height: 80px;
    object-fit: contain;
    border: 1px solid #e5e7eb;
    border-radius: 8px;
    padding: 8px;
    background: #f9fafb;
  }

  .logo-delete {
    position: absolute;
    top: -8px;
    right: -8px;
  }
}

.logo-upload-trigger {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 200px;
  height: 80px;
  border: 2px dashed #e5e7eb;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s;
  color: #9ca3af;

  &:hover {
    border-color: #111827;
    color: #111827;
  }

  span {
    margin-top: 8px;
    font-size: 13px;
  }
}

:deep(.n-collapse-item__header-main) {
  font-weight: 500;
}

.payment-test-modal {
  text-align: center;
  
  .payment-info {
    margin-bottom: 20px;
    text-align: left;
    padding: 16px;
    background: #f9fafb;
    border-radius: 8px;
    
    p {
      margin: 8px 0;
      color: #374151;
    }
    
    .amount {
      font-size: 24px;
      font-weight: 600;
      color: #ef4444;
    }
  }
  
  .qrcode-container {
    padding: 20px;
    background: #fff;
    border: 1px solid #e5e7eb;
    border-radius: 8px;
    display: inline-block;
    
    .qrcode-img {
      width: 200px;
      height: 200px;
    }
    
    .qrcode-tip {
      margin-top: 12px;
      color: #6b7280;
      font-size: 14px;
    }
  }
  
  .payment-link {
    margin-top: 20px;
  }
}
</style>
