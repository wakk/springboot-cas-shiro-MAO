package cas.controller;

import cas.common.lang.Result;
import cas.entity.User;
import cas.service.NginxService;
import cas.service.UserService;
import cas.utils.FtpUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import  java.io.File;

@RestController
@Slf4j
public class UploadController {
    @Autowired
    private NginxService nginxService;
    @Autowired
    private UserService userService;
    @Value("${ftp.img.url}")
    private String imgUrl;


    @RequiresAuthentication()//需要登录认证才能通过，否则返回401
    @RequestMapping(value="/upload",method = RequestMethod.POST)
    public Result avatarUpload(@RequestParam(value = "file") MultipartFile uploadFile) {
        String name=userService.getProfileId();//Post方法有时候返回null
        log.info("uploadFile: "+name);
        User user= userService.getOne(new QueryWrapper<User>().eq("username", name));
        if(user==null)return Result.fail("用户不存在");
        long begin = System.currentTimeMillis();
        String result = (String)nginxService.uploadPicture(uploadFile,false,name);
        log.info("上传图片共耗时：[" + (System.currentTimeMillis()-begin) + "]毫秒");
        String temp=user.getAvatar();
        user.setAvatar(result);
        userService.saveOrUpdate(user);
        if(temp!=null&&temp.startsWith(imgUrl)){//删除服务器多余的图片
            FtpUtil.delImages(StrUtil.removePrefix(temp,imgUrl));
        }
        return Result.succ(MapUtil.builder().put("avatar", result).map());
    }

//    @RequiresAuthentication()//需要登录认证才能通过，否则返回401
    @RequestMapping(value="/uploadBlogImg",method = RequestMethod.POST)
    public Result pictureUploadImg(@RequestParam(value = "file") MultipartFile uploadFile) {
        String name=userService.getProfileId();//Post方法有时候返回null
        log.info("uploadFile: "+name);
        if(name==null)return Result.fail("请重新登录");
        if(!FtpUtil.checkMemory("/vueblog/content/blog-"+name))
            return Result.fail("当前你的图片内存不足，请见谅");
        long begin = System.currentTimeMillis();
        String result = (String)nginxService.uploadPicture(uploadFile,true,name);
        log.info("上传图片共耗时：[" + (System.currentTimeMillis()-begin) + "]毫秒");
        return Result.succ(MapUtil.builder().put("imgUrl", result).map());
    }

//    @RequiresAuthentication()//需要登录认证才能通过，否则返回401
    @RequestMapping(value="/deleteImg",method = RequestMethod.POST)
    public Result deleteImg(@RequestParam(name = "deleteUrl")String deleteUrl){
        String name=userService.getProfileId();//Post方法有时候返回null
        log.info("uploadFile: "+name);
        if(name==null)return Result.fail("请重新登录");
        System.out.println(deleteUrl);
        if(deleteUrl.startsWith(imgUrl)){
            String fileIndex=StrUtil.removePrefix(deleteUrl,imgUrl);
            if(fileIndex.replace("/","").startsWith("blog-"+name)){
                long begin = System.currentTimeMillis();
                FtpUtil.delImages(fileIndex);
                log.info("删除图片共耗时：[" + (System.currentTimeMillis()-begin) + "]毫秒");
            }else{
                return Result.fail("删除服务器图片失败");
            }
        }
        return Result.succ(null);
    }




}