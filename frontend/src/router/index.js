import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/login/LoginView.vue'
import SalesView from '../views/sales/SalesView.vue'
import InventoryView from '../views/inventory/InventoryView.vue'
import ReportView from '../views/report/ReportView.vue'

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', component: LoginView },
  { path: '/sales', component: SalesView },
  { path: '/inventory', component: InventoryView },
  { path: '/report', component: ReportView }
]

export default createRouter({
  history: createWebHistory(),
  routes
})
