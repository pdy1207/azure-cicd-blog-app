package com.fitness.builder.Item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByUserId(Long userId);  // userId로 Item 조회
    List<Item> findByIdGreaterThanEqual(Long id);
    @Query("SELECT i FROM Item i LEFT JOIN FETCH i.images WHERE i.id = :id")
    Optional<Item> findByIdWithImages(@Param("id") Long id);
}
