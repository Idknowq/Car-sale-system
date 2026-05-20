import { INVENTORY_STATUS_MAP, ORDER_STATUS_MAP } from '../constants/status'

export function formatDateTime(value) {
  if (!value) return '-'
  const date = new Date(value)
  if (Number.isNaN(date.getTime())) return String(value)

  const pad = (n) => String(n).padStart(2, '0')
  return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())} ${pad(date.getHours())}:${pad(date.getMinutes())}:${pad(date.getSeconds())}`
}

export function formatDate(value) {
  if (!value) return '-'
  const date = new Date(value)
  if (Number.isNaN(date.getTime())) return String(value)

  const pad = (n) => String(n).padStart(2, '0')
  return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())}`
}

export function formatMoney(value) {
  if (value == null || value === '') return '-'
  const num = Number(value)
  if (Number.isNaN(num)) return String(value)
  return num.toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
}

export function formatInventoryStatus(value) {
  return INVENTORY_STATUS_MAP[value] || value || '-'
}

export function formatOrderStatus(value) {
  return ORDER_STATUS_MAP[value] || value || '-'
}
