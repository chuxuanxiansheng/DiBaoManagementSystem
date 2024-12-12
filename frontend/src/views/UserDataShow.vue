<!--<template>-->
<!--  <div class="card" style="margin: 5px;">-->
<!--    <h1>用户数据展示</h1>-->
<!--    <el-input v-model="data.username" placeholder="请输入内容" :prefix-icon="Search" clearable></el-input>-->
<!--    <el-button type="primary" @click="loadData" style="margin-top: 10px;">查询</el-button>-->
<!--    <el-button type="primary" @click="resetData" style="margin-top: 10px;">重置</el-button>-->
<!--  </div>-->
<!--  <div class="card" style="margin: 5px; display: flex; align-items: center; gap: 10px;">-->
<!--    <el-button type="primary" @click="handleAdd">-->
<!--      <Edit />新增用户-->
<!--    </el-button>-->
<!--    <el-button type="danger" @click="handleBatchDelete">-->
<!--      <Delete />批量删除-->
<!--    </el-button>-->
<!--    <el-upload -->
<!--      name="file" -->
<!--      accept=".xls,.xlsx" -->
<!--      action="http://localhost:8080/user/batchInsert" -->
<!--      :show-file-list="false" -->
<!--      :on-success="batchInsertSuccess"-->
<!--    >-->
<!--      <el-button type="info">导入</el-button>-->
<!--      <div>请选择要导入的Excel文件</div>-->
<!--    </el-upload>-->
<!--    <el-button type="success" @click="handleExport">导出</el-button>-->
<!--  </div>-->
<!--  <div class="card" style="margin: 5px;">-->
<!--    <el-table :data="data.tableData" stripe style="width: 100%" highlight-current-row-->
<!--      @selection-change="handleSelectionChange">-->
<!--      <el-table-column type="selection" width="55"></el-table-column>-->
<!--      <el-table-column prop="username" label="用户名" />-->
<!--      <el-table-column prop="email" label="邮箱" />-->
<!--      <el-table-column prop="avatar" label="头像">-->
<!--        <template #default="scope">-->
<!--          <img v-if="scope.row.avatar" :src="scope.row.avatar"-->
<!--            style="display:block; width: 40px; height: 40px; border-radius: 50%;" />-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--      <el-table-column prop="nickname" label="昵称" />-->
<!--      <el-table-column prop="gender" label="性别" />-->
<!--      <el-table-column prop="status" label="权限等级" />-->
<!--      <el-table-column prop="departmentName" label="部门" />-->
<!--      <el-table-column label="操作" width="140">-->
<!--        <template #default="scope">-->
<!--          <el-button circle type="primary" :icon="Edit" @click="handleEdit(scope.row)"></el-button>-->
<!--          <el-button circle type="danger" :icon="Delete" @click="handleDelete(scope.row.uid)"></el-button>-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--    </el-table>-->
<!--    <el-pagination @size-change="loadData" @prev-click="loadData" @next-click="loadData" @current-change="loadData"-->
<!--      v-model:current-page="data.pageNum" v-model:page-size="data.pageSize" :page-sizes="[5, 10, 15, 20]" background-->
<!--      layout="total, sizes, prev, pager, next, jumper" :total=data.total />-->
<!--  </div>-->
<!--  <div>-->
<!--    <el-dialog v-model="data.formVisible" title="用户信息" width="500px" destroy-on-close>-->
<!--      <el-form ref="formRef" :rules="data.rules" :model="data.form" label-width="80px"-->
<!--        style="padding-right: 40px; padding-top:20px">-->
<!--        <el-form-item label="用户名" prop="username" placeholder="请输入名称">-->
<!--          <el-input :disabled="data.form.uid" v-model="data.form.username" autocomplete="off" />-->
<!--        </el-form-item>-->
<!--        <el-form-item label="邮箱" prop="email" placeholder="请输入邮箱">-->
<!--          <el-input v-model="data.form.email" autocomplete="off" />-->
<!--        </el-form-item>-->
<!--        <el-form-item label="部门">-->
<!--          <el-select style="width: 100%;" v-model="data.form.departmentName">-->
<!--            <el-option v-for="item in data.departments" :key="item.id" :label="item.name" :value="item.name"></el-option>-->
<!--          </el-select>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="头像">-->
<!--          <el-upload action="http://localhost:8080/files/upload" :headers="{'token': data.user.token}" list-type="picture" :on-success="handleAvatarSuccess">-->
<!--            <el-icon style="font-size: 20px; margin-right: 10px;" type="picture-outline"><Avatar /></el-icon>-->
<!--          </el-upload>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="昵称" prop="nickname" placeholder="请输入昵称">-->
<!--          <el-input v-model="data.form.nickname" autocomplete="off" />-->
<!--        </el-form-item>-->
<!--        <el-form-item label="性别">-->
<!--          <el-radio-group v-model="data.form.gender">-->
<!--            <el-radio label="男">男</el-radio>-->
<!--            <el-radio label="女">女</el-radio>-->
<!--          </el-radio-group>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="权限等级" prop="status">-->
<!--          <el-input :rows="3" type="textarea" v-model="data.form.status" autocomplete="off" placeholder="请输入权限等级" />-->
<!--        </el-form-item>-->
<!--      </el-form>-->
<!--      <template #footer>-->
<!--        <div class="dialog-footer">-->
<!--          <el-button type="primary" @click="data.formVisible = false">取消</el-button>-->
<!--          <el-button type="primary" @click="handleSave">保存</el-button>-->
<!--        </div>-->
<!--      </template>-->
<!--    </el-dialog>-->
<!--  </div>-->
<!--</template>-->

<!--<script setup>-->
<!--import { reactive, ref } from 'vue';-->
<!--import { Search, Edit, Delete } from '@element-plus/icons-vue';-->
<!--import service from '@/utils/request';-->
<!--import { ElMessage, ElMessageBox } from 'element-plus';-->

<!--const data = reactive({-->
<!--  user: JSON.parse(localStorage.getItem('grantedUser') || '{}'),-->
<!--  tableData: [],-->
<!--  username: null,-->
<!--  dialogVisible: false,-->
<!--  formVisible: false,-->
<!--  pageNum: 1,-->
<!--  pageSize: 5,-->
<!--  total: 0,-->
<!--  form: null,-->
<!--  departments: [],-->
<!--  ids: [],-->
<!--  rules: {-->
<!--    username: [-->
<!--      { required: true, message: '请输入用户名', trigger: 'blur' },-->
<!--      // { min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur' }-->
<!--    ],-->
<!--    nickname: [-->
<!--      { required: true, message: '请输入昵称', trigger: 'blur' },-->
<!--      // { min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur' }-->
<!--    ],-->
<!--    status: [-->
<!--      { required: true, message: '请输入权限等级', trigger: 'blur' },-->
<!--      // { min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur' }-->
<!--    ]-->
<!--  }-->
<!--})-->

<!--const formRef = ref(null)-->

<!--service.get('/department/list').then(res => {-->
<!--  data.departments = res.data-->
<!--})-->

<!--const batchInsertSuccess = (res) => {-->
<!--  if (res.code === "200") {-->
<!--    ElMessage.success('导入成功')-->
<!--    data.formVisible = false-->
<!--    loadData()-->
<!--  } else {-->
<!--    ElMessage.error(res.msg)-->
<!--  }-->
<!--}-->

<!--const loadData = () => {-->
<!--  service.get('/user/selectPage', {-->
<!--    params: {-->
<!--      pageNum: data.pageNum,-->
<!--      pageSize: data.pageSize,-->
<!--      username: data.username-->
<!--    }-->
<!--  }).then(res => {-->
<!--    data.tableData = res.data.list-->
<!--    data.total = res.data.total-->
<!--  })-->
<!--}-->
<!--loadData()-->

<!--const resetData = () => {-->
<!--  data.username = null-->
<!--  loadData()-->
<!--}-->

<!--const handleEdit = (row) => {-->
<!--  data.form = JSON.parse(JSON.stringify(row))  //深拷贝，防止修改原始数据-->
<!--  data.formVisible = true-->
<!--}-->

<!--const handleAdd = () => {-->
<!--  data.formVisible = true-->
<!--  data.form = {}-->
<!--}-->

<!--const handleAvatarSuccess = (res) => {-->
<!--  data.form.avatar = res.data-->
<!--}-->

<!--const handleDelete = (uid) => {-->
<!--  ElMessageBox.confirm('确认删除该条数据吗？', '确认删除', { type: 'warning' }).then(() => {-->
<!--    service.delete('/user/delete/' + uid).then((res) => {-->
<!--      if (res.code === "200") {-->
<!--        ElMessage.success('删除成功')-->
<!--        loadData()-->
<!--      } else {-->
<!--        ElMessage.error(res.msg)-->
<!--      }-->
<!--    })-->
<!--  }).catch()-->
<!--}-->

<!--const handleSelectionChange = (rows) => {-->
<!--  //console.log(rows)-->
<!--  data.ids = rows.map(item => item.uid)-->
<!--}-->

<!--const handleBatchDelete = () => {-->
<!--  if (data.ids.length === 0) {-->
<!--    ElMessage.error('请先选择要删除的数据')-->
<!--    return-->
<!--  }-->
<!--  ElMessageBox.confirm('确认批量删除选中数据吗？', '确认批量删除', { type: 'warning' }).then(() => {-->

<!--    //取出选中行的id组成数组-->
<!--    service.delete('/user/batchDelete', { data: data.ids }).then((res) => {-->
<!--      if (res.code === "200") {-->
<!--        ElMessage.success('批量删除成功')-->
<!--        data.formVisible = false-->
<!--        loadData()-->
<!--      } else {-->
<!--        ElMessage.error(res.msg)-->
<!--      }-->
<!--    })-->
<!--  }).catch()-->
<!--}-->
<!--const handleSave = () => {-->
<!--  formRef.value.validate((valid) => {-->
<!--    if (valid) {-->
<!--      if (data.form.uid) {-->
<!--        update()-->
<!--      } else {-->
<!--        add()-->
<!--        // ElMessage.error('只能修改，不能新增')-->
<!--      }-->
<!--    } else {-->
<!--      return false-->
<!--    }-->
<!--  })-->
<!--}-->

<!--const add = () => { //新增的对象没有id-->
<!--  console.log(data.form)-->
<!--  service.post('/user/insert', data.form).then((res) => {-->
<!--    if (res.code === "200") {-->
<!--      ElMessage.success('新增成功')-->
<!--      data.formVisible = false-->
<!--      loadData()-->
<!--    } else {-->
<!--      ElMessage.error(res.msg)-->
<!--    }-->
<!--  })-->
<!--}-->
<!--const update = () => { //修改的对象有id-->

<!--  service.put('/user/update', data.form).then((res) => {-->
<!--    if (res.code === "200") {-->
<!--      ElMessage.success('修改成功')-->
<!--      data.formVisible = false-->
<!--      loadData()-->
<!--    } else {-->
<!--      ElMessage.error(res.msg)-->
<!--    }-->
<!--  })-->
<!--}-->

<!--const handleExport = () => {-->
<!--  //下载流文件，不是JSON文件-->
<!--  window.open('http://localhost:8080/user/export')-->
<!--  //打开流链接，浏览器帮忙下载-->
<!--}-->

<!--</script>-->

<!--<style scoped></style>-->
<template>
  <div class="card" style="margin: 5px;">
    <h1>文章数据展示</h1>
    <el-input v-model="data.title" placeholder="请输入标题" :prefix-icon="Search" clearable></el-input>
    <el-button type="primary" @click="loadData" style="margin-top: 10px;">查询</el-button>
    <el-button type="primary" @click="resetData" style="margin-top: 10px;">重置</el-button>
  </div>
  <div class="card" style="margin: 5px;">
    <el-button type="primary" @click="handleAdd" >
      <Edit />新增文章
    </el-button>
    <el-button type="danger" @click="handleBatchDelete">
      <Delete />批量删除
    </el-button>
    <el-upload style="margin-top: 10px;" name="file" accept=".xls,.xlsx" action="http://localhost:8080/article/batchInsert" :show-file-list="false" :on-success="batchInsertSuccess">
      <el-button type="info">导入</el-button>
      <div style="margin: 10px;">请选择要导入的Excel文件</div>
    </el-upload>
    <el-button style="margin-top: 10px;" type="success" @click="handleExportClick">导出</el-button>
  </div>
  <div class="card" style="margin: 5px;">
    <el-table :data="data.tableData" stripe style="width: 100%" highlight-current-row
              @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="img" label="封面">
        <template #default="scope">
          <el-image v-if="scope.row.img" :src="scope.row.img" :preview-src-list="[scope.row.img]" preview-teleported
                    style="display:block; width: 40px; height: 40px; border-radius: 50%;" />
        </template>
      </el-table-column>
      <el-table-column prop="description" label="描述" show-overflow-tooltip />
      <el-table-column label="内容">
        <template #default="scope">
          <el-button type="text" @click="view(scope.row.content)">查看内容</el-button>
        </template>
      </el-table-column>
      <el-table-column prop="time" label="发布时间" />
      <el-table-column label="操作" width="120">
        <template #default="scope">
          <el-button circle type="primary" :icon="Edit" @click="handleEdit(scope.row)"></el-button>
          <el-button circle type="danger" :icon="Delete" @click="handleDelete(scope.row.id)"></el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination @size-change="loadData" @prev-click="loadData" @next-click="loadData" @current-change="loadData"
                   v-model:current-page="data.pageNum" v-model:page-size="data.pageSize" :page-sizes="[5, 10, 15, 20]" background
                   layout="total, sizes, prev, pager, next, jumper" :total=data.total />
  </div>
  <div>
    <el-dialog v-model="data.formVisible" title="文章信息" width="50%" destroy-on-close>
      <el-form ref="formRef" :rules="data.rules" :model="data.form" label-width="80px"
               style="padding-right: 40px; padding-top:20px">
        <el-form-item label="标题" prop="title" placeholder="请输入标题">
          <el-input v-model="data.form.title" autocomplete="off" />
        </el-form-item>
        <el-form-item label="封面">
          <el-upload action="http://localhost:8080/files/upload"  :headers="{token: data.user.token}" :file-list="fileList" list-type="picture" :on-success="handleImgSuccess">
            <el-icon type="primary">上传封面</el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item label="描述" prop="description" placeholder="请输入描述">
          <el-input type="textarea" :rows="3" v-model="data.form.description" autocomplete="off" />
        </el-form-item>
        <el-form-item label="内容">
          <div style="border: 1px solid #ccc; width: 100%;">
            <Toolbar style="border-bottom: 1px solid #ccc" :editor="editorRef" :mode="mode" />
            <Editor style="height:500px; overflow-y: hidden;" v-model="data.form.content" :mode="mode"
                    :editorConfig="editorConfig" @onCreated="handleCreated" />
          </div>
        </el-form-item>
        <!--        <el-form-item label="发布时间">-->
        <!--          <el-input v-model="data.form.time" autocomplete="off" placeholder="请输入发布时间" />-->
        <!--        </el-form-item>-->
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="data.formVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSave">保存</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog title="内容" v-model="data.viewVisible" width="50%" :close-on-click-modal="false" destroy-on-close>
      <div class="editor-content-view" style="padding: 20px" v-html="data.content"></div>
      <template #footer>
        <span class="dialog-footer">
          <el-button type="primary" @click="data.viewVisible = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { onBeforeUnmount, reactive, ref } from 'vue';
import { Search, Edit, Delete } from '@element-plus/icons-vue';
import service from '@/utils/request';
import { ElMessage, ElMessageBox } from 'element-plus';

import '@wangeditor/editor/dist/css/style.css'
import { shallowRef } from 'vue';
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import '@/assets/view.css'

const formRef = ref(null)

const data = reactive({
  user: JSON.parse(localStorage.getItem('grantedUser') || '{}'),
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
  content: null,
  rules: {
    title: [
      { required: true, message: '请输入标题', trigger: 'blur' },
      { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
    ],
    description: [
      { required: true, message: '请输入描述', trigger: 'blur' },
      { min: 2, max: 200, message: '长度在 2 到 200 个字符', trigger: 'blur' }
    ],
    content: [
      { required: true, message: '请输入内容', trigger: 'blur' },
      { min: 2, max: 5000, message: '长度在 2 到 5000 个字符', trigger: 'blur' }
    ]
  }
})

/* wangEditor5 初始化开始 */
const baseUrl = 'http://localhost:8080'
const editorRef = shallowRef(null) // 编辑器实例
const mode = ref('default') // 编辑器模式
const editorConfig = { MENU_CONF: {} } // 编辑器默认配置
//图片上传配置
editorConfig.MENU_CONF['uploadImage'] = {
  server: baseUrl + '/files/wang/upload', //上传接口
  fieldName: 'file' //服务端图片上传接口参数
}

//组件销毁时，也及时销毁 wangEditor5 实例
onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor == null) return
  editor.destroy()
})
//记录editor实例
const handleCreated = (editor) => {
  editorRef.value = editor
}
/* wangEditor5 初始化结束 */

const view = (content) => {
  data.content = content
  data.viewVisible = true
}

const loadData = () => {
  service.get('/article/selectPage', {
    params: {
      authorId: data.user.uid,
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      title: data.title
    }
  }).then(res => {
    data.tableData = res.data.list
    data.total = res.data.total
  })
}
loadData()

const resetData = () => {
  data.title = null
  loadData()
}

const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))  //深拷贝，防止修改原始数据
  data.formVisible = true
}

const handleAdd = () => {
  data.formVisible = true
  data.form = {}
}

const handleImgSuccess = (res) => {
  data.form.img = res.data
}

const handleDelete = (id) => {
  ElMessageBox.confirm('确认删除该条数据吗？', '确认删除', { type: 'warning' }).then(() => {
    service.delete('/article/delete/' + id).then((res) => {
      if (res.code === "200") {
        ElMessage.success('删除成功')
        loadData()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch()
}

const handleSelectionChange = (rows) => {
  // console.log(rows)
  data.ids = rows.map(item => item.id)
}

const handleBatchDelete = () => {
  if (data.ids.length === 0) {
    ElMessage.error('请先选择要删除的数据')
    return
  }
  ElMessageBox.confirm('确认批量删除选中数据吗？', '确认批量删除', { type: 'warning' }).then(() => {

    //取出选中行的id组成数组
    service.delete('/article/batchDelete', { data: data.ids }).then((res) => {
      if (res.code === "200") {
        ElMessage.success('批量删除成功')
        loadData()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch()
}

const handleSave = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      if (data.form.id) {
        update()
      } else {
        add()
        // ElMessage.error('只能修改，不能新增')
      }
    } else {
      return false
    }
  })
}

const add = () => { //新增的对象没有id
  data.form.authorId = data.user.uid
  // data.form.time = new Date().toLocaleString()
  service.post('/article/insert', data.form).then((res) => {
    if (res.code === "200") {
      ElMessage.success('新增成功')
      data.formVisible = false
      loadData()
    } else {
      ElMessage.error(res.msg)
    }
  })
}
const update = () => { //修改的对象有id

  service.put('/article/update', data.form).then((res) => {
    if (res.code === "200") {
      ElMessage.success('修改成功')
      data.formVisible = false
      loadData()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const batchInsertSuccess = (res) => {
  if (res.code === "200") {
    ElMessage.success('导入成功')
    data.formVisible = false
    loadData()
  } else {
    ElMessage.error(res.msg)
  }
}

const handleExportClick = () => {
  //下载流文件，不是JSON文件
  window.open('http://localhost:8080/article/exportWithAuthorId')
  //打开流链接，浏览器帮忙下载
}

</script>

<style scoped></style>