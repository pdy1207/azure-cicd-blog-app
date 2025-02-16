package com.fitness.builder.Item;

import com.fitness.builder.User.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@Getter
@Setter
@ToString
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob // 큰 텍스트 저장을 위한 어노테이션
    private String content;

    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    // User와의 관계 설정 (작성자)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")  // 외래키 이름
    private User user; // 작성자

    @PrePersist
    public void prePersist() {
        createAt = updateAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updateAt = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ItemImage> images; // Item에 속한 이미지 리스트

    // Item에서 이미지 URL을 가져오는 메서드 추가
    public List<String> getImageUrls() {
        return images != null ? images.stream().map(ItemImage::getImageUrl).collect(Collectors.toList()) : null;
    }
}
