package com.zxm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zxm.entity.User;
import com.zxm.mapper.UserMapper;
import com.zxm.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.buji.pac4j.subject.Pac4jPrincipal;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zxm
 * @since 2021-12-10
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    UserMapper userMapper;
    @Override
    public Map<Long, User> getUserMap() {
        List<User> userList=userMapper.selectList(new QueryWrapper<>());
        System.out.println(userList);
        Map<Long,User> userMap=new HashMap<>();
        for (User user : userList)userMap.put(user.getId(),user);
        return userMap;
    }

    @Override
    public String getProfileId() {
        Subject subject = SecurityUtils.getSubject();
        if (subject == null || subject.getPrincipals() == null) {
            return null;
        }
        PrincipalCollection pcs = subject.getPrincipals();
        Pac4jPrincipal p = pcs.oneByType(Pac4jPrincipal.class);
        System.out.println("p.getProfile().getId()"+p.getProfile().getId());
        if (p != null) {
            return p.getProfile().getId();
        }
        return null;
    }
}
