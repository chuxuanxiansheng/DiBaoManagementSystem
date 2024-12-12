<template>
  <div class="card" style="margin: 5px;">
    <h1>部门数据展示</h1>
    <el-input v-model="data.name" placeholder="请输入内容" :prefix-icon="Search" clearable></el-input>
    <el-button type="primary" @click="loadData" style="margin-top: 10px;">查询</el-button>
    <el-button type="primary" @click="resetData" style="margin-top: 10px;">重置</el-button>
  </div>
  <div class="card" style="margin: 5px;">
    <el-button type="primary" @click="handleAdd">
      <Edit />新增部门
    </el-button>
    <el-button type="danger" @click="handleBatchDelete">
      <Delete />批量删除
    </el-button>
  </div>
  <div class="card" style="margin: 5px;">
    <el-table :data="data.tableData" border style="width: 100%" highlight-current-row
      @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="部门ID" />
      <el-table-column prop="name" label="部门名" />
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
    <el-dialog v-model="data.formVisible" title="部门信息" width="500px" destroy-on-close>
      <el-form ref="formRef" :rules="data.rules" :model="data.form" label-width="80px" style="padding-right: 40px; padding-top:20px">
        <el-form-item label="部门名" prop="name" placeholder="请输入名称">
          <el-input v-model="data.form.name" autocomplete="off" />
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
import { reactive, ref } from 'vue';
import { Search, Edit, Delete } from '@element-plus/icons-vue';
import service from '@/utils/request';
import { ElMessage, ElMessageBox } from 'element-plus';

const formRef = ref(null)

const data = reactive({
  user: JSON.parse(localStorage.getItem('grantedUser') || '{}'),
  tableData: [],
  name: null,
  dialogVisible: false,
  formVisible: false,
  pageNum: 1,
  pageSize: 5,
  total: 0,
  form: null,
  ids: [],
  rules: {
    name: [
      { required: true, message: '请输入用户名', trigger: 'blur' },
      // { min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur' }
    ]
  }
})


const loadData = () => {
  service.get('/department/selectPage', {
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

const handleAvatarSuccess = (res) => {
    data.form.avatar = res.data
}

const handleDelete = (id) => {
  ElMessageBox.confirm('确认删除该条数据吗？', '确认删除', { type: 'warning' }).then(() => {
    service.delete('/department/delete/' + id).then((res) => {
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
    service.delete('/department/batchDelete', { data: data.ids }).then((res) => {
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
  formRef.value.validate((valid) => {
    if (valid) {
      if (data.form.id) {
        update()
      } else {
        add()
        //ElMessage.error('只能修改，不能新增')
      }
    } else {
      return false
    }
  })
}

const add = () => { //新增的对象没有id
  service.post('/department/insert', data.form).then((res) => {
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

  service.put('/department/update', data.form).then((res) => {
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
