<template>
  <el-container class="main-layout">
    <el-aside width="220px" class="sidebar">
      <div class="brand">汽车销售管理系统</div>
      <el-menu :default-active="activeMenu" router class="menu">
        <el-menu-item index="/sales">销售前台</el-menu-item>
        <el-menu-item index="/inventory">库存管理</el-menu-item>
        <el-menu-item index="/report">报表中心</el-menu-item>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header class="header">
        <div class="header-title">{{ pageTitle }}</div>
        <el-button text @click="goLogin">返回登录</el-button>
      </el-header>
      <el-main class="content">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const titleMap = {
  '/sales': '销售前台',
  '/inventory': '库存管理',
  '/report': '报表中心'
}

const activeMenu = computed(() => route.path)
const pageTitle = computed(() => titleMap[route.path] || '汽车销售管理系统')

function goLogin() {
  router.push('/login')
}
</script>

<style scoped>
.main-layout {
  min-height: 100vh;
}

.sidebar {
  border-right: 1px solid var(--el-border-color-light);
  background: #fff;
}

.brand {
  padding: 20px 16px;
  font-size: 16px;
  font-weight: 600;
  border-bottom: 1px solid var(--el-border-color-light);
}

.menu {
  border-right: none;
}

.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-bottom: 1px solid var(--el-border-color-light);
  background: #fff;
}

.header-title {
  font-size: 16px;
  font-weight: 600;
}

.content {
  padding: 20px;
  background: #f7f8fa;
}
</style>
