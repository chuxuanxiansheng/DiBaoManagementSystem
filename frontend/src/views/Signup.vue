<template>
    <div class="login-container">
        <div class="login-box">
            <div style="padding: 50px 30px; background-color: white; border-radius: 5px;">
                <el-form ref="formRef" :rules="data.rules" :model="data.form" style="width: 300px;">
                    <div
                        style="margin-bottom: 30px; font-size: 20px; text-align: center; color: aqua; font-weight: bold;">
                        欢迎使用邸报管理系统</div>
                    <el-form-item prop="username">
                        <el-input size="large" type="text" v-model="data.form.username" placeholder="请输入用户名"
                            prefix-icon="User"></el-input>
                    </el-form-item>
                  <el-form-item prop="email">
                    <el-input size="large" type="text" v-model="data.form.email" placeholder="请输入邮箱"
                              prefix-icon="Message"></el-input>
                  </el-form-item>
                  <el-form-item prop="gender">
                    <el-select v-model="data.form.gender" placeholder="请选择性别" size="large" prefix-icon="Sort">
                      <el-option label="男" value="男"></el-option>
                      <el-option label="女" value="女"></el-option>
                    </el-select>
                  </el-form-item>
                    <el-form-item prop="password">
                        <el-input show-password size="large" type="password" v-model="data.form.password"
                            placeholder="请输入密码" prefix-icon="Lock"></el-input>
                    </el-form-item>
                    <el-form-item prop="confirmPassword">
                        <el-input show-password size="large" type="password" v-model="data.form.confirmPassword"
                            placeholder="请再次输入密码" prefix-icon="Lock"></el-input>
                    </el-form-item>
                    <div style="margin-bottom: 20px;">
                        <el-button type="primary" size="large" @click="signup" style="width: 100%;">注册</el-button>
                    </div>
                    <div style="text-decoration: none; color: #666; font-size: 14px; margin-bottom: 20px;">已有账号?请
                        <router-link to="/login">登录</router-link>
                    </div>
                </el-form>
            </div>
        </div>
    </div>
</template>
<script setup>
import { reactive, ref } from 'vue'
import {User, Lock, Sort} from "@element-plus/icons-vue"
import service from '@/utils/request';
import { ElMessage } from 'element-plus';


const validatePassword = (rule, value, callback) => {
    if (!value) {
        callback(new Error('请输入密码'))
    } else if (value.length < 6 || value.length > 20) {
        callback(new Error('密码长度在6到20个字符'))
    } else if (value !== data.form.password) {
        callback(new Error('两次输入的密码不一致!'))
    } else {
        callback()
    }
}

const data = reactive({
    form: {
        // username: '',
        // password: '',
        // confirmPassword: ''
    },
    rules: {
        username: [
            { required: true, message: '请输入用户名', trigger: 'blur' },
            { min: 2, max: 20, message: '用户名长度在2到20个字符', trigger: 'blur' }
        ],
        email: [
            { required: true, message: '请输入邮箱', trigger: 'blur' },
            { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
        ],
        password: [
            { required: true, message: '请输入密码', trigger: 'blur' },
            { min: 6, max: 20, message: '密码长度在6到20个字符', trigger: 'blur' }
        ],
        confirmPassword: [
            { validator: validatePassword, trigger: 'blur' }
        ]
    }
})


const formRef = ref(null)

const signup = () => {
    formRef.value.validate((valid) => {
        if (valid) {
            service.post('/signup', data.form).then(res => {
                console.log(res)
                if (res.code === '200') {
                    ElMessage.success('注册成功')
                    location.href = '/login'
                } else {
                    ElMessage.error(res.msg)
                }
            })
        }
    })
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
