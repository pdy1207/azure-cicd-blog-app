package com.fitness.builder.Basic;

import com.fitness.builder.Item.Item;
import com.fitness.builder.Item.ItemImageRepository;
import com.fitness.builder.Item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BasicController {

    private final ItemRepository itemRepository;
    private final ItemImageRepository itemImageRepository;  // ItemImageRepository 추가

    @GetMapping("/")
    public String index(Model model) {
        List<Item> items = itemRepository.findByIdGreaterThanEqual(59L);

        // items의 user 정보를 가져와서 username을 함께 전달
        for (Item item : items) {
            // 이미지 URL을 아이템에 저장된 리스트로 설정
            List<String> imageUrls = item.getImageUrls();  // getImageUrls() 메서드 호출
            // 이미지를 출력할 수 있도록 imageUrls를 모델에 추가하는 방법
            model.addAttribute("imageUrls", imageUrls);
        }

        model.addAttribute("items", items);  // items 목록을 모델에 추가

        return "index"; // index.html로 이동
    }
}
