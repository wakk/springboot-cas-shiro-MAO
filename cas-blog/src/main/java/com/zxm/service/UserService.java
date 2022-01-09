package com.zxm.service;

import com.zxm.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zxm
 * @since 2021-12-10
 */
public interface UserService extends IService<User> {
    public Map<Long,User> getUserMap();
    public String getProfileId();
}
