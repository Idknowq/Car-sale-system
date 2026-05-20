<template>
  <div class="login-page">
    <el-card class="login-card" shadow="never">
      <h2 class="title">汽车销售管理系统</h2>
      <p class="desc">前端基线已建立。请从左侧模块进入具体业务开发与联调。</p>
      <el-button type="primary" @click="enterSystem">进入系统</el-button>
    </el-card>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

function enterSystem() {
  router.push('/sales')
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f7f8fa;
}

.login-card {
  width: 520px;
}

.title {
  margin: 0 0 8px;
}

.desc {
  margin: 0 0 16px;
  color: #606266;
}
</style>
