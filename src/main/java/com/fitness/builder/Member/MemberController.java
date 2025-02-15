package com.fitness.builder.Member;

import com.fitness.builder.Item.Item;
import com.fitness.builder.Item.ItemRepository;
import com.fitness.builder.User.User;
import com.fitness.builder.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @GetMapping("/login")
    String login(){
        return "login.html";
    }

    @GetMapping("/signup")
    String signup(){
        return "signup.html";
    }

    @PostMapping("/register")
    public String register(@RequestParam Map<String, String> params, RedirectAttributes redirectAttributes) {
        try {
            memberService.saveMember(params);
        } catch (IllegalArgumentException e) {
            String errorMessage = e.getMessage();
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/signup";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "회원가입에 실패했습니다. 다시 시도해주세요.");
            return "redirect:/signup";
        }
        return "login.html";
    }

    @GetMapping("/current-user")
    public String getCurrentUser() {
        // SecurityContextHolder를 통해 로그인한 사용자 정보를 가져옵니다.
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            return "현재 로그인한 사용자: " + userDetails.getUsername();
        } else {
            return "로그인된 사용자가 없습니다.";
        }
    }

    @GetMapping("/my-page")
    public String myPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            return "redirect:/login"; // 로그인하지 않은 경우 로그인 페이지로 리디렉트
        }

        CustomUser user = (CustomUser) auth.getPrincipal(); // CustomUser로 캐스팅

        // UserRepository를 사용하여 username을 통해 User 찾기
        List<User> dbUser = userRepository.findByUsername(user.getUsername());

        // User의 id를 기준으로 Item 조회
        List<Item> items = itemRepository.findByUserId(dbUser.get(0).getId());

        // 모델에 사용자 정보 및 아이템 목록 추가
        model.addAttribute("id", user.getId());
        model.addAttribute("userId", user.getUserId()); // userId 전달
        model.addAttribute("username", user.getUsername()); // username 전달
        model.addAttribute("introduction", user.getIntroduction()); // introduction 전달
        model.addAttribute("items", items);  // items 목록 전달

        return "mypage.html"; // 마이페이지 HTML 반환
    }


    @PostMapping("/mypage/update")
    public String updateMyPage(@RequestParam("id") Long id,
                               @RequestParam("userId") String userId,
                               @RequestParam("username") String username,
                               @RequestParam("password") String password,
                               @RequestParam("introduction") String introduction,
                               RedirectAttributes redirectAttributes, Model model) {
        // 현재 로그인된 사용자 확인
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            return "redirect:/login"; // 로그인하지 않은 경우 로그인 페이지로 리디렉트
        }

        CustomUser user = (CustomUser) auth.getPrincipal(); // 현재 로그인된 사용자 정보

        try {
            // 비밀번호가 비어있지 않으면 암호화된 값으로 업데이트
            memberService.updateMember(id, userId, username, password, introduction);

            // 수정된 사용자 정보 다시 조회 (아이디로 사용자 찾기)
            Member updatedMember = memberRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다."));

            // 사용자 정보를 CustomUser로 업데이트
            user.setUserId(updatedMember.getUserId());
            user.setUsername(updatedMember.getUsername());
            user.setIntroduction(updatedMember.getIntroduction());

            // 수정 완료 메시지
            redirectAttributes.addFlashAttribute("message", "수정이 완료되었습니다.");

            // 수정된 정보를 모델에 추가
            model.addAttribute("id", updatedMember.getId());
            model.addAttribute("userId", updatedMember.getUserId());
            model.addAttribute("username", updatedMember.getUsername());
            model.addAttribute("introduction", updatedMember.getIntroduction());

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "/";
    }





}
