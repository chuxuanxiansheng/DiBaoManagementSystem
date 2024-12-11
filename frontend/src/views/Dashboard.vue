<template>
  <div>
    <div class="header">
      <div class="content">
        <!-- logo -->
        <div class="lg">
          <span v-for="item in LogoInfo" :key="item.letter" :style="{color: item.color}">{{item.letter}}</span>
        </div>

        <div class="panel-info">
          <el-button type="text" @click="goToNewsHome">新闻首页</el-button>
          <el-button type="text" @click="goToNearbyNews">周边新闻</el-button>
          <el-button type="text" @click="goToFeedback">意见反馈</el-button>
        </div>

        <!--搜索状态-->
        <div class="search">
          <div>
            <el-input v-model="data.title" placeholder="请输入标题" :prefix-icon="Search" clearable style="margin-top: 10px; width: 300px;"></el-input>
            <el-button type="primary" @click="handleSearch" style="margin-top: 10px;">查询</el-button>
            <el-button type="primary" @click="handleReset" style="margin-top: 10px;">重置</el-button>
          </div>
        </div>
        <!-- 登录状态 -->
        <div class="panel-login">
          <template v-if="isLoggedIn">
            <el-avatar :src="user.avatar || 'default-avatar.png'"
                       alt-text="头像" style="width: 40px;height: 40px;border-radius: 50%;" />
            <el-button type="text" @click="goToProfile">个人中心</el-button>
            <el-button type="text" @click="logout">退出登录</el-button>
          </template>
          <el-button v-else type="primary" @click="goToLogin">登录</el-button>
        </div>
      </div>
    </div>

    <div class="main-container">
      <!-- 轮播图部分 -->
      <div class="carousel-section">
        <el-carousel :height="'400px'" :autoplay="true" :interval="5000">
          <el-carousel-item v-for="item in carouselData" :key="item.id">
<!--            <div class="carousel-item-wrapper">-->
              <img 
                :src="item.imageUrl" 
                class="carousel-image" 
                :alt="item.title"
                @error="handleImageError"
                            @load="handleImageLoad"
              >
              <div class="carousel-title">{{ item.title }}</div>
<!--            </div>-->
          </el-carousel-item>
        </el-carousel>
      </div>

      <!-- 内容区域 -->
      <div class="content-container">
        <!-- 左侧新闻列表 -->
        <div class="news-section">
          <!-- 分类导航 -->
          <div class="category-nav">
            <el-radio-group v-model="currentCategory" @change="handleCategoryChange">
              <el-radio-button v-for="category in categories" 
                              :key="category.id" 
                              :label="category.id">
                {{ category.name }}
              </el-radio-button>
            </el-radio-group>
          </div>

          <!-- 新闻列表 -->
          <div class="news-list">
            <div v-for="news in newsData" 
                 :key="news.id" 
                 class="news-item" 
                 @click="viewNews(news)">
              <img v-if="news.img" :src="news.img" class="news-image" alt="新闻图片">
              <div class="news-content">
                <h3>{{ news.title }}</h3>
                <p class="news-desc">{{ news.description }}</p>
                <div class="news-meta">
                  <span>{{ formatTime(news.time) }}</span>
                  <span v-if="news.author">作者：{{ news.author }}</span>
                  <span v-if="news.categoryName">{{ news.categoryName }}</span>
                  <span>阅读 {{ news.viewCount }}</span>
                  <span>评论 {{ news.commentCount }}</span>
                </div>
              </div>
            </div>
          </div>

          <!-- 加载更多 -->
          <div class="load-more" v-if="hasMore">
            <el-button type="primary" @click="loadMoreNews">加载更多</el-button>
          </div>
        </div>

        <!-- 右侧热点榜 -->
        <div class="hot-section">
          <div class="hot-title">
            <h3>热点榜</h3>
            <el-button type="text" @click="getHotNews">
              <el-icon><Refresh /></el-icon> 换一换
            </el-button>
          </div>
          <div class="hot-list">
            <div v-for="(item, index) in hotNews" :key="item.id" class="hot-item" @click="viewNews(item)">
              <span class="hot-index" :class="{'top-three': index < 3}">{{ index + 1 }}</span>
              <span class="hot-text">{{ item.title }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue';
import { Search, Refresh, Loading } from '@element-plus/icons-vue';
import service from '@/utils/request';
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import dayjs from 'dayjs'
import Masonry from 'vue-masonry-css'


import '@wangeditor/editor/dist/css/style.css'
import '@/assets/view.css'
import router from "@/router/index.js";

import carousel1 from '../assets/1.png'
import carousel2 from '../assets/2.png'
import carousel3 from '../assets/3.png'

const LogoInfo=ref([{
      letter:'D',
      color:'#f44336'
    },
      {
        letter:'i',
        color:'#3688f4'
      },
      {
        letter:'B',
        color:'#e7f436'
      },
      {
        letter:'a',
        color:'#88f436'
      },
      {
        letter:'o',
        color:'#e7d0d6'
      }
    ]
);
const formRef = ref(null)

const data = reactive({
  fileList: [],
  tableData: [],
  title: null,
  dialogVisible: false,
  formVisible: false,
  pageNum: 1,
  pageSize: 5,
  total: 0,
  form: null,
  ids: [],
  viewVisible: false,
  content: null
})

const view = (content) => {
  data.content = content
  data.viewVisible = true
}
const getAllData = () => {
  service.get('/show', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      title: data.title
    }
  }).then(res => {
    data.tableData = res.data.list
    data.total = res.data.total
  })
}
getAllData()

const handleSelectionChange = (rows) => {
  // console.log(rows)
  data.ids = rows.map(item => item.id)
}

const user = reactive(JSON.parse(localStorage.getItem('grantedUser') || '{}'))
const isLoggedIn = ref(!!localStorage.getItem('grantedUser'))

const goToLogin = () => {
  router.push('/login')
}

const goToProfile = () => {
  router.push('/home')
}

const logout = () => {
  localStorage.removeItem('grantedUser')
  location.href = '/login'
}

// import carousel1 from '@/assets/1.png'
// import carousel2 from '@/assets/2.png'
// import carousel3 from '@/assets/3.png'


// 轮播图数据
const carouselData = ref([
  {
    id: 1,
    title: '智元机器人',
    imageUrl: carousel1
  },
  {
    id:2,
    title:'科技创新',
    imageUrl: carousel2
  },
  {
    id:3,
    title:'Cursor ai',
    imageUrl: carousel3
  }
  // ... 其他轮播图数据
])

// 新增分类相关的数据
const categories = ref([
  { id: 'all', name: '全部' },
  { id: 1, name: '教育' },
  { id: 2, name: '健康' },
  { id: 3, name: '军事' },
  { id: 4, name: '社会' },
  { id: 5, name: '体育' },
  { id: 6, name: '娱乐' },
  { id: 7, name: '科技' },
  { id: 8, name: '财经' }
])
const currentCategory = ref('all')

const newsData = ref([])
const hotNews = ref([])
const pageNum = ref(1)
const pageSize = ref(10)
const hasMore = ref(true)

// 修改获取新闻列表方法，添加分类参数
const getNewsList = async () => {
  try {
    const res = await service.get('/show', {
      params: {
        pageNum: pageNum.value,
        pageSize: pageSize.value,
        title: data.title,
        categoryId: currentCategory.value === 'all' ? 0 : currentCategory.value
      }
    })
    if (res.code === '200') {
      if (pageNum.value > 1) {
        newsData.value.push(...res.data.list)
      } else {
        newsData.value = res.data.list
      }
      data.total = res.data.total
      hasMore.value = newsData.value.length < data.total
    } else {
      ElMessage.error(res.msg || '获取新闻列表失败')
    }
  } catch (error) {
    console.error('获取新闻列表失败:', error)
    ElMessage.error('获取新闻列表失败')
  }
}

// 添加分类切换方法
const handleCategoryChange = (categoryId) => {
  currentCategory.value = categoryId
  pageNum.value = 1
  newsData.value = []
  getNewsList()
}

// 加载更多
const loadMoreNews = () => {
  if (hasMore.value) {
    pageNum.value += 1
    getNewsList()
  }
}

// 搜索和重置
const handleSearch = () => {
  pageNum.value = 1
  newsData.value = []
  getNewsList()
}

const handleReset = () => {
  data.title = null
  pageNum.value = 1
  newsData.value = []
  getNewsList()
}

// 查看新闻详情
const viewNews = (news) => {
  router.push(`/news/${news.id}`)
}

// 获热门新闻
const getHotNews = async () => {
  try {
    const res = await service.get('/article/hot')
    if (res.code === '200') {
      hotNews.value = res.data
    } else {
      ElMessage.error(res.msg || '获取热门新闻失败')
    }
  } catch (error) {
    console.error('获取热门新闻失败:', error)
    ElMessage.error('获取热门新闻失败')
  }
}

// 格式化时间
const formatTime = (time) => {
  return dayjs(time).format('YYYY-MM-DD HH:mm:ss')
}

const imageLoaded = ref(true)

const handleImageError = (e) => {
  console.error('图片加载失败:', e.target.src)
  e.target.src = carousel1
}

const handleImageLoad = () => {
  imageLoaded.value = true
}

onMounted(() => {
  getNewsList()
  getHotNews()
  // if (!isLoggedIn.value) {
  //   router.push('/login')
  // }
})
</script>

<style scoped>
.header{
  margin-top: 0;
  background: #92c3fe;
  width: 100%;
  position: fixed;
  height: 80px;
  z-index: 999;
  .content{
    margin:0 auto;
    height: 80px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    .lg{
      font-size: 30px;
      margin-left:20px
    }


  }

}

/* 新增main-container样式 */
.main-container {
  padding-top: 100px;
  width: 100%;
  box-sizing: border-box;
}

.second{
  background-color: #fff;
  border-radius: 5px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  padding: 20px;
  margin: 0 20px;
}

.main-container {
  padding-top: 80px;
  background-color: #f5f7fa;
}

.carousel-section {
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

/* 修改轮播图容器样式 */
.el-carousel {
  width: 100%;
  border-radius: 8px;
  overflow: hidden;
}

:deep(.el-carousel__container) {
  height: 100%;
}

:deep(.el-carousel__item) {
  position: relative;
  overflow: hidden;
  height: 100%;
  background-color: #f5f7fa;
}

.carousel-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  background-color: #f0f0f0; /* 浅灰色 */
  display: block;
  background-position: center;
  background-repeat: no-repeat;
}

.carousel-title {
  position: absolute;
  bottom: 20px;
  left: 20px;
  color: #333333;
  font-size: 24px;
  text-shadow: 0 2px 4px rgba(0,0,0,0.5);
  z-index: 2;
  padding: 10px;
  background: rgba(0,0,0,0.3);
  border-radius: 4px;
}

/* 添加图片加载状态样式 */
.carousel-item-loading {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  background-color: #f5f7fa;
}

/* 响应式调整 */
@media screen and (max-width: 768px) {
  .carousel-section {
    padding: 10px;
  }
  
  .el-carousel {
    height: 300px !important;
  }
  
  .carousel-title {
    font-size: 18px;
    bottom: 10px;
    left: 10px;
  }
}

.content-container {
  display: flex;
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  gap: 20px;
}

.news-section {
  flex: 1;
  background: #fff;
  border-radius: 8px;
  padding: 20px;
}

.news-categories {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
  flex-wrap: wrap;
}

.category-btn {
  padding: 8px 16px;
}

.news-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
  padding: 20px;
}

.news-item {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  transition: all 0.3s ease;
  display: flex;
  flex-direction: column;
  height: 100%;
}

.news-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
}

.news-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.news-content {
  padding: 15px;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.news-content h3 {
  font-size: 18px;
  margin: 0 0 10px;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  color: #333;
}

.news-desc {
  font-size: 14px;
  color: #666;
  margin-bottom: 15px;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
  line-height: 1.5;
}

.news-meta {
  margin-top: auto;
  padding-top: 10px;
  border-top: 1px solid #eee;
  color: #999;
  font-size: 12px;
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.news-meta span {
  display: inline-flex;
  align-items: center;
}

/* 添加加载动画 */
.news-item {
  animation: fadeIn 0.5s ease;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 响应式布局 */
@media screen and (max-width: 768px) {
  .news-list {
    grid-template-columns: 1fr;
  }
  
  .news-item {
    margin-bottom: 15px;
  }
}

.hot-section {
  width: 300px;
  background: #fff;
  border-radius: 8px;
  padding: 20px;
}

.hot-title {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.hot-title h3 {
  margin: 0;
}

.hot-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.hot-item {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
}

.hot-item:hover .hot-text {
  color: #409EFF;
}

.hot-index {
  width: 20px;
  height: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #eee;
  border-radius: 4px;
  font-size: 12px;
}

.hot-index.top-three {
  background: #409EFF;
  color: #fff;
}

.hot-text {
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.load-more {
  text-align: center;
  margin-top: 20px;
}

.search-bar {
  margin-bottom: 20px;
  display: flex;
  gap: 10px;
  align-items: center;
}

/* 添加分类导航样式 */
.category-nav {
  background: #fff;
  padding: 15px;
  margin-bottom: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  text-align: center;
}

.category-nav .el-radio-group {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 10px;
}

.category-nav .el-radio-button {
  margin: 5px;
}

/* 在小屏幕上调整布局 */
@media screen and (max-width: 768px) {
  .news-item {
    flex-direction: column;
  }
  
  .news-image {
    width: 100%;
    height: 200px;
    margin-top: 10px;
  }
}

.panel-login {
  display: flex;
  align-items: center;
  gap: 10px;
}
</style>