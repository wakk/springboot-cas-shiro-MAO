package com.zxm.controller;


import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zxm.common.dto.ChildDto;
import com.zxm.common.dto.FolderDto;
import com.zxm.common.lang.Result;
import com.zxm.entity.*;
import com.zxm.service.*;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zxm
 * @since 2021-12-10
 */
@RestController
@RequestMapping("/favorite")
public class FavoriteController {
    @Autowired
    BlogService blogService;
    @Autowired
    FavoriteService favoriteService;
    @Autowired
    FavoriteinfoService favoriteinfoService;
    @Autowired
    UserService userService;

    @RequiresAuthentication()
    @RequestMapping(value = "/list/{userId}",method = RequestMethod.GET)
    public Result getFolder(@PathVariable(name="userId")int userId){
        List<Favorite> list= favoriteService.list(new QueryWrapper<Favorite>().eq("user_id",userId));
        return Result.succ(MapUtil.builder().put("favoriteList",list).map());
    }

    @RequiresAuthentication()
    @RequestMapping(value = "/partInfo/{userId}",method = RequestMethod.GET)
    public Result getFavorite(@PathVariable(name="userId")Long id, @RequestParam("blogId")Long blogId){
        List<Favorite> list= favoriteService.list(new QueryWrapper<Favorite>().eq("user_id",id));
        List<Favoriteinfo> favoriteinfoList=favoriteinfoService.list(new QueryWrapper<Favoriteinfo>().select("blog_id").eq("user_id",id));
        boolean isCollect=false;
        for (Favoriteinfo favoriteinfo : favoriteinfoList) {
            if(favoriteinfo.getBlogId().longValue()==blogId.longValue()){
                isCollect=true;break;
            }
        }
        return Result.succ(MapUtil.builder().put("favoriteList",list)
                .put("isCollect",isCollect).map());
    }

    @RequiresAuthentication()
    @RequestMapping(value = "/create",method = RequestMethod.POST)
    public Result createFavorite( @RequestParam("favoriteName")String name){
        User user=userService.getOne(new QueryWrapper<User>().eq("username",userService.getProfileId()));
        Favorite favorite=new Favorite();
        favorite.setName(name);favorite.setUserId(user.getId());
        favoriteService.saveOrUpdate(favorite);
        return Result.succ("");
    }

    @RequiresAuthentication()
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public Result updateFavorite(@Validated @RequestBody Favorite favorite){
        User user=userService.getOne(new QueryWrapper<User>().eq("username",userService.getProfileId()));
        Assert.isTrue(favorite.getUserId().longValue() == user.getId().longValue(), "没有权限更新");
        favoriteService.saveOrUpdate(favorite);
        return Result.succ("");
    }

    @RequiresAuthentication()
    @RequestMapping(value = "/delete/{favoriteId}",method = RequestMethod.GET)
    public Result deleteFavorite(@PathVariable("favoriteId")Long favoriteId){
        Favorite favorite = favoriteService.getById(favoriteId);
        if(favorite==null)return Result.fail("该收藏夹不存在");
        User user=userService.getOne(new QueryWrapper<User>().eq("username",userService.getProfileId()));
        Assert.isTrue(favorite.getUserId().longValue() == user.getId().longValue(), "没有权限删除");
        System.out.println("删除favoriteId: "+favoriteId);
        favoriteService.removeById(favoriteId);
        return Result.succ("");
    }


}
