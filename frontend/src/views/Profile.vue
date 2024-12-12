<template>
  <div class="card" style="width: 50%; padding: 40px 20px;">
    <el-form ref="formRef" :rules="data.rules" :model="data.user" label-width="80px"
             style="padding-right: 40px; padding-top:20px">

      <el-form-item label="头像">
        <div style="width: 100%; display: flex; justify-content: center; margin-bottom: 20px;">
          <el-upload class="avatar-uploader" action="http://localhost:8080/files/upload"
                     :headers="{token: data.user.token}"
                     :show-file-list="false" :on-success="handleAvatarSuccess">
            <img v-if="data.user.avatar" :src="data.user.avatar" class="avatar">
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>

        </div>
      </el-form-item>
      <el-form-item  label="用户名" prop="username" placeholder="请输入名称">
        <el-input v-model="data.user.username" autocomplete="off" :disabled="true"/>
      </el-form-item>
      <el-form-item label="邮箱" prop="email" placeholder="请输入邮箱">
        <el-input v-model="data.user.email" autocomplete="off" />
      </el-form-item>
      <el-form-item label="昵称" prop="nickname" placeholder="请输入昵称">
        <el-input v-model="data.user.nickname" autocomplete="off"/>
      </el-form-item>
      <el-form-item label="性别" placeholder="请输入性别" prop="gender">
        <el-select v-model="data.user.gender">
          <el-option label="男" value="男"></el-option>
          <el-option label="女" value="女"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="权限等级" prop="status">
        <el-input :rows="3" type="textarea" v-model="data.user.status" autocomplete="off"
                  placeholder="请输入权限等级" :disabled="true" />
      </el-form-item>
      <div style="text-align: right; margin-top: 20px;">
        <el-button type="primary" @click="updateProfile">保存</el-button>
      </div>
    </el-form>
  </div>
</template>

<script setup>
import {reactive, ref} from 'vue';
import {Search, Edit, Delete} from '@element-plus/icons-vue';
import service from '@/utils/request';
import {ElMessage, ElMessageBox} from 'element-plus';

const formRef = ref(null)

const data = reactive({
  user: JSON.parse(localStorage.getItem('grantedUser') || '{}'),
  form: {},
  ids: [],
  rules: {
    username: [
      {required: true, message: '请输入用户名', trigger: 'blur'},
      {min: 1, max: 10, message: '长度在 1 到 10 个字符', trigger: 'blur'}
    ],
    email: [
      {required: true, message: '请输入邮箱', trigger: 'blur'},
      {type: 'email', message: '邮箱格式不正确', trigger: 'blur'}
    ],
    nickname: [
      {required: true, message: '请输入昵称', trigger: 'blur'},
      {min: 1, max: 10, message: '长度在 1 到 10 个字符', trigger: 'blur'}
    ],
    status: [
      {required: true, message: '请输入权限等级', trigger: 'blur'},
      {min: 1, max: 10, message: '长度在 1 到 10 个字符', trigger: 'blur'}
    ]
  }
})

const updateProfile = () => {
  formRef.value.validate((valid) => {
    if(valid){
      service.put('/user/update', data.user).then((res) => {
        if (res.code === "200") {
          ElMessage.success('修改成功')
          localStorage.setItem('grantedUser', JSON.stringify(data.user))
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}

const handleAvatarSuccess = (res) => {
  if (res.code === '200') {
    const avatarUrl = res.data
    
    data.user.avatar = avatarUrl
    localStorage.setItem('grantedUser', JSON.stringify(data.user))
    ElMessage.success('头像更新成功')
    setTimeout(() => {
      location.reload()
    }, 1000)
  } else {
    ElMessage.error('头像更新失败')
  }
}

</script>

<style scoped>
.avatar-uploader .avatar {
  width: 120px;
  height: 120px;
  display: block;
}
</style>

<style>
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 50%;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  text-align: center;
}
</style>
