package com.zxm.controller;


import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zxm.common.dto.ChildDto;
import com.zxm.common.dto.FolderDto;
import com.zxm.common.lang.Result;
import com.zxm.entity.*;
import com.zxm.service.FolderService;
import com.zxm.service.FolderinfoService;
import com.zxm.service.UserService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zxm
 * @since 2021-12-10
 */
@RestController
@RequestMapping("/folder")
public class FolderController {
    @Autowired
    FolderService folderService;
    @Autowired
    FolderinfoService folderinfoService;
    @Autowired
    UserService userService;
    /**
     * 返回前端专栏的传输对象
     * @param id 用户id
     * @return com.zxm.common.lang.Result
     * @author zhixiumao
     * @creed: Talk is cheap,show me the code
     * @date 2021/12/12 22:03
     */
    @RequestMapping(value = "/allInfo/{id}",method = RequestMethod.GET)
    public Result getFolder(@PathVariable(name="id")int id){
        Map<Long,FolderDto> map = folderService.getFolder(id);
        if(map==null)return Result.succ(MapUtil.builder().put("folderList",null).map());
        List<FolderDto> folderList=new ArrayList<>();
        List<Folderinfo> folderInfoList = folderinfoService.list(new QueryWrapper<Folderinfo>().eq("user_id", id));
        for (Folderinfo fi : folderInfoList){
            ChildDto cd=new ChildDto(fi.getBlogId(),fi.getBlogName());
            map.get(fi.getFolderId()).getChildren().add(cd);
        }
        map.forEach((key, value) -> folderList.add(value));
        return Result.succ(MapUtil.builder().put("folderList",folderList).map());
    }

    @RequestMapping(value = "/partInfo/{userId}",method = RequestMethod.GET)
    public Result getFavorite(@PathVariable(name="userId")Long id){
        List<Folder> list= folderService.list(new QueryWrapper<Folder>().eq("user_id",id));
        List<Folderinfo> folderinfoList=folderinfoService.list(new QueryWrapper<Folderinfo>().select("blog_id").eq("user_id",id));
        return Result.succ(MapUtil.builder().put("folderList",list)
                .map());
    }

    @RequiresAuthentication()
    @RequestMapping(value = "/create",method = RequestMethod.POST)
    public Result createFavorite( @RequestParam("folderName") String name){
        User user=userService.getOne(new QueryWrapper<User>().eq("username",userService.getProfileId()));
        folderService.saveOrUpdate(new Folder(null,name,user.getId()));
        return Result.succ("");
    }

    @RequiresAuthentication()
    @RequestMapping(value = "/clearNullFolder/{userId}",method = RequestMethod.GET)
    public Result clearNullFolder( @PathVariable("userId") Long userId){
        User user=userService.getOne(new QueryWrapper<User>().eq("username",userService.getProfileId()));
        Assert.isTrue(userId == user.getId().longValue(), "没有清理专栏");
        List<Folder> folderList = folderService.list(new QueryWrapper<Folder>().eq("user_id", userId));
        List<Folderinfo> list = folderinfoService.list(new QueryWrapper<Folderinfo>().eq("user_id", userId));
        Set<Long> set=new HashSet<>();
        for (Folderinfo fi : list){
           set.add(fi.getFolderId());
        }
        for (Folder folder : folderList) {
            Long fid=folder.getId();
            if(!set.contains(fid))folderService.removeById(fid);
        }
        return Result.succ("");
    }
}
