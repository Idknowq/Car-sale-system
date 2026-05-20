import axios from 'axios'
import { ElMessage } from 'element-plus'

const request = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080',
  timeout: 10000
})

request.interceptors.response.use(
  (response) => {
    const payload = response.data
    if (!payload || typeof payload.code !== 'number') {
      return payload
    }

    if (payload.code !== 0) {
      ElMessage.error(payload.message || '请求失败')
      return Promise.reject(new Error(payload.message || 'Request failed'))
    }

    return payload.data
  },
  (error) => {
    const message = error?.response?.data?.message || error.message || '网络异常'
    ElMessage.error(message)
    return Promise.reject(error)
  }
)

export default request
