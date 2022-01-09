<template>
  <el-menu
      class="el-menu-vertical-demo">
    <el-menu-item v-for="side in sides">
      <template #title>
        <div style="display: block;width: 100%;">
        <a class="SideSpan" :href="side.href">{{ side.title }}</a>
        </div>
      </template>
    </el-menu-item>
  </el-menu>
</template>

<script scope>
import {defineComponent} from 'vue'

export default defineComponent({
  name:"sidebar",
  props:{
    sides: {}
  }
})
</script>

<style>
.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 16.66666667%;
  height: 100%;
  position: fixed;
  left: 0px;
  top: 60px;
  background-color: #ffffff;
  box-shadow: 0 0 20px 0 #d4dee6;
  float: left;
  padding: 40px 0px 0px 0px;
  z-index: 10;
}
@media (max-width: 992px) {
  .el-menu-vertical-demo:not(.el-menu--collapse) {
    display: none;
  }
}
.SideSpan {
  color: #000000;
  font-size: 14px;
  line-height: 48px;
  height: 48px;
  text-decoration: none;
  padding-left: 20%;
  display: block;
  width: 95%;
}
</style>