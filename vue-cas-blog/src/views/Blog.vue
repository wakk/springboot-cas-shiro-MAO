<template>
  <div class="Blog">
    <h3 style="max-width: 960px;text-align: center;margin-top: 0px">博客首页</h3>
    <div class="input-suffix">
      <el-row :gutter="20">
        <el-input v-model="searchInput" placeholder="搜索作者、标题、描述" @input="handleSelect">
          <template #prefix>
            <el-icon class="el-input__icon">
              <search/>
            </el-icon>
          </template>
        </el-input>
      </el-row>
    </div>

    <div class="block">
      <el-timeline>
        <el-timeline-item :timestamp="filterBlogs[i].created" placement="top" v-for="i in filterIndex">
          <el-card class="my-card" slot="reference">
              <div style="padding-top: 0px">
                <div class="panel_image">
                    <el-avatar :size="50" style=" align-content: center;text-align: center;border-style: solid;
                          border-color: #b7b6b6; border-width: 1px;" :src="filterBlogs[i].avatar">user
                    </el-avatar>
                </div>
                <div style="margin-left: 80px;padding-top: 20px">
                  <h4 class="ellipsis-1"  style="margin: 0;">
                    <router-link v-if="searchInput" :to="{name: 'BlogDetail', params: {blogId: this.filterBlogs[i].id}}"
                                 v-html="filterBlogs[i].title"></router-link>
                    <router-link v-else :to="{name: 'BlogDetail', params: {blogId: this.filterBlogs[i].id}}">
                      {{ filterBlogs[i].title }}
                    </router-link>
                  </h4>
                  <p class="card-content ellipsis-2" v-if="searchInput" v-html="filterBlogs[i].description"></p>
                  <p class="card-content ellipsis-2" v-else>{{ filterBlogs[i].description }}</p>
                </div>
              </div>
              <div class="start">
                <h4 style="float: left;margin: 0;width: 80px;text-align: center"  v-if="searchInput" v-html="filterBlogs[i].username"></h4>
                <h4 style="float: left;margin: 0;width: 80px;text-align: center" v-else>{{filterBlogs[i].username}}</h4>
                <div style="text-align: right">
                <el-icon>
                  <star-filled style="color: deepskyblue"/>
                </el-icon>
                <div class="start-text">{{ filterBlogs[i].collectNum }}</div>
                </div>
              </div>
          </el-card>
        </el-timeline-item>
      </el-timeline>
    </div>

    <el-pagination v-if="filterBlogs.length>pageSize" class="mpage"
                   background
                   layout="prev, pager, next"
                   :current-page=currentPage
                   :page-size=pageSize
                   @current-change=page
                   :total="filterBlogs.length">
    </el-pagination>
  </div>
  <FloatButton/>

</template>

<script>
// @ is an alias to /src
import {Search, StarFilled} from '@element-plus/icons'
import FloatButton from "@/components/FloatButton";


export default {
  name: 'Blog',
  components: {
    Search,
    StarFilled,
    FloatButton
    },
  data() {
    return {
      blogs: [
        {
          id: 4,
          created: "",
          username: "",
          title: "",
          description: "",
          avatar: "",
          collectNum: 2
        },
        // {id: 4, created: "2018/4/12", username: "", title: "标题", description: "描述", avatar: "", collectNum: 0},

      ],
      filterBlogs: [],
      filterIndex: [],
      currentPage: 1,
      pageSize: 6,
      searchInput: '',
    }
  },
  methods: {
    page(currentPage) {
      this.filterIndex = [];
      const min = (currentPage - 1) * this.pageSize;
      const max = min + this.pageSize
      for (let i = min; i < this.filterBlogs.length && i < max; i++) {
        this.filterIndex.push(i)
      }
      this.currentPage = currentPage;
    },
    handleSelect(input) {
      if (input) {
        // 忽略输入大小写
        input = new RegExp(input, 'i');
        // 在所有文章作者、标题、描述中匹配查询值
        var index = 0;
        var find = false;
        this.filterBlogs = [];

        for (let i = 0; i < this.blogs.length; i++) {
          var mappingString = [];
          mappingString[0] = this.blogs[i].username;
          mappingString[1] = this.blogs[i].title;
          mappingString[2] = this.blogs[i].description;
          console.log("string+"+this.blogs)
          for (let j = 0; j < 3; j++) {
            if (mappingString[j].search(input) !== -1) {
              find = true;
              var indexContent = mappingString[j].search(input);
              var l = input.toString().length - 3;
              // 将匹配到内容的地方进行黄色标记，并包括周围一定数量的文本
              mappingString[j] = mappingString[j].slice(0, indexContent) +
                  '<mark>' + mappingString[j].slice(indexContent, indexContent + l) + '</mark>' +
                  mappingString[j].slice(indexContent + l, -1);
            }
          }
          if (find) {
            find = false;
            this.filterBlogs[index] = {...this.blogs[i]};
            this.filterBlogs[index].username = mappingString[0];
            this.filterBlogs[index].title = mappingString[1];
            this.filterBlogs[index++].description = mappingString[2];
          }
        }
      } else {
        this.filterBlogs = this.blogs
      }
      this.page(1);
    }
  },
  created() {
    const _this=this
    this.filterBlogs = this.blogs
    this.page(1);
    console.log("creat:"+this.blogs[0].title)
    this.$axios('/blog/blogDto').then(res => {
      _this.blogs= res.data.object.blogs;
      this.filterBlogs = this.blogs
      this.page(1);
      console.log("creat:"+this.blogs[0].title)

    });
  }
}
</script>
<style>
.mpage {
  margin: 0 auto;
  text-align: center;
}

.Blog {
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px 60px;
}

.input-suffix {
  margin-bottom: 16px;
  margin-left: 78px;
  margin-right: 10px;
}

.input-suffix:hover {
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.panel_image {
  width: 80px;
  height: 100%;
  float: left;
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 17px;
}

.el-card__body {
  padding: 0 !important;
  width: 100%;
  height: 100%;
}

.card-content {
  margin: 0;
  padding-top: 10px;
  font-size: 12px;
  max-height: 32px;
}
.ellipsis-2{
  overflow : hidden;/*必须结合的属性,当内容溢出元素框时发生的事情*/
  text-overflow: ellipsis;/*可以用来多行文本的情况下，用省略号“…”隐藏超出范围的文本 。*/
  display: -webkit-box;/*必须结合的属性 ，将对象作为弹性伸缩盒子模型显示 。*/
  -webkit-line-clamp: 2;/*用来限制在一个块元素显示的文本的行数。*/
  -webkit-box-orient: vertical;/*必须结合的属性 ，设置或检索伸缩盒对象的子元素的排列方式 。*/
  /*line-clamp: 2;*/
  color: gray;
}
.ellipsis-1{
  overflow : hidden;/*必须结合的属性,当内容溢出元素框时发生的事情*/
  text-overflow: ellipsis;/*可以用来多行文本的情况下，用省略号“…”隐藏超出范围的文本 。*/
  display: -webkit-box;/*必须结合的属性 ，将对象作为弹性伸缩盒子模型显示 。*/
  -webkit-line-clamp: 1;/*用来限制在一个块元素显示的文本的行数。*/
  -webkit-box-orient: vertical;/*必须结合的属性 ，设置或检索伸缩盒对象的子元素的排列方式 。*/
  /*line-clamp: 2;*/
}
.ellipsis-1 a{
  text-decoration: none;
  color: black;
  font-size: larger;
}
.start{
  height: 14px;
  padding-top: 5px;
}

.start-text {
  height: 14px;
  position: relative;
  font-size: small;
  float: right;
}
.my-card{
  height: 100%;
  padding: 0 10px 5px 0;
}
.my-card:hover{
  background-color: #f6f6f6;
}
@media (max-width: 992px) {
  .Blog{
    padding: 10px 20px;
  }
}
</style>
