<template>
<div>
  <!-- 固定于屏幕右下方的一个悬浮按钮 -->
  <div class="fixed-action-btn">
    <a class="btn-floating btn-large blue z-depth-4">
      <i class="large material-icons">apps</i>
    </a>
    <ul>
      <!-- 文章目录按钮 -->
      <li class="category-btn hide">
        <a class="sidenav-trigger btn-floating blue lighten-2" data-target="category">
          <i class="material-icons">format_list_bulleted</i>
        </a>
      </li>
      <li>
        <el-link class="btn-floating blue lighten-2" href="/myblog/blog/add">
          <i class="material-icons">edit</i>
        </el-link>
      </li>
      <!-- 下面也可以添加其他按钮，如返回文章顶部等-->
      <li>
        <a class="btn-floating blue lighten-2" href="javascript: scrollTo(0, 0);">
          <i class="material-icons">publish</i>
        </a>
      </li>
      <li>
        <a class="btn-floating blue lighten-2" href="javascript: scrollTo(0, document.body.clientHeight);" style="opacity: 0; transform: scale(0.4) translateY(40px) translateX(0px);">
          <i class="material-icons">file_download</i>
        </a>
      </li>
    </ul>
  </div>
  <!-- 文章目录侧栏 -->
  <ul id="category" class="sidenav no-autoinit grey lighten-4 grey-text text-darken-3 ">
    <li><p class="center-align">目录</p></li>
  </ul>
</div>
</template>


<script scoped>
import 'materialize-css/dist/js/materialize.min.js'

export default {
  name: "FloatButton",
  components:{
  },
  methods:{
    refreshNav(){
      M.AutoInit();
      let elemCategory = document.querySelector('#category');
      // document.addEventListener('DOMContentLoaded', function () {
      //   M.Sidenav.init(elemCategory, {
      //     'edge': 'right' // right 表示在右侧栏显示，left 则表示在左边显示
      //   });
      // });
      M.Sidenav.init(elemCategory, {
        'edge': 'right' // right 表示在右侧栏显示，left 则表示在左边显示
      });
      this.getSideNav();
    },
    getSideNav(){
      let postContent = document.querySelector("#preview");
      if (postContent) {
        let categories = postContent.querySelectorAll('h1, h2, h3, h4, h5, h6');
        let idInt = 1;
        console.log("querySelectorAll:" + categories.length)

        if (categories.length > 0) { // 文章存在标题
          var category = document.querySelector('#category'),
              categoryBtn = document.querySelector('.category-btn');
          var li = document.createElement('li'),
              a = document.createElement('a');
          a.className = 'waves-effect';
          // 存在目录则显示目录按钮和侧栏
          category.classList.remove('hide');
          categoryBtn.classList.remove('hide');

          categories.forEach(node => {
            // 每次 cloneNode 取代 createElement
            // 因为克隆一个元素快于创建一个元素
            var _li = li.cloneNode(false),
                _a = a.cloneNode(false);
            node.setAttribute('id', 'blog-h-' + idInt);
            idInt++;
            _a.innerText = node.innerText;
            // 为标题设置跳转链接
            // _a.href =void(0);
            const _this=this
            _a.addEventListener('click',function (ev) {
              _this.goAuchor('#' + node.id)
            });
            _li.appendChild(_a);
            // 为不同级别标题应用不同的缩进
            _li.style.paddingLeft = node.nodeName.slice(-1) * 32 + 'px';
            category.appendChild(_li);
          })
        }
      }
    },
    goAuchor(id){
      var auchor=document.querySelector(id)
      auchor.scrollIntoView(true)
      window.scrollTo(0, auchor.offsetTop -20);
    }
  },
  mounted() {
    this.refreshNav();
  },

}
</script>
<style scoped src="materialize-css/dist/css/materialize.min.css"/>
<style src="../assets/css/myMaterialize.css"/>
<style scoped src="../assets/css/icons.css"/>


