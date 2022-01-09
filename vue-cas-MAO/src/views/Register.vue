<template>
  <el-header style="text-align: right;font-size: 15px;color: aliceblue">
    <router-link :to="{name:'Home'}">
      <img style="width: 262px; height: 56px;float: left;margin-top: 3px;" src="../assets/MAO.png" fit="cover">
    </router-link>
  </el-header>

  <div class="container white">
    <div class="register-card">
      <div class="el-card register-card-in">
          <el-form ref="ruleForm" :model="ruleForm" status-icon :rules="rules" label-width="120px" class="demo-ruleForm">
            <div class="title_item el-form-item" style="text-align: center;margin-left: 120px">
              <span style="margin: 0 auto;font-size: 20px;">注册</span>
            </div>
            <el-form-item label="用户名" prop="username">
              <el-input v-model="ruleForm.username"></el-input>
            </el-form-item>
            <el-form-item label="密码" prop="password">
              <el-input v-model="ruleForm.password" type="password" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="验证密码" prop="checkPass">
              <el-input v-model="ruleForm.checkPass" type="password" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item prop="email" label="邮箱">
              <el-input v-model="ruleForm.email"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="submitForm('ruleForm')">提交</el-button>
              <el-button @click="resetForm('ruleForm')" style="float: right;">重置</el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
  </div>
</template>

<script>
export default {
  name: "Register",
  data() {
    const validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('Please input the password'))
      } else {
        if (this.ruleForm.checkPass !== '') {
          this.$refs.ruleForm.validateField('checkPass')
        }
        callback()
      }
    }
    const validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('Please input the password again'))
      } else if (value !== this.ruleForm.password) {
        callback(new Error("Two inputs don't match!"))
      } else {
        callback()
      }
    }
    return {
      ruleForm: {
        username: '',
        password: '',
        email:'',
        checkPass:'',
      },
      rules: {
        username: [{required: true, message: 'Please input username', trigger: 'blur',},
          {min: 3, max: 20, message: 'Length should be 3 to 20', trigger: 'blur',},],
        password: [{required: true, message: 'Please input password', trigger: 'blur',},
          {validator: validatePass, trigger: 'blur'}],
        checkPass: [{required: true, message: 'Please input password again', trigger: 'blur',},
          {validator: validatePass2, trigger: 'blur'}],
        email: [{required: true, message: 'Please input email address', trigger: 'blur',},
          {type: 'email', message: 'Please input correct email address', trigger: ['blur', 'change'],},]
      },
    }
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          const _this = this;
          this.$axios.post('/user/register', _this.ruleForm).then(res => {
            console.log(res.data)
            _this.$router.push("/login")
          });
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    resetForm(formName) {
      this.$refs[formName].resetFields()
    },
  },
}
</script>
<style>
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
.white {
  width: 100%;
  height: 100%;
  background-color: white;
}

.register-card {
  padding: 10% 30% 0% 30%;
}
.register-card-in{
  padding: 5% 15% 0 7%;
}


</style>