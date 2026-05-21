import request from '../utils/request'

export const salesPing = () => request.get('/api/sales/ping')

export const queryMyOrders = (params) => request.get('/api/sales/orders/mine', { params })

export const getOrderById = (orderId) => request.get(`/api/sales/orders/${orderId}`)

export const createSalesOrder = (data) => request.post('/api/sales/orders', data)

export const createCustomerIntent = (data) => request.post('/api/sales/intents', data)
