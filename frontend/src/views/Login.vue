<template>
  <div class="login-container">-->
    <div class="login-box">
      <div style="padding: 50px 30px; background-color: white; border-radius: 5px;">
        <el-form ref="loginFormRef" :rules="rules" :model="user" style="width: 300px;">
          <div
              style="margin-bottom: 30px; font-size: 20px; text-align: center; color: aqua; font-weight: bold;">
            欢迎登录邸报管理系统
          </div>
          <el-form-item prop="username">
            <el-input size="large" type="text" v-model="user.username" placeholder="请输入用户名"
                      prefix-icon="User"></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input show-password size="large" type="password" v-model="user.password"
                      placeholder="请输入密码" prefix-icon="Lock"></el-input>
          </el-form-item>
          <el-form-item prop="validCode">
            <div style="display:flex">
              <el-input prefix-icon="Lock" size="large" style="flex: 1" v-model="user.validCode"
                        placeholder="请输入验证码"></el-input>
              <div style="flex: 1; height: 36px">
                <valid-code @update:value="getCode"/>
              </div>
            </div>
          </el-form-item>
        </el-form>

        <div style="margin-bottom: 20px;">
          <el-button type="primary" size="large" @click="login" style="width: 100%;">登录</el-button>
        </div>
        <div style="text-decoration: none; color: #666; font-size: 14px; margin-bottom: 20px;">还没有账号?请
          <router-link to="/signup">注册</router-link>
        </div>
        <div style="text-decoration: none; color: #666; font-size: 14px; margin-bottom: 20px;">
          <el-button type="info" @click="clearForgetPasswordForm">忘记密码?</el-button>
        </div>
        <div style="margin-bottom: 20px;">
          <el-button type="success" @click="goHome" style="width: 100%;">返回首页</el-button>
        </div>
      </div>
    </div>
    <el-dialog title="忘记密码" v-model="data.formForgetPasswordVisible" width="50%" :close-on-click-modal="false" destroy-on-close>
      <el-form :model="data.formForgetPasswordRef" label-width="80px" style="padding-right: 20px;">
        <el-form-item label="用户名">
          <el-input v-model="data.formForgetPasswordRef.username" size="large" autocomplete="off"
                    placeholder="请输入用户名" prefix-icon="User"></el-input>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="data.formForgetPasswordRef.email" size="large" autocomplete="off" placeholder="请输入邮箱"
                    prefix-icon="Notebook"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button type="primary" @click="data.formForgetPasswordVisible = false">取消</el-button>
          <el-button type="primary" @click="handleForgetPassword">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>

import {ref, reactive, computed} from 'vue'
import {useRouter} from 'vue-router'
import service from '@/utils/request';
import ValidCode from '@/components/ValidCode.vue';

// Form reference
const loginFormRef = ref(null);
const data = reactive({
  user: JSON.parse(localStorage.getItem('grantedUser') || '{}'),
  dialogVisible: false,
  formVisible: false,
  viewVisible: false,
  formForgetPasswordVisible: false,
  formForgetPasswordRef: {
    username: '',
    email: '',
  },
});
// Reactive data
const user = reactive({
  username: '',
  password: '',
  validCode: ''
});

// Code from ValidCode component
let code = ref('');

// Form validation rules
const rules = reactive({
  username: [
    {required: true, message: '请输入用户名', trigger: 'blur'}
  ],
  password: [
    {required: true, message: '请输入密码', trigger: 'blur'}
  ],
  validCode: [
    {required: true, message: '请输入验证码', trigger: 'blur'},
    {validator: validateCode, trigger: 'blur'}
  ]
});

// Router instance
const router = useRouter();

// Validation function for the code
function validateCode(rule, value, callback) {
  if (value === '') {
    callback(new Error('验证码不能为空'));
  } else if (value.toLowerCase() !== code.value) {
    callback(new Error('验证码错误'));
  } else {
    callback();
  }
}

// Login method
function login() {
  loginFormRef.value.validate((valid) => {
    if (valid) {
      service.post('/login', user).then(res => {
        if (res.code === '200') {
          localStorage.setItem('grantedUser', JSON.stringify(res.data));

          router.push('/home');
        } else {
          ElMessage.error('登录失败，请检查用户名或密码');
        }
      });
    }
  });
}

// Update the code when received from ValidCode component
function getCode(newCode) {
  code.value = newCode.toLowerCase();
}

const clearForgetPasswordForm = () => {
  data.formForgetPasswordRef.username = '';
  data.formForgetPasswordRef.email = '';
  data.formForgetPasswordVisible = true;
}

const handleForgetPassword = () => {
  service.post('/resetPassword', data.formForgetPasswordRef).then(res => {
    if (res.code === '200') {
      ElMessage.success('密码已发送至邮箱，请注意查收。');
      ElMessage.success('重置成功！重置后的密码为123456。');
      data.formForgetPasswordVisible = false;
    } else {
      ElMessage.error('邮箱错误或用户名不存在');
    }
  });
}

// 新增返回首页的方法
function goHome() {
  router.push('/');
}

</script>

<style scoped>
.login-container {
  height: 100vh;
  overflow: hidden;
  background-image: url('@/assets/bg.jpg');
  background-size: cover;
}

.login-box {
  position: absolute;
  top: 50%;
  left: 75%;
  display: flex;
  align-items: center;
  transform: translate(-50%, -50%);
  background-color: #fff;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.3);
}
</style>
