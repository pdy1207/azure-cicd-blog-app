package com.fitness.builder.Item;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final S3Service s3Service;
    private final ItemService itemService;

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

    // 게시글 저장 요청
    @PostMapping("/save-post")
    @ResponseBody
    public ResponseEntity<?> savePost(@RequestBody Map<String, Object> postData) {
        try {
            // title, content, images 파싱
            String title = (String) postData.get("title");
            String content = (String) postData.get("content");
            List<Map<String, Object>> images = (List<Map<String, Object>>) postData.get("images");

            Item item = new Item();
            item.setTitle(title);
            item.setContent(content);

            // 서비스 레이어에서 게시글 저장
            Item savedItem = itemService.savePost(item, images);

            return ResponseEntity.ok(new Response("success", "글이 성공적으로 저장되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response("error", "서버 오류입니다."));
        }
    }


    // Response 객체 (API 응답 형식)
    static class Response {
        private String status;
        private String message;

        public Response(String status, String message) {
            this.status = status;
            this.message = message;
        }

        public String getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }
    }

    @GetMapping("/detail")
    String detail(){
        return "detail.html";
    }


}
