export const INVENTORY_STATUS_OPTIONS = [
  { label: '在途', value: 'IN_TRANSIT' },
  { label: '在库', value: 'IN_INVENTORY' },
  { label: '已锁定', value: 'LOCKED' },
  { label: '已售出', value: 'SOLD' }
]

export const INVENTORY_STATUS_MAP = Object.freeze(
  INVENTORY_STATUS_OPTIONS.reduce((acc, item) => {
    acc[item.value] = item.label
    return acc
  }, {})
)

export const ORDER_STATUS_OPTIONS = [
  { label: '已创建', value: 'CREATED' },
  { label: '已锁定', value: 'LOCKED' },
  { label: '已取消', value: 'CANCELLED' },
  { label: '已完成', value: 'COMPLETED' }
]

export const ORDER_STATUS_MAP = Object.freeze(
  ORDER_STATUS_OPTIONS.reduce((acc, item) => {
    acc[item.value] = item.label
    return acc
  }, {})
)
