<template>
  <div class="m-content">
    <h3 class="m-title">编辑博客</h3>
    <el-form ref="editForm" status-icon :model="editForm" :rules="rules" label-width="80px">
      <el-form-item class="mobile" label="标题" prop="title">
        <el-input v-model="editForm.title" placeholder="标题"></el-input>
      </el-form-item>
      <el-form-item class="mobile" label="摘要" prop="description">
        <el-input type="textarea" v-model="editForm.description" placeholder="摘要"></el-input>
      </el-form-item>
      <el-form-item class="mobile" label="内容" prop="content">
        <div class="mavonEditor">
          <v-md-editor v-model="editForm.content" height="600px"
                       :disabled-menus="[]"
                       @upload-image="handleUploadImage"></v-md-editor>
        </div>
      </el-form-item>
      <el-form-item class="center" style="text-align: center">
        <el-button type="primary" @click="clickSubmit">{{ buttonText }}</el-button>
        <el-button @click="this.$router.go(-1);">取消</el-button>
      </el-form-item>
    </el-form>
    <el-dialog v-model="dialogTableVisible" title="创建至一个专栏" width="400px" :center="true">
      <div style="text-align: center;margin: 0 auto">
        <el-table
            ref="singleTable"
            :key="timeStamp"
            :data="tableData"
            height="330"
            :show-header="false"
            highlight-current-row
            style="width: 100%;margin-bottom: 10px"
            @current-change="handleCurrentChange">
          <!--          <el-table-column type="index" width="50" />-->
          <el-table-column property="name" label=""/>
        </el-table>
        <div style="margin-bottom: 10px">
          <el-input v-model="inputNewFolder" placeholder="新建专栏">
            <template #append>
              <el-button style="color: black" @click="createFolder">新建</el-button>
            </template>
          </el-input>
        </div>
        <span class="dialog-footer">
              <el-button type="primary" @click="submitForm">确定</el-button>
              <el-button @click="dialogTableVisible = false">取消</el-button>
            </span>
      </div>
    </el-dialog>
    <FloatButton/>
  </div>
</template>

<script>
import 'mavon-editor/dist/css/index.css'
import FloatButton from "@/components/FloatButton";

export default {
  name: "BlogEdit",
  components: {
    FloatButton,
  },
  watch: {
    tableData: {
      handler(val) {
        this.timeStamp = new Date()
      },
    }
  },
  data() {
    return {
      userId: '',
      editForm: {
        id: null,
        title: '',
        description: '',
        content: ''
      },
      rules: {
        title: [
          {required: true, message: '请输入标题', trigger: 'blur'},
          {min: 3, max: 50, message: '长度在 3 到 50 个字符', trigger: 'blur'}
        ],
        description: [
          {required: true, message: '请输入摘要', trigger: 'blur'}
        ]
      },
      buttonText: false,
      dialogTableVisible: false,
      inputNewFolder: "",
      tableData: [{name: 'Tom1',},
        {name: 'Tom2',}, {name: 'Tom2',}, {name: 'Tom2',}, {name: 'Tom2',},
        {name: 'Tom2',}, {name: 'Tom2',},],
      currentRow: "",
      timeStamp: "",
    }
  },
  created() {
    const blogId = this.$route.params.blogId
    const _this = this
    this.buttonText = "立即创建";
    //当blogId存在是，当前页面为编辑文章
    if (blogId) {
      this.buttonText = "立即更新";
      this.$axios.get('/blog/content/' + blogId).then((res) => {
        console.log(res.data.object)
        const blog = res.data.object
        _this.editForm.id = blog.id
        _this.editForm.title = blog.title
        _this.editForm.description = blog.description
        _this.editForm.content = blog.content
      });
    }
    this.getUserId()


  },
  methods: {
    createFolder() {
      //名称不能为空，并且name不能重复
      if (this.inputNewFolder && this.isNotExist()) {
        const _this = this;
        let formData = new FormData()
        formData.append("folderName", this.inputNewFolder)
        this.$axios.post('/folder/create/', formData).then(res => {
          _this.inputNewFolder = '';
          _this.getFavorite()
        })
      }
      ;
    },
    isNotExist() {
      for (let i = 0; i < this.tableData.length; i++) {
        if (this.tableData[i].name === this.inputNewFolder) {
          this.$message.error('名称已存在');
          return false;
        }
      }
      return true;
    },
    async getUserId() {
      const _this = this;
      const r1 = await this.$axios('https://xxxxxxxxx.top/api_mao/user/headInfo').then(res => {
        _this.userId = res.data.object.id;
      });
      console.log(r1)
      this.getFavorite()
    },
    getFavorite() {
      const _this = this;
      if (!this.$route.params.blogId) {
        this.$axios('/folder/partInfo/' + this.userId).then(res => {
          _this.tableData = res.data.object.folderList;
        });
      }
    },
    clickSubmit() {
      this.$refs.editForm.validate((valid) => {
        if (valid) {
          if (this.buttonText === "立即创建") {
            this.dialogTableVisible = true
          } else {
            this.submitForm()
          }
        } else {
          console.log('error submit!!');
          return false;
        }
      })
    },
    handleCurrentChange(val) {
      this.currentRow = val
    },
    handleUploadImage(event, insertImage, files) {
      const file = files[0]
      let formData = new FormData()
      // 拿到 files 之后上传到文件服务器，然后向编辑框中插入对应的内容
      formData.append('file', file)
      let name = files[0].name.substring(0, file.name.lastIndexOf('.'));
      console.log(files);
      console.log(name)
      if (this.editForm.content.includes("![" + name + "]")) {
        this.$message.error('重复上传同名图片')
      } else if (file.size / 1024 / 1024 > 2) {
        this.$message.error('Avatar picture size can not exceed 2MB!')
      } else {
        let instance = this.$axios.create({
          withCredentials: true
        })
        instance.post('https://xxxxxxxxx.top/api_mao/uploadBlogImg', formData).then(res => {
          if (res.data.code === 200) {
            let url = res.data.object.imgUrl
            let content = this.editForm.content
            // 第二步.将返回的url替换到文本原位置![...](0) -> ![...](url)  这里是必须要有的
            insertImage({
              url: url,
              desc: name,
              // width: 'auto',
              // height: 'auto',
            });
            this.$message.success('上传成功')
          } else {
            this.$message.error(res.data.msg)
          }
        })
      }
    },
    submitForm() {
      let folderId = 1;
      if (this.currentRow) {
        folderId = this.currentRow.id
      }else{
        if (!this.$route.params.blogId) {
          this.$message.error('请先选择专栏')
          return false
        }
      }
      const _this = this
      this.dialogFormVisible = false
      this.$axios.post('/blog/edit/' + folderId, this.editForm, {
        headers: {
          "Authorization": localStorage.getItem("token")
        }
      }).then((res) => {
        _this.$alert('操作成功', '提示', {
          confirmButtonText: '确定',
          callback: action => {
            _this.$router.push("/blog/home/" + this.userId)
          }
        });
      });
    },
  },
}
</script>

<style scoped>
.m-content {
  margin: 0 auto;
  width: 80%;
}

.CodeMirror {
  text-align: left !important;
}

.m-title {
  text-align: center;
  padding: 20px;
}

@media (max-width: 992px) {
  .m-content {
    margin: 5px;
    padding: 5px;
    width: 95%;
  }

  .m-title {
    padding: 0px;
  }

  .mobile >>> label {
    display: none;
  }
}
</style>