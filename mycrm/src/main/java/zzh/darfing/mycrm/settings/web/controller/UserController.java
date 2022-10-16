package zzh.darfing.mycrm.settings.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import zzh.darfing.mycrm.commons.pojo.Result;
import zzh.darfing.mycrm.commons.constants.Constants;
import zzh.darfing.mycrm.commons.utils.DateFormatUtil;
import zzh.darfing.mycrm.settings.pojo.User;
import zzh.darfing.mycrm.settings.web.service.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/settings/qx/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/toLogin.do")
    public String toLogin() {
        return "settings/qx/user/login";
    }

    @RequestMapping("/login.do")
    @ResponseBody
    public Object login(String loginAct, String loginPwd, String isRemPwd, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>();
        map.put("loginAct", loginAct);
        map.put("loginPwd", loginPwd);
        User user = userService.queryUserByLoginActAndPwd(map);
        String nowString = DateFormatUtil.formatDateAndTime(new Date());
        Result result = new Result();
        if (user == null) {
            //登录失败，用户名或密码错误
            result.setCode(Constants.FAIL);
            result.setMessage("用户名或密码错误");
            return result;
        } else if ("0".equals(user.getLockState())) {
            //账号已被冻结
            result.setCode(Constants.FAIL);
            result.setMessage("账号已被冻结");
            return result;
        } else if (nowString.compareTo(user.getExpireTime()) > 0) {
            //时间过期
            result.setCode("0");
            result.setMessage("账号已过期");
            return result;
        } else if (!user.getAllowIps().contains(request.getRemoteAddr())) {
            //ip不支持
            result.setCode("0");
            result.setMessage("ip不支持");
            return result;
        } else {
            result.setCode(Constants.SUCCESS);
            HttpSession session = request.getSession();
            session.setAttribute(Constants.SESSION_USER, user);
            if("true".equals(isRemPwd)) { //如果勾选了10天内免登陆，就返回给浏览器两个个有效期为10天的账号和密码Cookie
                Cookie loginActCookie = new Cookie("loginAct", loginAct);
                Cookie loginPwdCookie = new Cookie("loginPwd", loginPwd);
                loginActCookie.setMaxAge(10*24*60*60);
                loginPwdCookie.setMaxAge(10*24*60*60);
                response.addCookie(loginActCookie);
                response.addCookie(loginPwdCookie);
            } else { //否则就返回一个失效的cookie
                Cookie loginActCookie = new Cookie("loginAct", null);
                Cookie loginPwdCookie = new Cookie("loginPwd", null);
                loginActCookie.setMaxAge(0);
                loginPwdCookie.setMaxAge(0);
                response.addCookie(loginActCookie);
                response.addCookie(loginPwdCookie);
            }
            return result;
        }
    }

    @RequestMapping("/logout.do")
    public String safeLogout(HttpSession session, HttpServletResponse response) {
        Cookie loginActCookie = new Cookie("loginAct", null);
        Cookie loginPwdCookie = new Cookie("loginPwd", null);
        loginActCookie.setMaxAge(0);
        loginPwdCookie.setMaxAge(0);
        response.addCookie(loginActCookie);
        response.addCookie(loginPwdCookie);
        session.invalidate();
        return "redirect:/";
    }
}
