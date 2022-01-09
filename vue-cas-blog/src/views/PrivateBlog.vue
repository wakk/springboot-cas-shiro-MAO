<template>
  <div>
    <SideBar v-if="flag" :_user="user"/>
    <div class="content">
      <div class="pBlog">
        <h3 v-if="isMyBlog" style="text-align: center;margin-top: 0px">个人博客</h3>
        <h3 v-else style="text-align: center;margin-top: 0px">{{user.username}}的博客</h3>

        <div class="block">
          <el-timeline class="timeline">
            <el-timeline-item :timestamp="filterBlogs[i].created" placement="top" v-for="i in filterIndex">
              <el-card class="my-card" slot="reference">
                <div style="padding-top: 0px">

                  <div style="margin-left: 20px;padding-top: 10px">
                    <h4  class="ellipsis-1"  style="margin: 0;">
                      <router-link :to="{name: 'BlogDetail', params: {blogId: filterBlogs[i].id}}">
                        {{ filterBlogs[i].title }}
                      </router-link>
                    </h4>
                    <p class="card-content ellipsis-2">{{ filterBlogs[i].description }}</p>
                  </div>
                </div>
                <div class="start">
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

        <el-pagination v-if="totalSize>pageSize" class="mpage"
                       background
                       layout="prev, pager, next"
                       :current-page=currentPage
                       :page-size=pageSize
                       @current-change=page
                       :total="totalSize">
        </el-pagination>
      </div>
      <FloatButton ref="flButton"/>
    </div>
  </div>
</template>
<script>
import FloatButton from "@/components/FloatButton";
import SideBar from "@/components/SideBar";
import {StarFilled} from '@element-plus/icons'
export default {
  name: "PrivateBlog",
  components: {
    FloatButton,
    SideBar,
    StarFilled,
  },
  data() {
    return {
      user:{userId:'',username:"",email:"",avatar:""},
      isMyBlog: false,
      blogs: [
        {
          id: 4,
          created: "  ",
          title: "  ",
          description: "    ",
          collectNum: 0
        },
      ],
      filterBlogs: [],
      filterIndex: [],
      currentPage: 1,
      pageSize: 6,
      totalSize: 9,
      flag:false,
    }
  },
  methods: {
    page(currentPage) {
      const _this = this
      this.$axios.get('/blog/page?currentPage=' + currentPage+'&&id='+this.user.userId).then((res) => {
        _this.blogs = res.data.object.records
        _this.totalSize = res.data.object.total
        _this.currentPage = res.data.object.current
        _this.pageSize = res.data.object.size
        _this.filterBlogs = _this.blogs

        _this.filterIndex = [];
        const max = _this.pageSize
        for (let i = 0; i < _this.filterBlogs.length && i < max; i++) {
          _this.filterIndex.push(i)
        }
      });

    },
  },
  created() {
    this.user.userId = this.$route.params.userId
    const _this=this
    async function a(){
      const r1= await _this.$axios('https://xxxxxxxxx.top/api_mao/user/partInfo/'+_this.user.userId).then(res => {
        _this.user.email=res.data.object.email;
        _this.user.username = res.data.object.username;
        _this.user.avatar = res.data.object.avatar;
        _this.flag=true;
      });
    }
    a();
    //axios请求获取数据
    this.page(1);
    //isMyBlog
    this.isMyBlog=false;
    this.$axios('https://xxxxxxxxx.top/api_mao/user/headInfo').then(res => {
      _this.isMyBlog =(res.data.object.id ==_this.user.userId);
    });

  }

}
</script>

<style scoped>
.content{
  position: relative;
  margin-left: 300px;
  min-width: 200px;
  height: 100%;
  /*float: left;*/

}
.pBlog {
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px 60px;
}

@media (max-width: 992px) {
  .content {
    margin: 0;
    width: 100%;
  }
  .pBlog {
    padding: 10px 20px 10px 5px;
  }
  .timeline{
    padding: 0!important;
  }
}
</style>