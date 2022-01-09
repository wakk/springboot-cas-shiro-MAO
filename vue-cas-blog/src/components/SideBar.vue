<template>
  <div class="left-side">
    <div class="img_box">
      <router-link v-if="username" class="user-avatar" :to="{name:'PrivateBlog', params: {userId: userId}}">
        <el-avatar class="circle-img" :size="60" style="font-size: larger;line-height: 55px" :src="avatar">{{
            icon
          }}
        </el-avatar>
      </router-link>
      <div v-else class="user-avatar">
        <el-avatar class="circle-img" :size="60" src="https://xxxxxxxxx.top:9090/vueblog/user.png"></el-avatar>
      </div>
      <div class="user-info">{{ username }}<br>{{ email }}</div>

      <div class="search">
        <i class="material-icons search-icon search-clear" @click="ClearText">clear</i>
        <div class="search-box">
          <input type="text" class="search-input" placeholder="Searching..." v-model="filterText"/>
          <i class="material-icons search-icon search-start">search</i>
        </div>
      </div>
    </div>
    <el-scrollbar class="node-content">
      <div class="center-div">
        <span>专栏</span>
        <el-tooltip v-if="ownUserId==userId" content="删除空专栏" placement="top" effect="light">
        <el-icon style="text-align: right;float: right;margin-right: 10px;cursor: pointer" size="18"
        @click="dialogVisible=true"><delete /></el-icon>
        </el-tooltip>
      </div>
      <el-tree style="padding-left: 20px;color: black"
          ref="tree"
          class="filter-tree"
          :data="data"
          :props="defaultProps"
          default-expand-all
          :filter-node-method="filterNode"
          @node-click="handleNodeClick"
      />
    </el-scrollbar>
  </div>
  <el-dialog v-model="dialogVisible" title="删除所有空专栏" width="400px" :center="true">
    <div style="text-align: center;margin: 0 auto">
      <span class="dialog-footer">
              <el-button type="primary" @click="deleteFolder">确定</el-button>
              <el-button @click="dialogVisible = false">取消</el-button>
      </span>
    </div>
  </el-dialog>
</template>

<script>
import axios from "axios";
import {Delete} from '@element-plus/icons'

export default {
  props:{
    _user: {},
  },
  name: "SideBar",
  components:{
    Delete
  },
  data() {
    return {
      ownUserId:1,
      userId: "1",
      username: "username",
      icon: "user", avatar: "",//icon和avatar只能有一个生效
      email: "email",
      filterText: '',
      dialogVisible:false,
      data: [{
        id: 1, label: ' ',
        children: [{
          id:1, label: ' ',leaf: true,},
          {id: 1, label: ' ', leaf: true,},
            {id: 1, label: ' ', leaf: true,},
          ],
      },
      ],
      defaultProps: {
        children: 'children',
        label: 'label',
      },
    }
  },
  methods: {
    async deleteFolder(){
      const _this=this
      const r1=await this.$axios('/folder/clearNullFolder/'+this.ownUserId).then(res => {
        _this.dialogVisible=false;
      });
      console.log(r1)
      this.$axios('/folder/allInfo/'+this.userId).then(res => {
        _this.data=res.data.object.folderList;
      });
    },
    ClearText() {
      this.filterText = "";
    },
    filterNode(value, data) {
      if (!value) return true
      return data.label.indexOf(value) !== -1
    },
    handleNodeClick(object) {
      if (object.leaf) {
        console.log(object.id)
        this.$router.push("/blog/detail/" + object.id)
      }
    },
  }
  ,
  watch: {
    filterText(val) {
      var searchClear = document.querySelector('.search-clear');
      if (val == '') {
        searchClear.style.display = 'none';
        this.$refs.tree.filter(val)
      } else if (val.search(/^\s+$/) >= 0) {
        // 检测输入值全是空白的情况
        searchClear.style.display = 'block';
      } else {
        // 合法输入值的情况
        searchClear.style.display = 'block';
        this.$refs.tree.filter(val)
      }
    },
  },
  mounted() {
    const _this = this;
    this.userId=this._user.userId;
    this.email=this._user.email;
    this.username=this._user.username;
    this.avatar=this._user.avatar;
    this.$axios('/folder/allInfo/'+this.userId).then(res => {
      _this.data=res.data.object.folderList;
    });
    this.$axios('https://xxxxxxxxx.top/api_mao/user/headInfo').then(res => {
      _this.ownUserId=res.data.object.id;
      console.log("test1:"+_this.ownUserId+"  "+_this.userId);
      console.log("test2:"+_this.ownUserId==_this.userId);
    });
  },

}
</script>
<style scoped src="../assets/css/icons.css"/>
<style scoped>
.left-side {
  width: 300px;
  height: 100%;
  position: fixed;
  left: 0px;
  top: 60px;
  background-color: #fff;
  box-shadow: 0 0 20px 0 #d4dee6;
  float: left;
  z-index: 10;
}

@media (max-width: 992px) {
  .left-side {
    display: none;
  }
}

.img_box {
  position: relative;
  width: 100%;
  height: 200px;
  overflow: hidden;
  background-image: url(../assets/avatar-back.jpg);
}

.user-avatar {
  position: absolute;
  top: 32px;
  left: 32px;
  width: 60px;
  height: 60px;
}

.circle-img {
  border-radius: 50%;
  border-style: solid;
  border-color: #009688;
  border-width: 3px;
  width: 60px;
  height: 60px;
}

.user-info {
  position: absolute;
  top: 110px;
  left: 32px;
  line-height: 20px;
  color: #fff;
  font-size: large;
}

.search {
  position: absolute;
  bottom: 10px;
  height: 30px;
  line-height: 30px;
  width: 100%;
}

.search-box {
  padding-left: 32px;
  padding-right: 10px;
}

.search .search-icon {
  float: left;
  height: 100%;
  margin: 0 10px;
  line-height: 30px;
  cursor: pointer;
  user-select: none;
  color: white;
}

.search .search-input {
  float: left;
  width: 80%;
  height: 30px;
  line-height: 30px;
  margin: 0;
  border: 2px solid #ddd;
  border-radius: 10px;
  box-sizing: border-box;
  background: no-repeat;
  color: white;
}

.search .search-clear {
  display: none;
  margin: 0 4px;
}

.node-content {
  /*padding-top: 10px;*/
}
.center-div{
  padding: 10px 0;
  text-align: center;
  margin: 0 auto
}
.node-content div {
  background-color: #fff;
}
</style>