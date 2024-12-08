<template>
    <el-button type="primary" @click="router.back()">返回主页</el-button>
</template>

<script setup>
import { reactive } from 'vue'
import router from '@/router/index.js';

const data = reactive({
    id: router.currentRoute.value.query.id,
    name: router.currentRoute.value.query.name,
})

</script>