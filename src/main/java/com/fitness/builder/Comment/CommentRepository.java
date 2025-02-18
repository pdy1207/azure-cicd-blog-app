package com.fitness.builder.Comment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // 게시글 ID로 댓글 리스트를 조회하는 메서드
    List<Comment> findByItemId(Long itemId);
}
