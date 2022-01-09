<template>
  <el-header style="text-align: right;font-size: 15px;color: aliceblue">
    <router-link :to="{name:'Blog'}">
      <img class="head-logo" src="../assets/BLOG.png" fit="cover">
    </router-link>
    <div style="height: 60px;float:right">
      <span>{{ username }}</span>
      <el-dropdown size="small">
        <el-icon style="margin-left: 15px;margin-top: 20px;color:aliceblue">
          <setting/>
        </el-icon>
        <template #dropdown>
          <el-dropdown-menu v-if="username">
            <router-link :to="{name:'PrivateBlog', params: {userId: this.userId}}" style="text-decoration: none;">
              <el-dropdown-item>个人博客</el-dropdown-item>
            </router-link>
            <router-link :to="{name:'Collection', params: {userId: this.userId}}" style="text-decoration: none;">
              <el-dropdown-item>收藏</el-dropdown-item>
            </router-link>
            <router-link :to="{name:'Logout'}" style="text-decoration: none;">
              <el-dropdown-item>注销</el-dropdown-item>
            </router-link>
          </el-dropdown-menu>
          <el-dropdown-menu v-else>
            <router-link :to="{name:'Login'}" style="text-decoration: none;">
              <el-dropdown-item>登录</el-dropdown-item>
            </router-link>
            <router-link :to="{name:'Register'}" style="text-decoration: none;">
              <el-dropdown-item>注册</el-dropdown-item>
            </router-link>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
    <div style="float:right;height:60px;margin-top: 10px;margin-right: 20px">
      <router-link v-if="username" :to="{name:'PrivateBlog', params: {userId: this.userId}}">
        <el-avatar :src="avatar">{{ icon }}</el-avatar>
      </router-link>
      <el-avatar v-else src="https://xxxxxxxxx.top:9090/vueblog/user.png"></el-avatar>
    </div>

  </el-header>
</template>


<script>
import {Message, Setting} from '@element-plus/icons'
import axios from 'axios'

export default {
  name: "Head",
  components: {
    Message,
    Setting
  },
  data() {
    return {
      userId: "1",
      username: "1",
      icon: "user", avatar: "",//icon和avatar只能有一个生效

    }
  },
  methods:{
    // skipToPrivateBlog(userId){
    //   // window .push("/blog/home/" + userId)
    //   window.open("https://xxxxxxxxx.top/myblog/blog/home/" + userId,"_self")
    // }
  },
  created() {
    const _this = this;
    axios.create({
      withCredentials: true,
      // headers: {
      //   Authorization: token
      // }
    }).get('https://xxxxxxxxx.top/api_mao/user/headInfo').then(res => {
      console.log(res.data.object)
      _this.userId=res.data.object.id;
      _this.username = res.data.object.username;
      _this.avatar = res.data.object.avatar;
    });
  }

}
</script>

<style scoped>
.el-header {
  background-color: #000000;
  color: var(--el-text-color-primary);
  line-height: 60px;
  position: fixed;
  width: 100%;
  height: 60px;
  top: 0px;
  z-index: 100;
}

.head-logo {
  width: 262px;
  height: 56px;
  float: left;
  margin-top: 3px;
}

@media (max-width: 992px) {
  .head-logo {
    width: 150px;
  }
}
</style>