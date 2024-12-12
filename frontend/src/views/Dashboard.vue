<template>
  <div class="card" style="margin: 5px;">
    <div>
      <h1>文章数据展示</h1>
      <el-button type="primary" @click="handleLoginClick" style="margin-top: 10px;">登录</el-button>
      <el-button type="primary" @click="handleExportClick" style="margin-top: 10px;">导出所有数据</el-button>
    </div>
    <el-input v-model="data.title" placeholder="请输入标题" :prefix-icon="Search" clearable style="margin-top: 10px; width: 300px;"></el-input>
    <el-button type="primary" @click="getAllData" style="margin-top: 10px; margin-left: 10px;">查询</el-button>
    <el-button type="primary" @click="resetData" style="margin-top: 10px;">重置</el-button>
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
      <el-table-column prop="author" label="作者" />
    </el-table>
    <el-pagination @size-change="getAllData" @prev-click="getAllData" @next-click="getAllData" @current-change="getAllData"
                   v-model:current-page="data.pageNum" v-model:page-size="data.pageSize" :page-sizes="[5, 10, 15, 20]" background
                   layout="total, sizes, prev, pager, next, jumper" :total=data.total />
  </div>
  <div>
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
import { reactive, ref } from 'vue';
import { Search } from '@element-plus/icons-vue';
import service from '@/utils/request';

import '@wangeditor/editor/dist/css/style.css'
import '@/assets/view.css'
import router from "@/router/index.js";

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

const resetData = () => {
  data.title = null
  getAllData()
}

const handleSelectionChange = (rows) => {
  // console.log(rows)
  data.ids = rows.map(item => item.id)
}

const handleLoginClick = () => {
  window.location.href = '/login'
  //router.push('/login')
}

const handleExportClick = () => {
  //下载流文件，不是JSON文件
  window.open('http://localhost:8080/article/exportWithAuthor')
  //打开流链接，浏览器帮忙下载
}


</script>

<style scoped></style>
