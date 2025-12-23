import axios from 'axios'

// 轻量消息提示替代（不依赖 Element-UI）
const notify = {
  show({ message, type }) {
    // 简易实现：优先使用 console，必要时 alert
    try {
      if (type === 'error') console.error(message);
      else if (type === 'success') console.log(message);
      else console.log(message);
    } catch (e) {}
    if (typeof window !== 'undefined') {
      // 尝试使用全局 $message（由 main.js 提供），否则回退 alert
      const m = (window.__message || {}).info || null;
      const err = (window.__message || {}).error || null;
      const succ = (window.__message || {}).success || null;
      if (type === 'error' && err) err(message);
      else if (type === 'success' && succ) succ(message);
      else if (m) m(message);
      else window.alert(message);
    }
  }
}

const http = axios.create({
  baseURL: process.env.VUE_APP_BASE_API,
  timeout: 1000 * 30,
  withCredentials: true
})

/**
 * 请求拦截
 */
http.interceptors.request.use(config => {
  let accessToken = sessionStorage.getItem("accessToken");
  if (accessToken) {
    config.headers.accessToken = encodeURIComponent(accessToken);
  }
  return config
}, error => {
  return Promise.reject(error)
})

/**
 * 响应拦截
 */
http.interceptors.response.use(async response => {
  if (response.data.code == 200) {
    return response.data.data;
  } else if (response.data.code == 400) {
    location.href = "/";
  } else if (response.data.code == 401) {
    console.log("token失效，尝试重新获取")
    let refreshToken = sessionStorage.getItem("refreshToken");
    if (!refreshToken) {
      location.href = "/";
    }
    // 发送请求, 进行刷新token操作, 获取新的token
    const data = await http({
      method: 'put',
      url: '/refreshToken',
      headers: {
        refreshToken: refreshToken
      }
    }).catch(() => {
      location.href = "/";
    })
    // 保存token
    sessionStorage.setItem("accessToken", data.accessToken);
    sessionStorage.setItem("refreshToken", data.refreshToken);
    // 重新发送刚才的请求
    return http(response.config)
  } else {
    notify.show({
      message: response.data.message,
      type: 'error'
    })
    return Promise.reject(response.data)
  }
}, error => {
  switch ((error.response || {}).status) {
    case 400:
      notify.show({ message: (error.response || {}).data, type: 'error' })
      break
    case 401:
      location.href = "/";
      break
    case 405:
      notify.show({ message: 'http请求方式有误', type: 'error' })
      break
    case 404:
    case 500:
      notify.show({ message: '服务器出了点小差，请稍后再试', type: 'error' })
      break
    case 501:
      notify.show({ message: '服务器不支持当前请求所需要的某个功能', type: 'error' })
      break
    default:
      notify.show({ message: '网络异常，请稍后再试', type: 'error' })
  }

  return Promise.reject(error)
})

export default http
