import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/login/LoginView.vue'
import MainLayout from '../layout/MainLayout.vue'
import SalesView from '../views/sales/SalesView.vue'
import InventoryView from '../views/inventory/InventoryView.vue'
import ReportView from '../views/report/ReportView.vue'

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', name: 'login', component: LoginView },
  {
    path: '/',
    component: MainLayout,
    children: [
      { path: 'sales', name: 'sales', component: SalesView },
      { path: 'inventory', name: 'inventory', component: InventoryView },
      { path: 'report', name: 'report', component: ReportView }
    ]
  }
]

export default createRouter({
  history: createWebHistory(),
  routes
})
