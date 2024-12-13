import axios from "axios";
import { ElMessage } from 'element-plus';
import router from "@/router/index.js";

// 配置新建一个 axios 实例
const service = axios.create({
    baseURL: "http://localhost:8080",
    timeout: 30000,
});

// 添加请求拦截器
// service.interceptors.request.use((config) => {
//   config.headers['Content-Type'] = 'application/json;charset=UTF-8';
//     config.headers['Authorization'] = 'Bearer ' + localStorage.getItem('grantedUser').token;
//     return config;
//   // let user = JSON.parse(localStorage.getItem('grantedUser') || '{}');
//   // console.log(user);
//   // config.headers['token'] = user.token;
//   // return config;
// }, (error) => {
//   console.log(error);
//   return Promise.reject(error);
// });
// 添加请求拦截器
//可以在请求发送前对请求做一些处理，比如设置 headers 信息，统一的错误处理，对请求参数统一加密等
service.interceptors.request.use((config) => {
    config.headers['Content-Type'] = 'application/json;charset=UTF-8';
    let user = JSON.parse(localStorage.getItem('grantedUser') || '{}');
    if (user?.token) {
        // config.headers['Authorization'] = 'Bearer ' + user.token;
        config.headers['token'] = user.token;
    }
    return config;
}, (error) => {
    console.error('请求错误', error);
    return Promise.reject(error);
});


// 添加响应拦截器
service.interceptors.response.use(
    response => {
        let res = response.data;
        if (typeof res === "string") {
            res = res ? JSON.parse(res) : res;
        }
        if (res.code === '401'){
            ElMessage.error('登录失效，请重新登录');
            router.push('/login');
            // localStorage.removeItem('grantedUser');
            // window.location.href = '/login';
            // return Promise.reject(res);
        }
        return res;
    },
    (error) => {
        console.log("response error", error);
        //   if (error.response.status === 404) {
        //     ElMessage.error('请求地址不存在');
        //   } else if (error.response.status === 500) {
        //     ElMessage.error('服务器内部错误');
        //   } else {
        //     ElMessage.error(error.response.data.message);
        //   }
        // } else {
        //   ElMessage.error('网络请求异常');
        // }
        return Promise.reject(error);
    }
);
// service.interceptors.response.use(
//   response => {
//     let res = response.data;
//     if (typeof res === "string") {
//       res = res ? JSON.parse(res) : res;
//     }
//     return res;
//   },
//   (error) => {
//   if(error.response.status === 404){
//     ElMessage.error('请求地址不存在');
//   }else if(error.response.status === 500){
//     ElMessage.error('服务器内部错误');
//   }else{
//     ElMessage.error(error.response.data.message);
//   }
//   return Promise.reject(error);
//   }
// );

export default service;
