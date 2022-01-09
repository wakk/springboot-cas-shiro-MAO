<template>
  <el-header style="text-align: right;font-size: 15px;color: aliceblue">
    <router-link :to="{name:'Home'}">
    <img class="head-logo" src="../assets/MAO.png" fit="cover">
    </router-link>
      <div style="height: 60px;float:right">
      <span>{{ username }}</span>
      <el-dropdown size="small">
        <el-icon style="margin-left: 15px;margin-top: 20px;color:aliceblue">
          <setting/>
        </el-icon>
        <template #dropdown>
          <el-dropdown-menu v-if="username">
            <router-link :to="{name:'UserInfo'}" style="text-decoration: none;">
            <el-dropdown-item >个人信息</el-dropdown-item>
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
            <el-dropdown-item >注册</el-dropdown-item>
            </router-link>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
    <div  style="float:right;height:60px;margin-top: 10px;margin-right: 20px">
      <el-avatar v-if="username" :src="avatar">{{ icon }}</el-avatar>
      <el-avatar v-else src="https://xxxxxxxxxx.top:9090/vueblog/user.png"></el-avatar>
    </div>

  </el-header>
</template>


<script>
import {Message, Setting} from '@element-plus/icons'

export default {
  name: "Head",
  components: {
    Message,
    Setting
  },
  data() {
    return {
      username: "",
      icon: "user", avatar: "",//icon和avatar只能有一个生效

    }
  },
  created() {
    const _this=this;
    this.$axios('/user/headInfo').then(res =>{
      _this.username=res.data.object.username;
      _this.avatar=res.data.object.avatar;
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
.head-logo{
  width: 262px; height: 56px;float: left;margin-top: 3px;
}
@media (max-width: 992px) {
  .head-logo{
    width: 150px;
  }
}

</style>