<template>
  <div>
    <h1 class="m-content">404，找不到页面</h1>

  </div>
</template>
<script>

export default {
  name: "NotFound",
  created() {
    document.getElementById("app-head").style.display='none';
    document.getElementById("container").style.marginTop='0';
  },
}
</script>

<style scoped>
.m-content {
  margin: 0 auto;
  text-align: center;
}
</style>
