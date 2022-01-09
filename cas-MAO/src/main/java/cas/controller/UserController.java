package cas.controller;


import cas.common.dto.RegisterDto;
import cas.common.lang.Result;
import cas.entity.User;
import cas.mapper.UserMapper;
import cas.service.UserService;
import cas.utils.RedisUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zxm
 * @since 2021-11-10
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public Result register(@Validated @RequestBody User user) {
        User temp = userService.getOne(new QueryWrapper<User>().eq("username", user.getUsername()));
        if (temp != null) return Result.fail("用户名已存在");

        user.setPassword(SecureUtil.md5(user.getPassword()));
        user.setCreated(LocalDateTime.now());
        user.setStatus(0);
        userService.saveOrUpdate(user);

        return Result.succ(null);

    }

    @RequestMapping(value = "/headInfo",method = RequestMethod.GET)
    public Result getHeadInfo(){
        String name=userService.getProfileId();
        System.out.println("headInfo:"+name);
        if(name==null)return Result.succ(MapUtil.builder().put("username",null).map());
        User temp = userService.getOne(new QueryWrapper<User>().eq("username", name));
        if(temp==null)return Result.fail("用户不存在");
        return Result.succ(MapUtil.builder()
                .put("id",temp.getId())
                .put("username", temp.getUsername())
                .put("avatar", temp.getAvatar())
                .map());
    }

    @RequestMapping(value="/partInfo/{id}",method = RequestMethod.GET)
    public Result getPartInfo(@PathVariable(name = "id")Long id){
        User temp = userService.getById(id);
        if(temp==null)return Result.fail("该用户不存在");
        return Result.succ(MapUtil.builder()
                .put("username", temp.getUsername())
                .put("avatar", temp.getAvatar())
                .put("email", temp.getEmail())
                .map());
    }

//    @RequiresAuthentication()
    @RequestMapping(value = "/userInfo",method = RequestMethod.GET)
    public Result getUserInfo(){
        String name=userService.getProfileId();
        if(name==null)return Result.succ(MapUtil.builder().put("username",null).map());
        User temp = userService.getOne(new QueryWrapper<User>().eq("username", name));
        if(temp==null)return Result.fail("用户不存在");
        return Result.succ(MapUtil.builder()
                .put("id", temp.getId())
                .put("username", temp.getUsername())
                .put("avatar", temp.getAvatar())
                .put("email", temp.getEmail())
                .put("sex", temp.getSex())
                .put("company",temp.getCompany())
                .put("address",temp.getAddress())
                .map()
        );
    }

    @RequiresAuthentication()
    @RequestMapping(value = "/updateUser",method = RequestMethod.POST)
    public Result updateUser(@Validated @RequestBody User user) {
        String name=userService.getProfileId();
        if(name==null)return Result.fail(null);
        User temp = userService.getOne(new QueryWrapper<User>().eq("username", name));
        if(temp.getId()!=user.getId())return Result.fail("用户非法修改异常");
        userService.saveOrUpdate(user);
        return Result.succ(null);
    }

    @RequestMapping(value = "/history",method = RequestMethod.GET)
    public Result getHistory(){
        long l1=System.currentTimeMillis();
        String name=userService.getProfileId();
        if(name==null)return Result.succ(MapUtil.builder().put("username",null).map());
        //从redis获取数据
        final Set<String> historySet = redisTemplate.opsForZSet().range("history-"+name, 0, -1);
        System.out.println("redis取数据时间： "+(System.currentTimeMillis()-l1));
        System.out.println(historySet);
        int len=historySet.size()>8?8: historySet.size();
        int [] index1=new int[len];
        int [] index2=new int[len];
        int i=len;
        for(String str:historySet){
            if(--i<0)break;
            String[] split = str.split("-");
            index1[i]=Integer.parseInt(split[0]);
            index2[i]=Integer.parseInt(split[1]);
        }
        System.out.println("总时间： "+(System.currentTimeMillis()-l1));
        return Result.succ(MapUtil.builder()
                .put("username", name)
                .put("index1",index1)
                .put("index2", index2)
                .map()
        );
    }

    @RequestMapping(value = "/saveHistory/{tag}",method = RequestMethod.GET)
    public Result saveHistory(@PathVariable(name = "tag") String tag){
        String name=userService.getProfileId();
        if(name==null)Result.succ(null);
        //往redis存数据
        redisTemplate.expire("history-"+name,3, TimeUnit.DAYS);
        redisTemplate.opsForZSet().add("history-"+name,tag,System.currentTimeMillis());
        return Result.succ(null);
    }



}
