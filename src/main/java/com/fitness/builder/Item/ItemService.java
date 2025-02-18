package com.fitness.builder.Item;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;  // 아이템 레포지토리 주입
    private final ItemImageRepository itemImageRepository;

    public Item savePost(Item item, List<Map<String, Object>> images) {
        // 게시글 저장
        Item savedItem = itemRepository.save(item);

        if (images != null && !images.isEmpty()) {
            for (Map<String, Object> image : images) {
                String imageUrl = (String) image.get("url");
                ItemImage itemImage = new ItemImage();
                itemImage.setItem(savedItem);
                itemImage.setImageUrl(imageUrl);
                itemImageRepository.save(itemImage);
            }
        }

        return savedItem;
    }

    public List<Item> getAllPosts() {
        return itemRepository.findAll();
    }

    public Item getPostById(Long id){
        return itemRepository.findById(id).orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
    }

    // ⭐ 좋아요 증가
    @Transactional
    public boolean likeItem(Long id) {
        Optional<Item> optionalItem = itemRepository.findById(id);
        if (optionalItem.isPresent()) {
            Item item = optionalItem.get();
            item.incrementLikes();
            itemRepository.save(item);
            return true;
        }
        return false;
    }

}
