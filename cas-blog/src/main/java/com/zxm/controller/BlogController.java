package com.zxm.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zxm.common.dto.BlogDto;
import com.zxm.common.lang.Result;
import com.zxm.entity.Blog;
import com.zxm.entity.Folderinfo;
import com.zxm.entity.User;
import com.zxm.job.executor.jobhandler.DemoJobHandler;
import com.zxm.service.BlogService;
import com.zxm.service.FolderinfoService;
import com.zxm.service.UserService;
import com.zxm.utils.RedisUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zxm
 * @since 2021-12-10
 */
@RestController
@RequestMapping("/blog")
public class BlogController {
    @Autowired
    BlogService blogService;

    @Autowired
    UserService userService;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    FolderinfoService folderinfoService;

    @RequestMapping(value = "/blogDto",method = RequestMethod.GET)
    public Result getBlogs(){
        //先尝试从redis缓存中获取
        if(redisUtil.hasKey("blogDto")){
            return Result.succ(MapUtil.builder().put("blogs",redisUtil.get("blogDto")).map());
        }
        List<BlogDto> list = blogService.getBlogs();
        redisUtil.set("blogDto",list);
        return Result.succ(MapUtil.builder().put("blogs",list).map());
    }
    @RequestMapping(value = "/page",method = RequestMethod.GET)
    public Result getOneBlogs(@RequestParam("id") Integer id ,@RequestParam("currentPage")Integer currentPage) {
        Page page = new Page(currentPage, 5);

        IPage pageData = blogService.page(page, new QueryWrapper<Blog>()
                .eq("user_id",id).orderByDesc("created"));
        System.out.println("list"+pageData.getTotal());
        return Result.succ(pageData);
    }

    @RequestMapping(value = "/content/{id}",method = RequestMethod.GET)
    public Result detail(@PathVariable(name = "id") Long id) {
        Blog blog = blogService.getById(id);
        if(blog==null)Result.fail("该博客已删除");
        return Result.succ(blog);
    }

    @RequiresAuthentication
    @RequestMapping(value = "/edit/{folderId}",method = RequestMethod.POST)
    public Result edit(@Validated @RequestBody Blog blog,@PathVariable("folderId")Long folderId) {
        System.out.println(blog.toString());
        Blog temp = null;
        User user=userService.getOne(new QueryWrapper<User>().eq("username",userService.getProfileId()));
        if (blog.getId() != null) {//编辑文章
            temp = blogService.getById(blog.getId());
            Assert.isTrue(temp.getUserId().longValue() == user.getId().longValue(), "没有权限编辑或创建");
        } else {//新建文章
            temp = new Blog();
            temp.setUserId(user.getId());
            temp.setCreated(LocalDateTime.now());
            temp.setStatus(0);
        }
        BeanUtil.copyProperties(blog, temp, "id", "userId", "created", "status");
        blogService.saveOrUpdate(temp);
        if(blog.getId()==null){
            //创建专栏并提交定时任务
            Folderinfo folderinfo=new Folderinfo();
            folderinfo.setBlogId(temp.getId());folderinfo.setBlogName(temp.getTitle());
            folderinfo.setFolderId(folderId);folderinfo.setUserId(user.getId());
            folderinfoService.save(folderinfo);
            if(DemoJobHandler.running){
                DemoJobHandler.needUpdate=true;
            }else{
                DemoJobHandler.isSubmitTask=true;
            }
        }
        //缓存图片url到redis
        blogService.saveUrlCache(temp);
        return Result.succ(null);
    }

}
