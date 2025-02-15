package com.fitness.builder.Basic;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class BasicController {

    @GetMapping("/")
    String index(){
        return "index.html";
    }



}
