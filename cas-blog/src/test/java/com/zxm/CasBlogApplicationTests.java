package com.zxm;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zxm.common.dto.BlogDto;
import com.zxm.entity.*;
import com.zxm.service.*;
import com.zxm.utils.FtpUtil;
import com.zxm.utils.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SpringBootTest
class CasBlogApplicationTests {
    @Autowired
    private BlogService userMapper;

    @Autowired
    FavoriteService favoriteService;
    @Autowired
    FavoriteinfoService favoriteinfoService;
    @Autowired
    FolderinfoService folderinfoService;
    @Autowired
    FolderService folderService;
    @Autowired
    BlogService blogService;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    UserService userService;

//    @Test
//    void contextLoads() {
//        int a=Integer.MAX_VALUE;
//        for (int i=0;i<100;i++) {
//            a++;
//            System.out.println((a&15)==15);
//        }
//        System.out.println(Integer.MIN_VALUE);
//    }
//    @Test
//    public void test1(){
//        Set<String> set=new HashSet<>();
//        set.add("1638455464429397.jpg");
//        set.add("1638455534978524.jpg");
////        set.add("1638455829963986.png");
//        FtpUtil.checkMemory("/vueblog/content/blog-test/20211202");
//    }
//    @Test
//    public void test2(){
//        List<Blog> list = blogService.list(new QueryWrapper<Blog>().select("id","user_id").orderByAsc("user_id"));
//        Map<Long, User> userMap = userService.getUserMap();
//        long pre=list.get(0).getUserId();
//        Set<String> set=new HashSet<>();
//        for (Blog blog : list) {
//            String name="blogImg-"+blog.getUserId()+"-"+blog.getId();
//            if(blog.getUserId().longValue()!=pre){
//                FtpUtil.clearOldImg("/content/"+"blog-"+userMap.get(pre).getUsername(),set);
//                set.clear();
//            }
//            if(redisUtil.hasKey(name)) {
//                List<String> str = (List<String>) redisUtil.get(name);
//                set.addAll(str);
//            }
//            pre=blog.getUserId();
//        }
//        FtpUtil.clearOldImg("/content/"+"blog-"+userMap.get(pre).getUsername(),set);
//    }
//
//    @Test
//    public void updateRedis(){
//        System.out.println("更新博客首页的redis缓存");
//        List<BlogDto> list = blogService.getBlogs();
//        redisUtil.set("blogDto",list);
//    }

}
