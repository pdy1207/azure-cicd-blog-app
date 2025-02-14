package com.fitness.builder;

import com.fitness.builder.Member.CustomUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class BasicController {
    @GetMapping("/")
    String index(){
        return "index.html";
    }

    @GetMapping("/detail")
    String detail(){
        return "detail.html";
    }

    @GetMapping("/write")
    String write(){
        return "write.html";
    }

}
