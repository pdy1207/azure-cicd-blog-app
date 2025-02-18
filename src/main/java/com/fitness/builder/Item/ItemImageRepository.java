package com.fitness.builder.Item;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemImageRepository extends JpaRepository<ItemImage, Long> {
    List<ItemImage> findByItemId(Long itemId);
    void deleteByItemId(Long itemId);  // item_id가 일치하는 데이터 삭제
}
