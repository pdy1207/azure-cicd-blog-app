package com.fitness.builder.Member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

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



}
