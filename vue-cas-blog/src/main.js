import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import axios from 'axios'
import './axios.js' // 请求拦截
import VMdEditor from '@kangc/v-md-editor/lib/codemirror-editor';
//
import VMdPreview from '@kangc/v-md-editor/lib/preview';
import '@kangc/v-md-editor/lib/style/codemirror-editor.css';
import vuepressTheme from '@kangc/v-md-editor/lib/theme/vuepress.js';
import '@kangc/v-md-editor/lib/theme/style/vuepress.css';
import Prism from 'prismjs';

// highlightjs
import hljs from 'highlight.js';

// codemirror 编辑器的相关资源
import Codemirror from 'codemirror';
// mode
import 'codemirror/mode/markdown/markdown';
import 'codemirror/mode/javascript/javascript';
import 'codemirror/mode/css/css';
import 'codemirror/mode/htmlmixed/htmlmixed';
import 'codemirror/mode/vue/vue';
// edit
import 'codemirror/addon/edit/closebrackets';
import 'codemirror/addon/edit/closetag';
import 'codemirror/addon/edit/matchbrackets';
// placeholder
import 'codemirror/addon/display/placeholder';
// active-line
import 'codemirror/addon/selection/active-line';
// scrollbar
import 'codemirror/addon/scroll/simplescrollbars';
import 'codemirror/addon/scroll/simplescrollbars.css';
// style
import 'codemirror/lib/codemirror.css';

VMdEditor.Codemirror = Codemirror;
VMdEditor.use(vuepressTheme, {
    Prism,
    Hljs: hljs,
});
VMdPreview.use(vuepressTheme, {
    Prism,
    Hljs: hljs,
});

const app = createApp(App)
app.use(store).use(router).use(VMdEditor).use(VMdPreview).use(ElementPlus).mount('#app')
axios.defaults.withCredentials=true//axios携带cookies
app.config.globalProperties.$axios=axios



// 路由判断登录 根据路由配置文件的参数
router.beforeEach((to, from, next) => {
    if (to.meta.requireAuth) { // 判断跳转的路由是否需要登录
        axios.get('https://xxxxxxxxx.top/api_mao/user/headInfo').then(res => {
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