import request from '../utils/request'

export const inventoryPing = () => request.get('/api/inventory/ping')
