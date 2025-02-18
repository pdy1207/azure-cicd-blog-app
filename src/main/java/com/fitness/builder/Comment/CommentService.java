package com.fitness.builder.Comment;

import com.fitness.builder.Item.Item;
import com.fitness.builder.Item.ItemRepository;
import com.fitness.builder.User.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private final ItemRepository itemRepository;

    public List<Comment> getCommentsByItemId(Long itemId) {
        return commentRepository.findByItemId(itemId);
    }

    public Comment addComment(Long itemId, String content, User user) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        Comment comment = new Comment();
        comment.setItem(item);
        comment.setContent(content);
        return commentRepository.save(comment);
    }
}
