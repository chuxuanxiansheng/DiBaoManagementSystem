<template>
  <div class="card" style="margin: 5px;">
    <h1>Data Show</h1>
    <p>This is the data show page.</p>
    <el-input v-model="data.name" placeholder="请输入内容" :prefix-icon="Search" clearable></el-input>
    <el-button type="primary" @click="loadData" style="margin-top: 5px;">查询</el-button>
    <el-button type="primary" @click="resetData" style="margin-top: 5px;">重置</el-button>
    <!-- <p>输入的内容为：{{ data.name }}</p> -->
  </div>
  <div class="card" style="margin: 5px;">
    <el-button type="primary" @click="handleAdd">
      <Edit />新增神仙
    </el-button>
    <el-button type="danger" @click="handleBatchDelete">
      <Delete />批量删除
    </el-button>
  </div>
  <div class="card" style="margin: 5px;">
    <el-table :data="data.tableData" stripe style="width: 100%" highlight-current-row @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <!-- <el-table-column prop="id" label="ID" width="180" /> -->
      <el-table-column prop="name" label="名称" width="180" />
      <el-table-column prop="gender" label="性别" width="180" />
      <el-table-column prop="status" label="地位" width="180" />
      <el-table-column prop="description" label="描述" width="180" show-overflow-tooltip />
      <el-table-column label="操作" width="140">
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
    <el-dialog v-model="data.formVisible" title="新增神仙" width="500px">
      <el-form :model="data.form" label-width="80px" style="padding-right: 40px; padding-top:20px">
        <!-- <el-form-item label="ID">
          <el-input v-model="data.form.id" autocomplete="off" placeholder="请输入ID" />
        </el-form-item> -->
        <el-form-item label="名称" placeholder="请输入名称">
          <el-input v-model="data.form.name" autocomplete="off" />
        </el-form-item>
        <el-form-item label="性别">
          <el-radio-group v-model="data.form.gender">
            <el-radio label="男">男</el-radio>
            <el-radio label="女">女</el-radio>
          </el-radio-group>
          <!-- <el-input-number style="width: 180px;" :min="18" :max="70" v-model="data.form.age" autocomplete="off"
            placeholder="请输入年龄" /> -->
        </el-form-item>
        <el-form-item label="地位">
          <el-input v-model="data.form.status" autocomplete="off" placeholder="请输入地位" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input :rows="3" type="textarea" v-model="data.form.description" autocomplete="off" placeholder="请输入描述" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="data.formVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSave">保存</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive } from 'vue';
import { Search, Edit, Delete } from '@element-plus/icons-vue';
import service from '@/utils/request';
import { ElMessage, ElMessageBox } from 'element-plus';

const data = reactive({
  tableData: [],
  name: null,
  dialogVisible: false,
  formVisible: false,
  pageNum: 1,
  pageSize: 5,
  total: 0,
  form: null,
  ids: []
})


const loadData = () => {
  service.get('/god/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      name: data.name
    }
  }).then(res => {
    data.tableData = res.data.list
    data.total = res.data.total
  })
}
loadData()

const resetData = () => {
  data.name = null
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

const handleDelete = (id) => {
  ElMessageBox.confirm('确认删除该条数据吗？', '确认删除', { type: 'warning' }).then(() => {
    service.delete('/god/delete/' + id).then((res) => {
      if (res.code === "200") {
        ElMessage.success('删除成功')
        data.formVisible = false
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
    service.delete('/god/batchDelete', { data: data.ids }).then((res) => {
      if (res.code === "200") {
        ElMessage.success('批量删除成功')
        data.formVisible = false
        loadData()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch()
}

const handleSave = () => {
  if (data.form.id) {
    update()
  } else {
    add()
  }
}

const add = () => { //新增的对象没有id
  service.post('/god/insert', data.form).then((res) => {
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

  service.put('/god/update', data.form).then((res) => {
    if (res.code === "200") {
      ElMessage.success('修改成功')
      data.formVisible = false
      loadData()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

</script>

<style scoped></style>
