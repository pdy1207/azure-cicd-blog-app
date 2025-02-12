package com.fitness.builder;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class BasicController {
    @GetMapping("/")
    String index(){
        return "index.html";
    }

    @GetMapping("/detail")
    String detail(){
        return "detail.html";
    }

    @GetMapping("/login")
    String login(){
        return "login.html";
    }

    @GetMapping("/signup")
    String signup(){
        return "signup.html";
    }

    @GetMapping("/write")
    String write(){
        return "write.html";
    }

    @GetMapping("/my-page")
    String myPage(){
        return "mypage.html";
    }
}
