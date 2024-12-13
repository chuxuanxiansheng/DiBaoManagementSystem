import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    // { path: '/', redirect: '/login'},
    { path: '/', redirect: '/dashboard'},
    { path: '/dashboard', name: 'dashboard', meta: { title: '首页' }, component: () => import('@/views/Dashboard.vue')},
    { path: '/home', meta: { title: '个人中心' }, component: () => import('@/views/HomePage.vue'), children: [
    { path: 'profile', name: 'profile', meta: { title: '信息修改' }, component: () => import('@/views/Profile.vue')},
    { path: 'password', name: 'password', meta: { title: '修改密码' }, component: () => import('@/views/Password.vue')},
    { path: 'user', name: 'user', meta: { title: '用户数据展示' }, component: () => import('@/views/UserDataShow.vue')},
     {path:'allarticle',name:'allarticle',meta:{title:'全部文章'},component:()=>import('@/views/AllArticleDataShow.vue')},
     { path: 'dataAnalysis', name: 'dataAnalysis', meta: { title: '数据展示' }, component: () => import('@/views/DataAnalysis.vue')},
    { path: 'article', name: 'article', meta: { title: '文章数据展示' }, component: () => import('@/views/ArticleDataShow.vue')},
    { path: 'department', name: 'department', meta: { title: '部门数据展示' }, component: () => import('@/views/DepartmentDataShow.vue')},
    ]},
    { path: '/login', name: 'login', meta: { title: '登录' }, component: () => import('@/views/Login.vue')},
    { path: '/signup', name: 'signup', meta: { title: '注册' }, component: () => import('@/views/Signup.vue')},
    { path: '/404', name: '404', meta: { title: '404' }, component: () => import('@/views/404.vue')},
    { path: '/nearby-news', name: 'NearbyNews', component: () => import('@/views/NearbyNews.vue') },
    { path: '/feedback', name: 'Feedback', component: () => import('@/views/Feedback.vue') },
    { path: '/:pathMatch(.*)*', redirect: '/404' },
    {
      path: '/news/:id',
      name: 'NewsDetail',
      component: ()=>import('@/views/NewsDetail.vue')
    }
  ]
})
router.beforeEach((to, from, next) => {
	document.title = to.meta.title;
	next();
})
export default router
