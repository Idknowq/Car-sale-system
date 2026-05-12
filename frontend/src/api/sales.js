import request from '../utils/request'

export const salesPing = () => request.get('/api/sales/ping')
