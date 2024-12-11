<template>
  <div class="feedback">
    <h2>意见反馈</h2>
    <div class="feedback-form">
      <el-form :model="feedbackForm" ref="formRef" :rules="rules">
        <el-form-item label="标题" prop="title">
          <el-input v-model="feedbackForm.title" placeholder="请输入标题"></el-input>
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input type="textarea" v-model="feedbackForm.content" rows="4" placeholder="请输入反馈内容"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitFeedback">提交反馈</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import service from '@/utils/request'

const formRef = ref(null)
const feedbackForm = reactive({
  title: '',
  content: ''
})

const rules = {
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  content: [{ required: true, message: '请输入反馈内容', trigger: 'blur' }]
}

const submitFeedback = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      service.post('/feedback', feedbackForm).then(() => {
        ElMessage.success('反馈提交成功')
        feedbackForm.title = ''
        feedbackForm.content = ''
      })
    }
  })
}
</script>

<style scoped>
.feedback {
  padding: 20px;
  margin-top: 80px;
}
.feedback-form {
  max-width: 600px;
  margin: 0 auto;
}
</style> 