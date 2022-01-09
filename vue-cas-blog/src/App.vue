<template>
  <div>
    <Head id="app-head"></Head>
    <div id="container" class="container">
      <router-view></router-view>
    </div>
  </div>
</template>

<style>
#app {
  height: 100%;
  width: 100%;
  position: relative;
  padding: 0 0;
  margin: 0 auto;
}

body {
  margin:0 auto;
  height: 100%;
  width: 100%;
  display: block;
  /*background-color: #f8f7f7;*/
}

.container {
  position: relative;
  margin-top: 60px;
  width: 100%;
  height: 100%;
}
</style>
<script>
import Head from "@/components/Head";

export default {
  components: {Head}
}
</script>