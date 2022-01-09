import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import Register from "@/views/Register";
import NotFound from "@/views/NotFound";
import Login from "@/views/Login";
import Logout from "@/views/Logout";
import UserInfo from "@/views/UserInfo";


const routes = [
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/logout',
    name: 'Logout',
    component: Logout
  },
  {
    path: '/userInfo',
    name: 'UserInfo',
    component: UserInfo,
    meta: {
      requireAuth: true
    },
  },
  {
    path: '/register',
    name: 'Register',
    component: Register
  },
  {
    path: '/',
    name: 'Index',
    redirect: "/home"
  },
  {
    path: '/home',
    name: 'Home',
    component: Home
  },
  {
    path: "/:pathMatch(.*)*",
    component: NotFound
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
