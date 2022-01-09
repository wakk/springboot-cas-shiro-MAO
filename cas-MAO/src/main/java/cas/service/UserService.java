package cas.service;

import cas.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * Created by wwu on 2017/4/13.
 */
public interface UserService extends IService<User> {
    String getProfileId();
}
