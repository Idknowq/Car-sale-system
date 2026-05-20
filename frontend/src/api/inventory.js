import request from '../utils/request'

export const inventoryPing = () => request.get('/api/inventory/ping')

export const queryInventoryVehicles = (params) => request.get('/api/inventory/vehicles', { params })

export const inboundInventoryVehicle = (data) => request.post('/api/inventory/vehicles/inbound', data)

export const queryInventoryAlerts = (params) => request.get('/api/inventory/alerts', { params })
