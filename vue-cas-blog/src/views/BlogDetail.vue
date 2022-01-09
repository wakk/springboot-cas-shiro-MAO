<template>
  <div>
    <SideBar v-if="flag" :_user="user"/>
    <div class="container">
      <div class="main">
        <h1 style="text-align: center;padding-top: 2rem;margin: 0 auto;font-size: 2rem;">{{ blog.title }}</h1>
        <div v-if="isMyBlog" style="text-align: right;display: block;padding-right: 2.5rem;">
          <router-link :to="{name: 'BlogEdit', params: {blogId: blog.id}}">
            <el-icon style="margin-right: 5px">
              <edit/>
            </el-icon>
            重新编辑
          </router-link>
        </div>
        <div class="blog-content">
          <v-md-preview :text="blog.content" id="preview"></v-md-preview>
        </div>
        <el-divider style="width: 90%;margin: 0 auto;"></el-divider>
        <div style="text-align: center;display: block;padding: 1rem  0 2rem 0 ;">
          <el-icon :size="24" style="cursor: pointer" @click="DoCollection">
            <star-filled v-if="isCollect" style="color: #fc5531"/>
            <star v-else style="color: #999aaa"/>
          </el-icon>
          <div style="color:#fc5531" v-if="isCollect">{{ blog.collectNum }}</div>
          <div style="color:#999aaa" v-else>{{ blog.collectNum }}</div>
        </div>

        <!--        收藏的对话框-->
        <el-dialog v-model="dialogTableVisible" title="添加收藏夹" width="450px" :center="true">
          <div style="text-align: center;margin: 0 auto">
            <el-table
                ref="singleTable"
                :key="timeStamp"
                :data="tableData"
                :show-header="false"
                max-height="330"
                highlight-current-row
                style="width: 100%;margin-bottom: 10px"
                @current-change="handleCurrentChange">
              <!--          <el-table-column type="index" width="50" />-->
              <el-table-column property="name" label=""/>
            </el-table>
            <div style="margin-bottom: 10px">
              <el-input v-model="inputNewCollection" placeholder="新建文件夹">
                <template #append>
                  <el-button style="color: black" @click="createFavorite">新建</el-button>
                </template>
              </el-input>
            </div>
            <span class="dialog-footer">
              <el-button type="primary" @click="submitCollection">确定</el-button>
              <el-button @click="dialogTableVisible = false">取消</el-button>
            </span>
          </div>
        </el-dialog>
      </div>
    </div>
  </div>
  <FloatButton :key="timer"/>

</template>

<script scoped>
import SideBar from "@/components/SideBar";
import {Edit, StarFilled, Star} from '@element-plus/icons'
import 'materialize-css/dist/js/materialize.min.js'
import FloatButton from "@/components/FloatButton";
import hljs from "highlight.js";
import axios from "axios";

export default {
  name: "BlogDetail",
  components: {
    SideBar,
    Edit,
    FloatButton,
    Star,
    StarFilled,
  },
  data() {
    return {
      timer: '',
      newTime:'',
      myUserId:"",
      user:{userId:'',username:"",email:"",avatar:""},//当前博客的用户
      flag:false,
      blog: {
        id:"",
        userId: '1',
        title: "",
        content: "",
        collectNum: "10",
      },
      timeStamp: 1,
      tableData: [
        {id:1,name: ' ',},
         ],
      currentRow: null,
      isMyBlog: false,
      dialogTableVisible: false,
      inputNewCollection: "",
      isCollect:false,
      showFloatButton:false,
    }
  },
  watch:{
    tableData:{
      handler(val) {
        this.timeStamp = new Date()
      },
    },
    "$route": function(newVal, oldVal){
      if(newVal.path.search("/blog/detail/") !== -1 && !newVal.hash){
        this.getData();
      }
    }
  },
  methods: {
    getData: function(){
      this.blog.id = this.$route.params.blogId
      this.mainFuction()
    },
    async DoCollection() {
      let isAuthenticate=false;
      const r1 = await this.$axios.get('https://xxxxxxxxx.top/api_mao/user/headInfo').then(res => {
        if (res.data.object.username) { // vuex.state判断token是否存在
          isAuthenticate=true;
        }else{
          window.open("https://xxxxxxxxx.top/api_mao/login","_self");
        }
      });
      console.log(r1);
      if(!isAuthenticate)return;
      if(this.isCollect){
        const _this=this;
        this.$axios('/favoriteinfo/delete/?blogId=' + this.blog.id).then(res=>{
          _this.blog.collectNum--;
          this.isCollect=false
        })
      }else{
        this.dialogTableVisible = true;
      }
    },
    handleCurrentChange(val) {
      this.currentRow = val
    },
    submitCollection() {
      if(this.currentRow) {
        const _this=this;
        this.$axios('/favoriteinfo/save/' + this.currentRow.id + '?blogId=' + this.blog.id).then(res=>{
          _this.blog.collectNum++;
          _this.isCollect=true;
          this.dialogTableVisible = false
        })
      }else{
        this.$message.error('请先选择收藏夹')
      }
    },
    async mainFuction(){
      const _this=this;
      console.log("mainFuction")
      const r1 = await this.$axios.get('/blog/content/' + this.blog.id).then((res) => {
        _this.blog = res.data.object
      });
      console.log(r1)
      this.checkMyBlog().then(r => {})
      this.getUserInfo()
      //渲染
      this.$nextTick(() => {
        this.timer = new Date().getTime();
      }).then(r => {})
    },
    async checkMyBlog() {
      this.isMyBlog = false;
      const _this = this;
      const r1=await this.$axios('https://xxxxxxxxx.top/api_mao/user/headInfo').then(res => {
        _this.myUserId = res.data.object.id;
        _this.isMyBlog = (_this.myUserId == _this.blog.userId);
        console.log("headInfo")
      });
      console.log(r1)
      if(_this.myUserId){this.getFavorite()}
    },
    getFavorite(){
      const _this = this;
      this.$axios('/favorite/partInfo/'+this.myUserId+'?blogId='+this.blog.id).then(res=>{
        _this.tableData=res.data.object.favoriteList;
        _this.isCollect=res.data.object.isCollect;
        console.log("collect:"+_this.isCollect)
      });
    },
    createFavorite() {
      //名称不能为空，并且name不能重复
      if (this.inputNewCollection && this.isNotExist()) {
        const _this = this;
        let formData = new FormData()
        formData.append("favoriteName",this.inputNewCollection)
        this.$axios.post('/favorite/create/', formData).then(res => {
          _this.inputNewCollection = '';
          _this.getFavorite()
        })
      };
    },
    isNotExist(){
      for(let i=0;i<this.tableData.length;i++){
        if(this.tableData[i].name===this.inputNewCollection){
          this.$message.error('名称已存在');
          return false;
        }
      }
      return true;
    }
    ,
    getUserInfo(){
      const _this = this;
      this.user.userId=this.blog.userId
      this.$axios('https://xxxxxxxxx.top/api_mao/user/partInfo/'+this.user.userId).then(res => {
        _this.user.email=res.data.object.email;
        _this.user.username = res.data.object.username;
        _this.user.avatar = res.data.object.avatar;
        _this.flag=true
        console.log("partInfo")
      });
    }
  },
  created() {
    this.blog.id = this.$route.params.blogId
    this.mainFuction()
  },
  mounted() {
  },
  // beforeUpdate() {
  //   this.showFloatButton=true;
  //   this.timer= new Date().getTime();
  // }
}
</script>

<style scoped>
.container {
  display: flex;
  padding: 0;
  width: 100%;
  font-family: auto;
  flex-direction: column;
  margin: 0;
}

.blog-content {
  margin: 0 auto;
  width: 95%
}

.main {
  flex: 1 0 auto;
  padding-left: 300px
}

@media (max-width: 992px) {
  .main {
    padding: 0;
  }

}

</style>