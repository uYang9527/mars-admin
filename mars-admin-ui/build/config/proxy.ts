import type { ProxyOptions } from 'vite';
import { createServiceConfig } from '../../src/utils/service';

/**
 * Set http proxy
 *
 * @param env - The current env
 * @param enable - If enable http proxy
 */
export function createViteProxy(env: Env.ImportMeta, enable: boolean) {
  const isEnableHttpProxy = enable && env.VITE_HTTP_PROXY === 'Y';

  if (!isEnableHttpProxy) return undefined;

  // 直接配置代理规则，不依赖 createServiceConfig
  const proxy: Record<string, ProxyOptions> = {
    '/proxy-default': {
      target: 'http://localhost:8080',  // 实际的后端地址
      changeOrigin: true,
      rewrite: path => path.replace(/^\/proxy-default/, ''),
      timeout: 30000,
      configure: (proxy, options) => {
        proxy.on('error', (err, req, res) => {
          console.log('代理错误:', err);
        });
        proxy.on('proxyReq', (proxyReq, req, res) => {
          console.log('代理请求:', req.method, req.url);
        });
        proxy.on('proxyRes', (proxyRes, req, res) => {
          console.log('代理响应:', proxyRes.statusCode, req.url);
        });
      }
    }
  };

  return proxy;
}

function createProxyItem(item: App.Service.ServiceConfigItem) {
  const proxy: Record<string, ProxyOptions> = {};

  proxy[item.proxyPattern] = {
    target: item.baseURL,
    changeOrigin: true,
    rewrite: path => path.replace(new RegExp(`^${item.proxyPattern}`), ''),
    // 配置代理超时
    timeout: 30000,
    // 配置代理日志
    configure: (proxy, options) => {
      proxy.on('error', (err, req, res) => {
        console.log('代理错误:', err);
      });
      proxy.on('proxyReq', (proxyReq, req, res) => {
        console.log('代理请求:', req.method, req.url);
      });
      proxy.on('proxyRes', (proxyRes, req, res) => {
        console.log('代理响应:', proxyRes.statusCode, req.url);
      });
    }
  };

  return proxy;
}
