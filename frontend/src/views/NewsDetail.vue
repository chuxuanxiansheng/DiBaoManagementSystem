<template>
  <!-- 顶部状态栏 -->
  <div class="header">
    <div class="content">
      <!-- Logo部分 -->
      <div class="lg">
        <span v-for="item in LogoInfo" :key="item.letter" :style="{color: item.color}">{{item.letter}}</span>
        <span class="chinese-logo">邸报</span>
      </div>

      <!-- 登录状态 -->
      <div class="panel-login">
        <template v-if="isLoggedIn">
          <div class="user-controls">
            <el-avatar :src="user.avatar"
                       alt-text="头像" style="width: 40px;height: 40px;border-radius: 50%;"/>


            <el-button type="text" @click="goToProfile">个人中心</el-button>
            <el-button type="text" @click="logout">退出登录</el-button>
          </div>
        </template>
        <el-button v-else type="primary" @click="goToLogin">登录</el-button>
        <el-button type="text" @click="goHome">返回首页</el-button>
      </div>
    </div>
  </div>

  <div class="container">
    <div class="news-detail">
      <button @click="goToHome" class="back-button">返回首页</button>
      <h1 class="news-title">{{ news.title }}</h1>
      <img :src="news.img" alt="新闻图片" class="news-image" />
      <p class="news-description">{{ news.description }}</p>
      <div class="news-meta">
        <span>作者：{{ news.author }}</span>
        <span>分类：{{ news.categoryName }}</span>
        <span>阅读：{{ news.viewCount }}</span>
        <span>评论：{{ news.comment_count }}</span>
        <span>时间：{{ formatTime(news.time) }}</span>
      </div>

      <!-- 评论框 -->
      <div class="comment-section">
        <h2>发表评论</h2>
        <textarea v-model="newComment" placeholder="请输入评论内容" rows="4"></textarea>
        <button @click="submitComment" :disabled="!isLoggedIn">提交评论</button>
        <p v-if="!isLoggedIn" class="login-warning">请先登录以发表评论。</p>
      </div>

      <!-- 评论列表 -->
      <div class="comments-list">
        <h2>评论列表</h2>
        <div v-for="comment in comments" :key="comment.id" class="comment-item">
          <p><strong>{{ comment.username }}</strong>：{{ comment.content }}</p>
          <p class="comment-time">{{ formatTime(comment.createTime) }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import service from '@/utils/request'
import { Search } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const news = ref({})
const comments = ref([])
const newComment = ref('')
const isLoggedIn = ref(!!localStorage.getItem('grantedUser')) // 检查登录状态
const searchTitle = ref('')

// 添加Logo信息
const LogoInfo = ref([
  { letter: 'D', color: '#f44336' },
  { letter: 'i', color: '#3688f4' },
  { letter: 'B', color: '#e7f436' },
  { letter: 'a', color: '#88f436' },
  { letter: 'o', color: '#e7d0d6' }
])

// 修改用户信息处理逻辑
const userInfo = JSON.parse(localStorage.getItem('grantedUser') || '{}')

// 添加处理头像URL的方法
const getAvatarUrl = (avatar) => {
  if (!avatar) return 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
  if (avatar.startsWith('http')) return avatar
  return `http://localhost:8080/files/download/${avatar}`
}

// 确保头像路径正确
const user = ref({
  ...userInfo,
  avatar: getAvatarUrl(userInfo.avatar)
})

const updateDocumentTitle = (title) => {
  document.title = title ? `${title} - 邸报` : '邸报'
}

// 监听news对象的变化
watch(() => news.value.title, (newTitle) => {
  updateDocumentTitle(newTitle)
})

const fetchNewsDetail = async () => {
  const newsId = route.params.id
  try {
    const response = await service.get(`/article/selectById/${newsId}`)
    news.value = response.data
    // 更新页面标题
    updateDocumentTitle(news.value.title)
    
    // 确保图片路径正确
    if (news.value.img && !news.value.img.startsWith('http')) {
      news.value.img = `/${news.value.img}`;
    }
    // 增加阅读量
    await service.post(`/article/view/${newsId}`)
  } catch (error) {
    console.error('获取新闻详情失败:', error)
  }
}

const fetchComments = async () => {
  const newsId = route.params.id
  try {
    const response = await service.get(`/article/comments/${newsId}`)
    comments.value = response.data
  } catch (error) {
    console.error('获取评论失败:', error)
  }
}

const submitComment = async () => {
  if (!newComment.value.trim()) {
    ElMessage({
      message: '评论内容不能为空',
      type: 'warning'
    })
    return
  }

  const user = JSON.parse(localStorage.getItem('grantedUser') || '{}')
  const commentData = {
    articleId: route.params.id,
    userId: user.uid,
    content: newComment.value
  }

  try {
    await service.post('/article/comment', commentData)
    ElMessage({
      message: '评论发布成功',
      type: 'success'
    })
    newComment.value = '' // 清空评论框
    await Promise.all([
      fetchComments(), // 重新获取评论列表
      fetchNewsDetail() // 重新获取文章信息以更新评论数
    ])
  } catch (error) {
    console.error('提交评论失败:', error)
    ElMessage({
      message: '评论发布失败，请稍后重试',
      type: 'error'
    })
  }
}

const formatTime = (time) => {
  return new Date(time).toLocaleString()
}

const goToHome = () => {
  router.push('/');
};
// 新增返回首页的方法
function goHome() {
  router.push('/');
}
const goToProfile = () => {
  router.push('/home')
}

const goToLogin = () => {
  router.push('/login')
}

const logout = () => {
  localStorage.removeItem('grantedUser')
  location.reload()
}

const goToNewsHome = () => {
  router.push('/')
}

const goToCategory = () => {
  router.push('/')
  // 可以添加滚动到分类区域的逻辑
}

const goToAbout = () => {
  ElMessage({
    message: '欢迎使用新闻管理系统！',
    type: 'success'
  })
}

const handleSearch = () => {
  router.push({
    path: '/',
    query: { search: searchTitle.value }
  })
}

const handleReset = () => {
  searchTitle.value = ''
}

onMounted(() => {
  fetchNewsDetail()
  fetchComments()
})
</script>

<style scoped>
.header {
  background: #92c3fe;
  width: 100%;
  position: fixed;
  height: 80px;
  z-index: 999;
  top: 0;
  left: 0;
}

.content {
  max-width: 1200px;
  margin: 0 auto;
  height: 80px;
  display: flex;
  align-items: center;
  justify-content: space-between; /* 两端对齐 */
  padding: 0 50px; /* 增加两侧内边距 */
}

.lg {
  display: flex;
  align-items: center;
  font-size: 24px;
  font-weight: bold;
}

.chinese-logo {
  margin-left: 8px;
  color: #fff;
}

.news-detail {
  max-width: 800px;
  margin: 0 auto; /* 修改上下边距 */
  padding: 30px;
  background: #ffffff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.news-title {
  font-size: 2.5em;
  margin-bottom: 20px;
  color: #333;
  text-align: center;
  font-weight: bold;
}

.news-image {
  width: 100%;
  height: auto;
  border-radius: 8px;
  margin-bottom: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.news-description {
  margin-top: 20px;
  font-size: 18px;
  line-height: 1.8;
  color: #555;
  text-align: justify;
}

.news-meta {
  margin-top: 20px;
  font-size: 14px;
  color: #666;
  flex-wrap: wrap;
  gap: 10px;
  justify-content: center;
}

.news-meta span {
  background-color: #f0f0f0;
  padding: 5px 15px;
  border-radius: 20px;
}

.comment-section {
  margin-top: 40px;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 8px;
}

.comment-section h2 {
  font-size: 1.8em;
  margin-bottom: 15px;
  color: #333;
}

.comment-section textarea {
  width: 100%;
  padding: 15px;
  border-radius: 8px;
  border: 1px solid #ccc;
  margin-bottom: 15px;
  font-size: 16px;
}

.comment-section button {
  padding: 10px 25px;
  background-color: #409eff;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-size: 16px;
}

.comment-section button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.login-warning {
  color: red;
  font-size: 14px;
}

.comments-list {
  margin-top: 40px;
}

.comments-list h2 {
  font-size: 1.8em;
  margin-bottom: 15px;
  color: #333;
}

.comment-item {
  background-color: #fff;
  padding: 15px;
  border-radius: 8px;
  margin-bottom: 15px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.comment-time {
  font-size: 12px;
  color: #999;
  margin-top: 5px;
}

.back-button {
  margin-bottom: 20px;
  padding: 10px 20px;
  background-color: #409eff;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-size: 16px;
}

.back-button:hover {
  background-color: #66b1ff;
}

.panel-login {
  display: flex;
  align-items: center;
}

.user-controls {
  display: flex;
  align-items: center;
  gap: 15px; /* 控制元素之间的间距 */
}
</style> 