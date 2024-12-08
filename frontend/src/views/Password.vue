<template>
    <div class="card" style="width: 50%; padding: 40px 20px;">
        <el-form ref="formRef" :rules="data.rules" :model="data.form" label-width="80px"
            style="padding-right: 40px; padding-top:20px">
            <el-form-item label="原密码" prop="oldPassword" placeholder="请输入原密码">
                <el-input show-password v-model="data.form.oldPassword" autocomplete="off" />
            </el-form-item>
            <el-form-item label="新密码" prop="newPassword" placeholder="请输入新密码">
                <el-input show-password v-model="data.form.newPassword" autocomplete="off" />
            </el-form-item>
            <el-form-item label="确认新密码" prop="confirmNewPassword" placeholder="请确认新密码">
                <el-input show-password v-model="data.form.confirmNewPassword" autocomplete="off" />
            </el-form-item>
            <div style="text-align: right; margin-top: 20px;">
                <el-button type="primary" @click="updatePassword">确认修改</el-button>
            </div>
        </el-form>
    </div>
</template>

<script setup>
import { reactive, ref } from 'vue';
import service from '@/utils/request';
import { ElMessage, ElMessageBox } from 'element-plus';

const validatePassword = (rule, value, callback) => {
    if (value === '') {
        callback(new Error('密码不能为空!'));
    } else if (value.length < 3 || value.length > 10) {
        callback(new Error('密码长度在 3 到 10 个字符!'));
    } else if (value !== data.form.confirmNewPassword) {
        callback(new Error('两次输入的密码不一致!'));
    } else {
        callback();
    }
}


const formRef = ref(null)

const data = reactive({
    user: JSON.parse(localStorage.getItem('grantedUser') || '{}'),
    form: {},
    rules: {
        oldPassword: [
            { required: true, message: '请输入原密码', trigger: 'blur' },
            { min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur' }
        ],
        newPassword: [
            { required: true, message: '请输入新密码', trigger: 'blur' },
            { min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur' }
        ],
        confirmNewPassword: [
            { validator: validatePassword, trigger: 'blur' }
            // { required: true, message: '请确认新密码', trigger: 'blur' },
            // { min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur' }
        ]
    }
})

const updatePassword = () => { //修改的对象有id
    data.form.id = data.user.id
    if (data.form.newPassword !== data.form.confirmNewPassword) {
        ElMessage.error('两次输入的密码不一致')
        return
    }
    if (data.form.oldPassword === data.form.newPassword) {
        ElMessage.error('新密码不能与原密码相同')
        return
    }
    data.user.password = data.form.newPassword
    formRef.value.validate((valid) => {
        if(valid){
            service.put('/updatePassword', data.user).then((res) => {
            if (res.code === "200") {
                ElMessage.success('修改成功')
                localStorage.removeItem('grantedUser')
                setTimeout(() => {
                    window.location.href = '/login'
                }, 500)
            } else {
                ElMessage.error(res.msg)
            }
        })
        }
    })
}


</script>

<style scoped></style>
