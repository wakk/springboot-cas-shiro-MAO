package com.zxm.job.executor.jobhandler;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;
import com.xxl.job.core.util.ShardingUtil;
import com.zxm.common.dto.BlogDto;
import com.zxm.entity.Blog;
import com.zxm.entity.User;
import com.zxm.service.BlogService;
import com.zxm.service.UserService;
import com.zxm.utils.FtpUtil;
import com.zxm.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 任务Handler示例（Bean模式）
 * 开发步骤：
 * 1、继承"IJobHandler"：“com.xxl.job.core.handler.IJobHandler”；
 * 2、注册到Spring容器：添加“@Component”注解，被Spring容器扫描为Bean实例；
 * 3、注册到执行器工厂：添加“@JobHandler(value="自定义jobhandler名称")”注解，注解value值对应的是调度中心新建任务的JobHandler属性的值。
 * 4、执行日志：需要通过 "XxlJobLogger.log" 打印执行日志；
 * 5、任务执行结果枚举：SUCCESS、FAIL、FAIL_TIMEOUT
 */


@Component
@Slf4j
public class DemoJobHandler{
    public static Boolean isSubmitTask=false;//是否有任务提交
    public static int circleNum=0;//运行总轮数
    public static final int CIRCLE_TIME=10;//周期数,分钟
    public static Boolean needUpdate=false;//更新过程中是否有任务提交
    public static Boolean running=false;
    private static Logger logger = LoggerFactory.getLogger(DemoJobHandler.class);

    @Autowired
    BlogService blogService;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    UserService userService;

    private static String projectName;
    @Value("${project.name}")
    /**
     * 1、更新redis缓存，运行条件：有任务提交或者运行次数达到周期数
     */
    @XxlJob("demoJobHandler")
    public ReturnT<String> demoJobHandler(String param) throws Exception {
        if(isSubmitTask||(++circleNum==CIRCLE_TIME)){
            long begin = System.currentTimeMillis();
            running=true;
            List<BlogDto> list = blogService.getBlogs();
            running=false;
            redisUtil.set("blogDto",list);
            circleNum=0;
            if(needUpdate)circleNum=8;//如果有在更新期间提交任务的，先记录下来，并在2周期后更新。
            isSubmitTask=false;
            needUpdate=false;
            log.info("更新博客首页的redis缓存：[" + (System.currentTimeMillis()-begin) + "]毫秒");
        }
        return ReturnT.SUCCESS;
    }
    @XxlJob("clearCacheJobHandler")
    public ReturnT<String> clearCacheJobHandler(String param) throws Exception {
        List<Blog> list = blogService.list(new QueryWrapper<Blog>().select("id","user_id").orderByAsc("user_id"));
        Map<Long, User> userMap = userService.getUserMap();
        long pre=list.get(0).getUserId();
        Set<String> set=new HashSet<>();
        for (Blog blog : list) {
            String name="blogImg-"+blog.getUserId()+"-"+blog.getId();
            if(blog.getUserId().longValue()!=pre){
                FtpUtil.clearOldImg(projectName+"/content/blog-"+userMap.get(pre).getUsername(),set);
                set.clear();
            }
            if(redisUtil.hasKey(name)) {
                List<String> str = (List<String>) redisUtil.get(name);
                set.addAll(str);
            }
            pre=blog.getUserId();
        }
        FtpUtil.clearOldImg(projectName+"/content/blog-"+userMap.get(pre).getUsername(),set);
        return ReturnT.SUCCESS;
    }

    public void init(){
        logger.info("init");
    }
    public void destroy(){
        logger.info("destory");
    }


}