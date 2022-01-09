<template>
    <div class="content">
      <div class="content-block" :id="card.id" v-for="(card,index1) in cards">
        <div class="title_item">{{card.title}}</div>
        <div class="block_item" v-for="item in items[index1]">
          <div class="item" @click="a(item.url,item.tag)">
            <div class="panel_image">
              <div><span>{{item.icon}}</span></div>
            </div>
            <div class="panel_content">
              <p :title="item.title">{{item.title}}</p>
              <p class="panel_url" :title="item.url">{{item.url}}</p>
            </div>
          </div>
        </div>

      </div>
      <span  style="display: block;height: 700px;"></span>
    </div>
</template>

<script>
export default {
  name: "ItemsCard",
  props:{
    cards: {},
    items: {}
  },
  methods:{
    a(url,tag){
      this.$axios.get('/user/saveHistory/'+tag);
      window.open(url);
    }
  }
}
</script>

<style scoped>
.content{
  position: relative;
  margin-left: 16.66666667%;
  min-width: 200px;
  width: 83.33333333%;
  height: 100%;
  float: left;
}
@media (max-width: 992px) {
  .content{
    margin-left: 0;
    width: 100%;
  }
}
.content-block{
  padding: 60px 40px 0px 40px;
  clear: both;
  margin: 0;
  content: '';
}
.title_item{
  font-size: 20px;
  font-weight: normal;
  line-height: 40px;
  color: #000000;
  width: 100%;
  float: left;
  position: relative;
  box-sizing: border-box;
}
.block_item{
  padding: 10px 20px 10px 0px;
  width: 25%;
  min-width: 100px;
  float: left;
  position: relative;
  box-sizing: border-box;
  min-width: 300px;
}
.item{
  display: flex;
  width: 100%;
  height: 60px;
  background-color: #ffffff;
  cursor: pointer;
  margin: 0;
}
.item:hover{
  box-shadow: 0 6px 16px 0 #d6e0e8;/*设置阴影,可以自定义参数*/
  color: #6e07e3;

}
.panel_image{
  width: 30px;
  padding: 15px;
  flex-shrink: 0;
}
.panel_image div{
  width: 30px;
  height: 30px;
  border-radius: 30px;
  background-color: #350170;
  color: #ffffff;
  font-size: 20px;
  line-height: 30px;
  text-align: center;
}
.panel_content{
  flex: 1;
  overflow: hidden;
  font-size: 14px;
  line-height: 20px;
  padding: 10px 10px 10px 10px;
  margin: 0;
}
.panel_content p{
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.panel_url{
  font-size: 12px;
  color: #909090;
}
</style>