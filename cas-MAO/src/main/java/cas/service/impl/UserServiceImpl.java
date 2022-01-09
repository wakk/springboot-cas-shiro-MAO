package cas.service.impl;

import cas.entity.User;
import cas.mapper.UserMapper;
import cas.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.buji.pac4j.subject.Pac4jPrincipal;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.pac4j.core.profile.CommonProfile;
import org.springframework.stereotype.Service;

/**
 * Created by wwu on 2017/4/13.
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
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
