<template>
  <div>
    <el-row :gutter="10">
      <el-col :span="12" style="margin-bottom: 10px">
        <div class="card" style="padding: 20px; height: 400px;" id="bar"></div>
      </el-col>
      <el-col :span="12" style="margin-bottom: 10px">
        <div class="card" style="padding: 20px; height: 400px;" id="line"></div>
      </el-col>
      <el-col :span="12" style="margin-bottom: 10px">
        <div class="card" style="padding: 20px; height: 400px;" id="pie"></div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { onMounted, reactive } from 'vue';
import * as echarts from 'echarts';
import service from '@/utils/request.js';

// 指定图表的配置项和数据
const barOption = {
  title: {
    text: '各部门人数'
  },
  tooltip: {},
  legend: {
    data: ['人数']
  },
  xAxis: {
    data: []
  },
  yAxis: {},
  series: [
    {
      name: '人数',
      data: [],
      type: 'bar',
      itemStyle: {
        color: function (params) {
          let colorList = ['#c23531', '#2f4554', '#61a0a8', '#d48265', '#749f83', '#ca8622', '#bda29a', '#6e7074', '#546570', '#c4ccd3'];
          return colorList[params.dataIndex % colorList.length]
        }
      }
    }
  ]
};
const lineOption = {
  title: {
    text: '近七天每日发文数量'
  },
  tooltip: {},
  legend: {
    trigger: 'item'
  },
  xAxis: {
    data: []
  },
  yAxis: {},
  series: [
    {
      name: '发文数量',
      data: [],
      type: 'line',
      smooth: true,
    }
  ]
};

const pieOption = {
  title: {
    text: '各部门员工数量占比',
    left: 'center'
  },
  tooltip: {
    trigger: 'item',
  },
  legend: {
    orient: 'vertical',
    left: 'left',
  },
  series: [
    {
      name: '员工数量占比',
      type: 'pie',
      radius: '55%',
      data: [],
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
      }
    }
  ]
};

const data = reactive({

});

onMounted(() => {
  // 基于准备好的dom，初始化echarts实例
  const barChart = echarts.init(document.getElementById('bar'));
  service.get('/getBarData').then(res => {
    //console.log(res.data);
    barOption.xAxis.data = res.data.department;
    barOption.series[0].data = res.data.count;
    barChart.setOption(barOption);
  });
  const lineChart = echarts.init(document.getElementById('line'));
  service.get('/getLineData').then(res => {
    //console.log(res.data);
    lineOption.xAxis.data = res.data.date;
    lineOption.series[0].data = res.data.count;
    lineChart.setOption(lineOption);
  });
  const pieChart = echarts.init(document.getElementById('pie'));
  service.get('/getPieData').then(res => {
    //console.log(res.data);
    pieOption.series[0].data = res.data;
    pieChart.setOption(pieOption);
  });

});






</script>

<style scoped></style>
