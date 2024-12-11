<template>
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
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import service from '@/utils/request'

const route = useRoute()
const router = useRouter()
const news = ref({})
const comments = ref([])
const newComment = ref('')
const isLoggedIn = ref(!!localStorage.getItem('grantedUser')) // 检查登录状态

const fetchNewsDetail = async () => {
  const newsId = route.params.id
  try {
    const response = await service.get(`/article/selectById/${newsId}`)
    news.value = response.data
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
  if (!newComment.value.trim()) return

  const user = JSON.parse(localStorage.getItem('grantedUser') || '{}')
  const commentData = {
    articleId: route.params.id,
    userId: user.uid,
    content: newComment.value
  }

  try {
    await service.post('/article/comment', commentData)
    newComment.value = ''
    fetchComments() // 重新获取评论列表
  } catch (error) {
    console.error('提交评论失败:', error)
  }
}

const formatTime = (time) => {
  return new Date(time).toLocaleString()
}

const goToHome = () => {
  router.push('/');
};

onMounted(() => {
  fetchNewsDetail()
  fetchComments()
})
</script>

<style scoped>
.news-detail {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  //background-image: url('/background.jpg');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
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
</style> 