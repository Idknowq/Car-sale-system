import request from '../utils/request'

export const reportPing = () => request.get('/api/report/ping')
