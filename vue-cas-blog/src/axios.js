import axios from 'axios'
import router from "./router";
import { ElMessage } from 'element-plus'

axios.defaults.baseURL='https://xxxxxxxxx.top/api_blog'


axios.interceptors.request.use(config => {
    // 可以统一设置请求头
    return config
})

axios.interceptors.response.use(response => {
        let res = response.data;

        if (res.code == 200) {
            return response
        } else {
            console.log(response)
            ElMessage.error(response.data.msg)
            return Promise.reject(response.data.msg)
        }
    },
    error => {
        console.log(error)
        if (error.response.data) {
            error.message = error.response.data.msg
        }
        if (error.response.code == 401) {
            router.push("/login")
        }
        ElMessage.error(error.message)
        return Promise.reject(error)
    }
)