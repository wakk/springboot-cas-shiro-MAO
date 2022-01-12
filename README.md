# springboot-cas-shiro-MAO

## 简介

这是一个简单的CAS单点登录的认证系统。用户可以登录该系统对多个项目进行访问，目前该系统里面只有一个博客项目。主系统负责账号信息的管理，子系统则通过该账号进行统一登录，并提供对应项目的功能。

博客的功能主要是搜索和浏览他人文章以及撰写文章发布，并支持收藏文章、收藏夹的管理等。

项目网站地址：https://xiumaozhi.top/

## 技术栈

* 后端：springboot，redis，mybatis-plus，cas，shiro，tomcat，xxl-job，ftp
* 前端：vue，element-plus，axio，v-md-editor
* 部署：nginx、docker、ssl

## 前言

刚开始是想做个自己的博客，但考虑到以后可能会做各种有趣的小项目，所以干脆先做个SSO系统，然后自己可以在里面登录各种子项目，一劳永逸！

>  思考:CAS和jwt都可以做单点登录，选择哪个？
>
>  jwt相当于客户端session，避免了cookie的跨域，无状态，但服务后台无法拒绝携带JWT的请求，可能被伪造，jwt过长影响性能。
>
>  CAS跨平台，在认证中心保证客户端资源安全，需要依赖SSL，浏览器或CAS服务器重启后单点登录失效。最后考虑集成难度，选择了更易于实现的CAS。

**系统结构图**

![img](https://xiumaozhi.top:9090/image-20220111003256167.png)

## 系统开发过程

### CAS
cas的服务端使用的是tomcat版本的5.3.14，下载地址：https://repo1.maven.org/maven2/org/apereo/cas/cas-server-webapp-tomcat/ 。主要是简单对登录界面做了一些修改，并配置数据库的连接，然后部署在tomcat上，由于cas运行需要ssl支持，所以需要在tomcat开启https服务，才能正常运行，购买域名、安装证书等教程基本上按照阿里云的说明书。

> 参考：https://www.bilibili.com/video/BV1xy4y1r7BU?p=4

## cas-MAO

系统主页的项目以及账号信息的管理，springboot项目集成了cas以及shiro对登录和权限进行认证。shiro主要是过滤未登录认证的请求，重定向到cas登录，最后再由cas进行权限认证。主页的历史记录使用redis进行缓存，并提供图片和头像上传、删除的api，主要是通过ftp发送图片到服务器，再由nginx进行访问图片，用户数据则是使用mybatis-plus存储到mysql数据库。

前端页面基本是自己按照其他网站以及element-ui拼凑出来，看着有点蹩脚，仅限于参考。

>  参考：https://github.com/willwu1984/springboot-cas-shiro

## cas-blog

博客项目主页展示所有的博客，由于从mysql读取较慢，使用了redis缓存优化数据的读取，并使用xxl-job调用定时任务来更新redis缓存中的数据。在个人博客中，侧边栏中的专栏可以快速定位到博客，新建的博客都会放在一个专栏文件夹中，而博客的新增和编辑都会影响redis的博客缓存，所以在定时任务中对新增的博客比编辑的时效性要更高。收藏页面的管理有收藏夹的新建、改名、移动、删除等。

由于博客编写过程中可能会上传图片，所以需要对多余的图片进行清理，主要对每一篇博客内容进行匹配搜索，保存图片地址到redis，xxl-job定时在空闲时间检查所有博客进行图片清理。
前端项目的博客编辑器使用v-md-editor，并且能够上传本地图片到服务器中，博客还可以根据其目录进行文章的定位。

>  参考：https://github.com/MarkerHub/vueblog

## 部署

springboot项目都是在部署docker容器，vue项目则打包上传到nginx作为静态资源浏览，cas服务器则部署在tomcat中。

由于cas服务器需要https，浏览器会自动将http跳转到https，所以浏览器的请求都要支持https。因此nginx也安装了SSL证书，并通过443端口对浏览器请求进行分配，静态资源定位到对应的vue项目，后端请求则配置反向代理转发请求。nginx还需要有配置图片资源的访问，满足博客内容中对图片资源的请求。

xxl-job使用docker部署，在容器运行时配置mysql数据库连接与密码即可。
