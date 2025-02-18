package com.fitness.builder.Comment;

import com.fitness.builder.Item.Item;
import com.fitness.builder.User.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;  // 댓글이 속한 게시글

    private String content;  // 댓글 내용

    private LocalDateTime createdAt = LocalDateTime.now();  // 댓글 작성 시간

}

