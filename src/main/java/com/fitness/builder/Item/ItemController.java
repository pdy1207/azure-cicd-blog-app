package com.fitness.builder.Item;

import com.fitness.builder.User.User;
import com.fitness.builder.User.UserRepository;
import com.fitness.builder.User.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final S3Service s3Service;
    private final ItemService itemService;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;


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

    @PostMapping("/save-post")
    @ResponseBody
    public ResponseEntity<?> savePost(@RequestBody Map<String, Object> postData,
                                      @AuthenticationPrincipal User user, Authentication info) { // 로그인한 사용자 정보 받기
        try {
            // title, content, images 파싱
            String title = (String) postData.get("title");
            String content = (String) postData.get("content");
            List<Map<String, Object>> images = (List<Map<String, Object>>) postData.get("images");

            // 만약 유저가 null인 경우 DB에 새로운 유저를 저장해야 한다면:
            if (user == null) {
                // 새로 User 저장 (예시로 이메일만 저장한다고 가정)
                user = new User();
                user.setUsername(info.getName());
                user.setEmail("pdyme1207@gmail.com");
                userRepository.save(user);
            }

            Item item = new Item();
            item.setTitle(title);
            item.setContent(content);
            item.setUser(user);  // 작성자 설정

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

    @GetMapping("/detail-list/{id}")
    String detailList(@PathVariable Long id, Model model) {
        // Item 객체 조회
        Item post = itemService.getPostById(id);

        // 이미지 리스트에서 첫 번째 이미지만 처리
        if (post.getImages() != null && !post.getImages().isEmpty()) {
            ItemImage firstImage = post.getImages().get(0);
            post.setImages(Collections.singletonList(firstImage));  // 첫 번째 이미지만 저장
        } else {
            System.out.println("이미지가 없습니다.");
        }

        // User 객체 가져오기
        User user = post.getUser();  // Item 객체의 User 필드 접근
        String username = user != null ? user.getUsername() : "사용자 없음";  // 사용자 이름을 조회, 없으면 기본 값 설정

        // content에서 HTML 태그를 제거
        String content = post.getContent().replaceAll("<[^>]*>", ""); // HTML 태그 제거

        // 소제목을 10글자로 자르기
        String abbreviatedContent = content.length() > 10 ? content.substring(0, 15) : content; // 10글자 이하로 자르기


        // 모델에 post와 username 추가
        model.addAttribute("post", post);
        model.addAttribute("username", username);
        model.addAttribute("abbreviatedContent", abbreviatedContent);

        return "detailList.html";
    }

    @GetMapping("/edit-write/{id}")
    public String editWrite(@PathVariable Long id, Model model) {
        Item item = itemRepository.findByIdWithImages(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 글이 존재하지 않습니다: " + id));

        model.addAttribute("item", item);
        return "editWrite";
    }






}
