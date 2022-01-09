<template>
  <Head ref="head"></Head>
  <div class="container white">
    <div class="container-box">
      <div class="container-user">
        <div class="left-content">

          <div class="user-avatar">
              <el-avatar class="avatar-big" :size="168" v-if="UserInfo.username" :src="avatar">{{ icon }}</el-avatar>
              <el-avatar class="avatar-big" :size="168" v-else src="https://xxxxxxxxxx.top:9090/vueblog/user.png"></el-avatar>
          </div>
          <div style="text-align: center;box-sizing: inherit">
            <h2><span>{{ username }}</span></h2>
          </div>
          <div class="button-setting">
            <el-upload
                action="https://xxxxxxxxxx.top/api_mao/upload"
                name="file"
                :on-progress="uploadProcess"
                :with-credentials="true"
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
                :before-upload="beforeAvatarUpload">
              <el-button type="primary" class="len-button">更换头像</el-button>
            </el-upload>
          </div>
          <el-progress v-if="progressFlag" :stroke-width="8" :percentage="loadProgress"></el-progress>
        </div>
        <div class="right-content">
          <div style="border-bottom: 1px solid rgba(0,0,0,0.1);padding-bottom: 10px;margin-top: 0rem;">
            <span
                style="font-size: 1.125rem;opacity: 1;margin-bottom: -3rem;border-bottom: 2px solid #000000 !important;padding:0.6em 0.95em;">基本信息</span>
          </div>
          <el-card style="margin-top: 24px;padding: 10px 50px">
            <el-form class="userLabel" label-position="top" status-icon :rules="rules" label-width="100px"
                     :model="UserInfo" ref="UserInfo">
              <el-form-item label="用户名" prop="username">
                <el-input v-model="username" :model-value="username" disabled></el-input>
              </el-form-item>
              <el-form-item prop="email" label="邮箱">
                <el-input v-model="UserInfo.email" :model-value="UserInfo.email"></el-input>
              </el-form-item>
              <el-form-item label="性别">
                <el-radio-group v-model="UserInfo.sex">
                  <el-radio :label='0' v-model="UserInfo.sex">男</el-radio>
                  <el-radio :label='1' v-model="UserInfo.sex">女</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="公司" prop="company">
                <el-input v-model="UserInfo.company" :model-value="UserInfo.company"></el-input>
              </el-form-item>
              <el-form-item label="地址" prop="address">
                <el-input v-model="UserInfo.address" :model-value="UserInfo.address"></el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="submitForm('UserInfo')">更新</el-button>
              </el-form-item>
            </el-form>
          </el-card>
        </div>
      </div>


    </div>
  </div>
</template>

<script>
import Head from "@/components/Head";
import {ElMessage} from 'element-plus'
import {Plus} from '@element-plus/icons'

export default {
  name: "UserInfo",
  components: {
    Head,
    Plus,
  },
  data() {
    return {
      icon: "user", avatar: "",//icon和avatar只能有一个生效
      username: '',
      loadProgress: 0, // 动态显示进度条
      progressFlag: false, // 关闭进度条
      UserInfo: {
        id: -1,
        username: 'sdf',
        email: 'sdf',
        sex: 0,
        address: 'sdf',
        company: 'sdf'
      },
      rules: {
        email: [{required: true, message: 'Please input email', trigger: 'blur',},
          {type: 'email', message: 'Please input correct email address', trigger: ['blur', 'change'],},],
        address: [{min: 2, max: 50, message: 'Length should be 2 to 50', trigger: 'blur',},],
        company: [{min: 2, max: 50, message: 'Length should be 2 to 50', trigger: 'blur',},],
      },
    }
  },
  methods: {
    handleAvatarSuccess(res, file) {
      console.log(res)
      console.log(file)
      this.avatar =res.object.avatar;
      this.$refs.head.avatar = res.object.avatar;
    },
    beforeAvatarUpload(file) {
      const isJpgOrPng = file.type === 'image/jpeg' || 'image/png'
      const isLt2M = file.size / 1024 / 1024 < 2

      if (!isJpgOrPng) {
        this.$message.error('Avatar picture must be JPG/PNG format!')
      }
      if (!isLt2M) {
        this.$message.error('Avatar picture size can not exceed 2MB!')
      }
      return isJpgOrPng && isLt2M
    },
    uploadProcess(event, file, fileList) {
      console.log(event.percent)
      this.progressFlag = true; // 显示进度条
      this.loadProgress = parseInt(event.percent); // 动态获取文件上传进度
      if (this.loadProgress >= 100) {
        console.log(event.percent)
        this.loadProgress = 100
        setTimeout( () => {this.progressFlag = false}, 1000) // 一秒后关闭进度条
      }
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          console.log('success submit!!')
          const _this = this;
          this.$axios.post('/user/updateUser', _this.UserInfo).then(res => {
            console.log(res.data)
            ElMessage.success("信息更新成功")
          });
        } else {
          console.log('error submit!!')
          return false
        }
      });
    }
  },
  created() {
    const _this = this;
    this.$axios('/user/userInfo').then(res => {
      console.log(res)
      _this.UserInfo.id = res.data.object.id;
      _this.UserInfo.username = res.data.object.username;
      _this.UserInfo.email = res.data.object.email;
      _this.UserInfo.sex = res.data.object.sex;
      _this.UserInfo.company = res.data.object.company;
      _this.UserInfo.address = res.data.object.address;
      _this.username = res.data.object.username;
      _this.avatar = res.data.object.avatar;
    });
  }
}
</script>

<style>
.container-box {
  width: 90%;
  padding: 1rem 20px 0 20px;
  margin: 0 auto;
  box-sizing: border-box;
}

.container-user {
  position: relative;
  margin: -1rem -1rem -1rem -1rem;
  display: block;
  text-align: left;
  font-size: 0em;
  padding: 0em;
  box-sizing: inherit;

}

.left-content {
  width: 25% !important;
  position: relative;
  display: inline-block;
  text-align: left;
  font-size: 1rem;
  padding: 1rem 1rem 1rem 1rem;
  vertical-align: top;
  box-sizing: inherit;
}

.right-content {
  width: 75% !important;
  position: relative;
  display: inline-block;
  text-align: left;
  font-size: 1rem;
  padding: 1.5em 1rem 1rem 1rem;
  vertical-align: top;
  box-sizing: inherit;
}

.right-content div {
  box-sizing: inherit;
}

.user-avatar {
  position: relative;
  margin: 0 auto 10px;
  width: 160px;
  height: 160px;
  box-sizing: inherit;
}

.button-setting {
  align-items: center;
  justify-content: space-around;
  -webkit-box-align: center;
  margin: 15px auto;
  display: flex;
  box-sizing: inherit;
}

.avatar-big {
  width: 100%;
  height: 100%;
  margin: 0;
  border: 4px solid #EEE;
  font-size: 60px !important;
}

.len-button {
  min-height: 20px !important;
  padding: 8px 32px !important;
}

.userLabel .el-form-item label {
  font-weight: bolder;
  padding: 0px !important;
}


.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>