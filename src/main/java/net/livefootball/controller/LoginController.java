package net.livefootball.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/login")
public class LoginController {

    @GetMapping("/error")
    public @ResponseBody
    String loginError(HttpServletResponse response){
        response.setStatus(401);
        return "you entered wrong username or password";
    }

    @GetMapping("/success")
    public @ResponseBody
    String loginSuccess(){
        return "success";
    }
}