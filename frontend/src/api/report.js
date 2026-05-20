import request from '../utils/request'

export const reportPing = () => request.get('/api/report/ping')

export const querySalesPerformanceRanking = (params) =>
  request.get('/api/report/sales-performance/ranking', { params })

export const queryBestSellingModels = (params) =>
  request.get('/api/report/best-selling-models', { params })

export const queryMonthlySalesReport = (params) =>
  request.get('/api/report/monthly-sales', { params })
