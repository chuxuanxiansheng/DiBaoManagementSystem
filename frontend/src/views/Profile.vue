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
                        <el-icon v-else class="avatar-uploader-icon"></el-icon>
                    </el-upload>

                </div>
            </el-form-item>
            <el-form-item disabled label="用户名" prop="username" placeholder="请输入名称">
                <el-input v-model="data.user.username" autocomplete="off" />
            </el-form-item>
            <el-form-item label="昵称" prop="nickname" placeholder="请输入昵称">
                <el-input v-model="data.user.nickname" autocomplete="off" />
            </el-form-item>
            <el-form-item label="性别">
                <el-radio-group v-model="data.user.gender">
                    <el-radio label="男">男</el-radio>
                    <el-radio label="女">女</el-radio>
                </el-radio-group>
                <!-- <el-input-number style="width: 180px;" :min="18" :max="70" v-model="data.form.age" autocomplete="off"
              placeholder="请输入年龄" /> -->
            </el-form-item>
            <!-- <el-form-item label="地位">
            <el-input v-model="data.form.status" autocomplete="off" placeholder="请输入地位" />
          </el-form-item> -->
            <el-form-item label="职业" prop="occupation">
                <el-input :rows="3" type="textarea" v-model="data.user.occupation" autocomplete="off"
                    placeholder="请输入职业" />
            </el-form-item>
            <div style="text-align: right; margin-top: 20px;">
                <el-button type="primary" @click="updateUser">保存</el-button>
            </div>
        </el-form>
    </div>
</template>

<script setup>
import { reactive, ref } from 'vue';
import { Search, Edit, Delete } from '@element-plus/icons-vue';
import service from '@/utils/request';
import { ElMessage, ElMessageBox } from 'element-plus';

const formRef = ref(null)

const data = reactive({
    user: JSON.parse(localStorage.getItem('grantedUser') || '{}'),
    form: {},
    ids: [],
    rules: {
        username: [
            { required: true, message: '请输入用户名', trigger: 'blur' },
            { min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur' }
        ],
        nickname: [
            { required: true, message: '请输入昵称', trigger: 'blur' },
            { min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur' }
        ],
        occupation: [
            { required: true, message: '请输入职业', trigger: 'blur' },
            { min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur' }
        ]
    }
})

const emit = defineEmits(['updateUser'])
if (data.user.occupation !== "管理员") {
    service.get('/user/selectById/' + data.user.uid).then((res) => {
        data.form = res.data
    })
} else {
    service.get('/admin/selectById/' + data.admin.uid).then((res) => {
        data.form = res.data
    })
}

const updateUser = () => { //修改的对象有id
    if (data.user.occupation !== "管理员") {
        service.put('/user/update', data.form).then((res) => {
            if (res.code === "200") {
                ElMessage.success('修改成功')
                localStorage.setItem('grantedUser', JSON.stringify(data.form))
                emit('updateUser')
            } else {
                ElMessage.error(res.msg)
            }
        })
    } else {
        service.put('/admin/update', data.form).then((res) => {
            if (res.code === "200") {
                ElMessage.success('修改成功')
                localStorage.setItem('grantedUser', JSON.stringify(data.form))
                emit('updateUser')
            } else {
                ElMessage.error(res.msg)
            }
        }
        )
    }
}

const handleAvatarSuccess = (res) => {
    data.form.avatar = res.data
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
