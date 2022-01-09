package com.zxm.controller;


import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zxm.common.dto.FavoriteinfoDto;
import com.zxm.common.lang.Result;
import com.zxm.entity.Blog;
import com.zxm.entity.Favorite;
import com.zxm.entity.Favoriteinfo;
import com.zxm.entity.User;
import com.zxm.service.BlogService;
import com.zxm.service.FavoriteService;
import com.zxm.service.FavoriteinfoService;
import com.zxm.service.UserService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.jws.Oneway;
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
@RequestMapping("/favoriteinfo")
public class FavoriteinfoController {
    @Autowired
    BlogService blogService;
    @Autowired
    FavoriteinfoService favoriteinfoService;
    @Autowired
    UserService userService;
    @Autowired
    FavoriteService favoriteService;

    @RequiresAuthentication()
    @RequestMapping(value = "/save/{favoriteId}",method = RequestMethod.GET)
    public Result saveFavorite(@PathVariable(name="favoriteId")Long favoriteId, @RequestParam("blogId")Long blogId){
        Blog blog = blogService.getById(blogId);
        if(blog==null)return Result.fail("该博客不存在");
        User user=userService.getOne(new QueryWrapper<User>().eq("username",userService.getProfileId()));
        if(user==null){
            Result.fail("请先登录用户");
        }
        if(favoriteinfoService.getOne(new QueryWrapper<Favoriteinfo>().eq("blog_id",blogId).eq("user_id",user.getId()))!=null){
            Result.fail("已经收藏过该博客");
        }
        Favoriteinfo favoriteinfo=new Favoriteinfo();
        favoriteinfo.setFavoriteId(favoriteId);
        favoriteinfo.setBlogId(blogId);
        favoriteinfo.setBlogName(blog.getTitle());
        favoriteinfo.setUserId(user.getId());
        System.out.println("新增收藏记录"+favoriteinfo.toString());
        favoriteinfoService.save(favoriteinfo);//没有声明id的表，使用构造器时mybatis-plus会报错
        blog.setCollectNum(blog.getCollectNum()+1);
        blogService.saveOrUpdate(blog);
        Favorite favorite = favoriteService.getById(favoriteId);
        favorite.setNum(favorite.getNum()+1);
        favoriteService.saveOrUpdate(favorite);
        return Result.succ("");
    }

    @RequiresAuthentication()
    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    public Result deleteFavorite(@RequestParam("blogId")Long blogId){
        Blog blog = blogService.getById(blogId);
        if(blog==null)return Result.fail("该博客不存在");
        User user=userService.getOne(new QueryWrapper<User>().eq("username",userService.getProfileId()));
        if(user==null){
            Result.fail("请先登录用户");
        }
        Favoriteinfo favoriteinfo = favoriteinfoService.getOne(new QueryWrapper<Favoriteinfo>().eq("blog_id", blogId).eq("user_id", user.getId()));
        Favorite favorite = favoriteService.getById(favoriteinfo.getFavoriteId());
        System.out.println("删除blog_id: "+blogId +"user_id "+user.getId());
        favoriteinfoService.remove(new QueryWrapper<Favoriteinfo>().eq("blog_id",blogId).eq("user_id",user.getId()));
        blog.setCollectNum(blog.getCollectNum()-1);
        blogService.saveOrUpdate(blog);
        favorite.setNum(favorite.getNum()-1);
        favoriteService.saveOrUpdate(favorite);
        return Result.succ("");
    }

    @RequiresAuthentication()
    @RequestMapping(value = "/list/{favoriteId}",method = RequestMethod.GET)
    public Result getFavorite(@PathVariable(name="favoriteId")Long favoriteId, @RequestParam("userId")Long userId){
        List<Favoriteinfo> list = favoriteinfoService.list(new QueryWrapper<Favoriteinfo>().eq("favorite_id", favoriteId).eq("user_id", userId));
        List<FavoriteinfoDto> favoriteinfoDtoList=new ArrayList<>();
        for (Favoriteinfo favoriteinfo : list) {
            favoriteinfoDtoList.add(new FavoriteinfoDto(favoriteinfo.getBlogId(),favoriteinfo.getBlogName()));
        }
        return Result.succ(MapUtil.builder().put("favoriteList",favoriteinfoDtoList).map());
    }

    @RequiresAuthentication()
    @RequestMapping(value = "/update/{oldFavoriteId}",method = RequestMethod.POST)
    public Result moveFavorite(@Validated @RequestBody Favoriteinfo favoriteinfo,@PathVariable("oldFavoriteId")Long favoriteId){
        User user=userService.getOne(new QueryWrapper<User>().eq("username",userService.getProfileId()));
        Assert.isTrue(favoriteinfo.getUserId().longValue() == user.getId().longValue(), "没有权限更新");
        Favorite old=favoriteService.getById(favoriteId);
        Favorite newF=favoriteService.getById(favoriteinfo.getFavoriteId());
        favoriteinfoService.update(new UpdateWrapper<Favoriteinfo>().eq("blog_id",favoriteinfo.getBlogId())
                .eq("user_id",user.getId()).set("favorite_id",favoriteinfo.getFavoriteId()));
        old.setNum(old.getNum()-1);
        favoriteService.saveOrUpdate(old);
        newF.setNum(newF.getNum()+1);
        favoriteService.saveOrUpdate(newF);
        return Result.succ("");
    }




}
