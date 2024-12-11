<template>
  <div class="nearby-news">
    <h2>周边新闻</h2>
    <div class="news-list">
      <!-- 新闻列表内容 -->
      <el-table :data="newsData.tableData" stripe style="width: 100%">
        <el-table-column prop="title" label="标题" />
        <el-table-column prop="location" label="地点" />
        <el-table-column prop="time" label="发布时间" />
        <el-table-column prop="description" label="描述" show-overflow-tooltip />
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import service from '@/utils/request'

const newsData = reactive({
  tableData: []
})

// 获取周边新闻数据
const getNearbyNews = () => {
  service.get('/nearby-news').then(res => {
    newsData.tableData = res.data
  })
}

getNearbyNews()
</script>

<style scoped>
.nearby-news {
  padding: 20px;
  margin-top: 80px;
}
</style> 