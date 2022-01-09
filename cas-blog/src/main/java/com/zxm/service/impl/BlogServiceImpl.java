package com.zxm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zxm.common.dto.BlogDto;
import com.zxm.entity.Blog;
import com.zxm.entity.User;
import com.zxm.mapper.BlogMapper;
import com.zxm.mapper.UserMapper;
import com.zxm.service.BlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxm.service.UserService;
import com.zxm.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zxm
 * @since 2021-12-10
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {
    @Autowired
    UserService userService;
    @Autowired
    BlogMapper blogMapper;
    @Autowired
    RedisUtil redisUtil;

    @Override
    public List<BlogDto> getBlogs() {
        List<Blog> list = blogMapper.selectList(new QueryWrapper<Blog>().orderByDesc("created"));
        Map<Long,User> userMap=userService.getUserMap();
        List<BlogDto> blogDtoList=new ArrayList<>();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for(Blog blog:list){
            User user=userMap.get(blog.getUserId());
            blogDtoList.add(new BlogDto(blog.getId(),user.getUsername(), df.format(blog.getCreated()),
                    blog.getTitle(),blog.getDescription(),user.getAvatar(),blog.getCollectNum()));
        }
        if(blogDtoList.size()==0)return null;
        return blogDtoList;
    }

    @Override
    public void saveUrlCache(Blog blog) {
        String name="blogImg-"+blog.getUserId()+"-"+blog.getId();
        String content= blog.getContent();
        String[] strSplit=content.split("!\\[.*]\\(https:/*xxxxxx.top:9090/");
        List<String> list=new ArrayList<>();
        for(int i=1;i<strSplit.length;i++){
            String url=strSplit[i].substring(0,strSplit[i].indexOf(')'));
            list.add(url.substring(url.lastIndexOf('/')+1,url.length()));
        }
        if(list.size()>0)redisUtil.set(name,list);
    }
}
