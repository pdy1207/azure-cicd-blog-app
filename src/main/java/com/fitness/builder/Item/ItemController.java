package com.fitness.builder.Item;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final S3Service s3Service;

//    @PreAuthorize("isAuthenticated()") 로그인한 사용자 확인
    @GetMapping("/write")
    String write(){
        return "write.html";
    }

    @GetMapping("/presigned-url")
    @ResponseBody
    String getListPage(@RequestParam String filename) {
        var result = s3Service.createPresignedUrl("builder/" + filename);
        System.out.println(result);
        return result;
    }
}
