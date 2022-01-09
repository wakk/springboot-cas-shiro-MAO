import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import axios from 'axios'
import './axios.js' // 请求拦截

const app = createApp(App)
app.use(store).use(router).use(ElementPlus).mount('#app')
axios.defaults.withCredentials=true//axios携带cookies
app.config.globalProperties.$axios=axios

// 路由判断登录 根据路由配置文件的参数
router.beforeEach((to, from, next) => {
    if (to.meta.requireAuth) { // 判断跳转的路由是否需要登录
        axios.get('/user/headInfo').then(res => {
            const check = res.data.object.username;
            if (check) { // vuex.state判断token是否存在
                next() // 已登录
            } else {
                next({
                    path: '/login',
                    query: {redirect: to.fullPath} // 将跳转的路由path作为参数，登录成功后跳转到该路由
                })
            }
        });
    } else {
        next()
    }
})