package com.blog.controller;

import com.blog.service.impl.loginServiceimpl;
import com.blog.utils.SHA256;
import com.blog.utils.ScriptUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    private static loginServiceimpl ls = new loginServiceimpl();
    SHA256 sha256 = new SHA256();

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/loginCheck", method = RequestMethod.POST)
    public String login(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        request.setCharacterEncoding("utf-8");
        String userID = request.getParameter("userID");
        String userPassword = sha256.encrypt(request.getParameter("userPassword"));
        if (session.getAttribute("userID") != null ){
            session.removeAttribute("userID");
        }
        var u = ls.login(userID, userPassword);
        if (u == 1) {
            session.setAttribute("userID", userID);
            return "redirect:main";
        } else {
            ScriptUtils.alertAndBackPage(response, "아이디또는 패스워드가 틀렸습니다.");
        }
        return null;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @ResponseBody
    public void logout(HttpServletResponse response, HttpSession session) throws Exception {
        session.invalidate();
        ScriptUtils.alertAndMovePage(response, "로그아웃 되었습니다.", "/NKBlog/main");
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register() {
        return "join";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(HttpServletRequest request, HttpSession session) throws Exception {
        request.setCharacterEncoding("utf-8");
        String userID = request.getParameter("userID");
        String userPassword = sha256.encrypt(request.getParameter("userPassword"));
        String userGender = request.getParameter("userGender");
        ls.register(userID, userPassword, userGender);
        log.debug("user register request....");
        if (userID == null && userPassword == null && userGender == null) {
            log.debug("값이 없습니다.");
            return "redirect:join";
        } else {
            return "redirect:login";
        }
    }
}
