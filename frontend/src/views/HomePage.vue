<template>
  <div>
    <header
        style="height: 60px; background-color: #3c7fff; display: flex; align-items: center; justify-content: space-between; padding: 0 20px;">
      <div style="width: 200px; display: flex; align-items: center;">
        <img src="@/assets/icons8-star-64.png" style="margin-left: 10px; width: 40px; height: 40px;"/>
        <span style="font-size: 20px; font-weight: bold; color: wheat;">邸报管理系统</span>
      </div>
      <div style="flex: 1"></div>
      <div style="display: flex; align-items: center; width: fit-content; cursor: pointer;">
        <el-avatar :src="data.user.avatar || 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'"
                   alt-text="头像" style="width: 40px;height: 40px;border-radius: 50%;"/>
        <span style="color: #eee;">{{ data.user.username }}</span>
      </div>
    </header>
  </div>
  <!-- 头部结束 -->
  <div style="display: flex;">
    <!-- 左侧菜单开始 -->
    <div style="width: 200px; border-right: 1px solid #eee; min-height: calc(100vh - 60px);">
      <el-menu router :default-active="router.currentRoute.value.path" :default-openeds="['1']"
               class="el-menu-vertical-demo" background-color="#f0f2f5" text-color="#303133"
               active-text-color="#409eff">
        <el-menu-item @click="goToDashboard" >
          <el-icon><House /></el-icon>
          返回首页
        </el-menu-item>
        <el-menu-item index="/home/profile" v-if="data.user.occupation === '用户'">
          <el-icon>
            <Avatar/>
          </el-icon>
          个人信息
        </el-menu-item>
        <el-menu-item index="/home/dataAnalysis">
          <el-icon>
            <DataAnalysis/>
          </el-icon>
          数据展示
        </el-menu-item>
        <el-menu-item index="/home/article">
          <el-icon>
            <Document/>
          </el-icon>
          新闻管理
        </el-menu-item>
        <el-menu-item index="/home/department" v-if="data.user.occupation === '管理员'">
          <el-icon>
            <OfficeBuilding/>
          </el-icon>
          部门管理
        </el-menu-item>

        <el-sub-menu index="1">
          <template #title>
            <el-icon>
              <location/>
            </el-icon>
            <span>用户管理</span>
          </template>
          <el-menu-item index="/home/admin" v-if="data.user.occupation === '管理员'">管理员页面</el-menu-item>
          <el-menu-item index="/home/user" v-if="data.user.occupation === '管理员'">用户页面</el-menu-item>
          <el-menu-item index="/home/password">密码修改页面</el-menu-item>
        </el-sub-menu>
        <el-menu-item @click="logout">
          <el-icon>
            <SwitchButton/>
          </el-icon>
          退出登录
        </el-menu-item>
      </el-menu>
    </div>
    <!-- 左侧菜单结束 -->
    <!-- 右侧内容开始 -->
    <div style="flex: 1; padding: 20px;">
      <RouterView></RouterView>
    </div>
  </div>
</template>

<script setup>
import {reactive} from 'vue'
import {
  Avatar,
  DataAnalysis,
  Document,
  Location,
  OfficeBuilding,
  Setting,
  SwitchButton,
  House
} from '@element-plus/icons-vue'

import router from '@/router';

const data = reactive({
  user: JSON.parse(localStorage.getItem('grantedUser') || '{}'),
})


const logout = () => {
  localStorage.removeItem('grantedUser')
  location.href = '/login'
}

const goToDashboard = () => {
  router.push('/dashboard')
}

const getUser = () => {
  data.user = JSON.parse(localStorage.getItem('grantedUser'))
}


const updateUser = () => {
  data.user = JSON.parse(localStorage.getItem('grantedUser'))
}

</script>

<style>
.el-menu .is-active {
  background-color: #e6ecf7 !important;
}
</style>