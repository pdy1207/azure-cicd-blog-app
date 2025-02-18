package com.fitness.builder.Comment;
import com.fitness.builder.Item.Item;
import com.fitness.builder.Item.ItemRepository;
import com.fitness.builder.User.User;
import com.fitness.builder.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;

    @PostMapping("/detail-list/{id}/comment")
    public String addComment(@PathVariable Long id, @RequestParam String commentcontent) {


        // 게시글을 찾기
        Item item = itemRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        // Comment 객체 생성
        Comment comment = new Comment();
        comment.setContent(commentcontent);
        comment.setItem(item);

        // 댓글 저장
        commentRepository.save(comment);

        // 댓글 추가 후 상세 페이지로 리디렉션
        return "redirect:/detail-list/" + id;
    }



}

