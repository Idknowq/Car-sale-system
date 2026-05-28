import axios from 'axios'
import { ElMessage } from 'element-plus'

const request = axios.create({
  baseURL: (import.meta.env.VITE_API_BASE_URL || '').trim() || 'http://localhost:8080',
  timeout: 10000
})

function createAppError({ type, code, message, raw }) {
  const error = new Error(message)
  error.type = type
  error.code = code
  error.raw = raw
  return error
}

function normalizeNetworkError(error) {
  if (error?.code === 'ECONNABORTED') {
    return createAppError({
      type: 'timeout',
      code: error.code,
      message: '请求超时，请稍后重试',
      raw: error
    })
  }

  if (error?.response) {
    const status = error.response.status
    const responseMessage = error.response.data?.message
    return createAppError({
      type: 'http',
      code: status,
      message: responseMessage || `服务异常（HTTP ${status}）`,
      raw: error
    })
  }

  if (error?.request) {
    return createAppError({
      type: 'network',
      code: 'NETWORK_ERROR',
      message: '网络异常或服务不可达',
      raw: error
    })
  }

  return createAppError({
    type: 'unknown',
    code: 'UNKNOWN_ERROR',
    message: error?.message || '未知错误',
    raw: error
  })
}

request.interceptors.response.use(
  (response) => {
    const payload = response.data
    if (!payload || typeof payload.code !== 'number') {
      return payload
    }

    if (payload.code !== 0) {
      const appError = createAppError({
        type: 'business',
        code: payload.code,
        message: payload.message || '业务处理失败',
        raw: payload
      })
      ElMessage.error(appError.message)
      return Promise.reject(appError)
    }

    return payload.data
  },
  (error) => {
    const appError = normalizeNetworkError(error)
    ElMessage.error(appError.message)
    return Promise.reject(appError)
  }
)

export const isBusinessError = (error) => error?.type === 'business'
export const isNetworkError = (error) => ['network', 'timeout', 'http'].includes(error?.type)

export default request
