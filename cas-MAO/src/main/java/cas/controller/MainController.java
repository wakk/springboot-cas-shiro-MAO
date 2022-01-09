package cas.controller;

import cas.common.lang.Result;
import cas.entity.User;
import cas.service.UserService;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by wwu on 2017/4/13.
 */
@Controller
@RequestMapping("/")
public class MainController {

    @RequestMapping(value = {"","index"},method = RequestMethod.GET)
    public void index(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String url="https://xxxxxxx.top/";
        System.out.println("index");
        response.sendRedirect(url);
    }

    @RequiresAuthentication()
    @RequestMapping(value = "login",method = RequestMethod.GET)
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String url="https://xxxxxxx.top/";
        System.out.println("login");
        response.sendRedirect(url);
    }


}