import { createRouter, createWebHistory } from 'vue-router'
import Login from "@/views/Login";
import Register from "@/views/Register";
import Blog from "@/views/Blog";
import BlogDetail from "@/views/BlogDetail";
import BlogEdit from "@/views/BlogEdit";
import PrivateBlog from "@/views/PrivateBlog";
import NotFound from "@/views/NotFound";
import Logout from "@/views/Logout";
import Collection from "@/views/Collection";

const routes = [
  {
    path: '/',
    name: 'Index',
    redirect: "/blog"
  },
  {
    path: '/blog',
    name: 'Blog',
    component: () => import('../views/Blog.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: Register
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/logout',
    name: 'Logout',
    component: Logout,
    meta: {
      requireAuth: true
    }
  },
  {
    path: '/blog/add',
    name: 'BlogAdd',
    component: BlogEdit,
    meta: {
      requireAuth: true
    }
  },
  {
    path: '/blog/detail/:blogId',
    name: 'BlogDetail',
    component: BlogDetail
  },
  {
    path: '/blog/edit/:blogId',
    name: 'BlogEdit',
    component: BlogEdit,
    meta: {
      requireAuth: true
    }
  },
  {
    path: '/blog/home/:userId',
    name: 'PrivateBlog',
    component: PrivateBlog,
  },
  {
    path: '/blog/collection/:userId',
    name: 'Collection',
    component:Collection,
    meta: {
      requireAuth: true
    }
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
