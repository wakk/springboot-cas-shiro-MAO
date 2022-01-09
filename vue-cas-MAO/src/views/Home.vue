<template>
  <div id="home">
    <Head></Head>
    <div class="container" >
      <sidebar :sides="sides"
      ></sidebar>
      <ItemsCard :items="items" :cards="cards"
      ></ItemsCard>
    </div>
  </div>
</template>

<script>
import Head from "@/components/Head";
import SideBar from "@/components/SideBar";
import ItemsCard from "@/components/ItemsCard";

export default {
  name: "home",
  components: {
    sidebar: SideBar,
    Head,
    ItemsCard
  },

  data(){
    return{
      sides: [{href:"#history",title:"最近访问"},
              {href:"#group-0",title:"博客"},
              {href:"#group-1",title:"其他"}],
      cards: [{id: "history", title: "最近访问"},
              {id: "group-0", title: "博客"},
              {id: "group-1", title: "其他"}],
      historyItem:[],
      items: [[],
              [{url: 'https://xxxxxxxxxx.top/myblog/', icon: "博", title: "博客",tag:"1-0"},
              ],
              [{url: '暂无', icon: "其", title: "其他",tag:"2-0"}],
            ],
    }
  },
  beforeCreate() {
    const _this=this;
    this.$axios('/user/history').then(res =>{
      if(res.data.object.username){
        console.log(res.data.object)
        const index1=res.data.object.index1;
        const index2=res.data.object.index2;
        for(let j = 0,len=index1.length; j < len; j++) {
          _this.historyItem.push(_this.items[index1[j]][index2[j]])
        }
        _this.items[0]=_this.historyItem
      }
    });

  }
}
</script>

<style>
.container{
  position: relative;
  margin-top: 60px;
  width: 100%;
  height: 100%;
}



p {
  display: block;
  margin-block-start: 1em;
  margin-block-end: 1em;
  margin-inline-start: 0px;
  margin-inline-end: 0px;
  margin: 0;
}
body{
  margin:0 auto;
  height: 100%;
  width: 100%;
  display: block;
  background-color: #f1f5f8;
}
html{
  display: block;
  height: 100%;
}

</style>

