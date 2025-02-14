package com.fitness.builder.Member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); // 비밀번호 암호화 인스턴스

    public void saveMember(Map<String, String> params) {
        if (params == null || !params.containsKey("username") || !params.containsKey("userId") || !params.containsKey("password")) {
            throw new IllegalArgumentException("필수 값이 누락되었습니다.");
        }

        // 1. 닉네임 검증
        String username = params.get("username");
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("닉네임이 비어있습니다.");
        }
        if (memberRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("이미 존재하는 닉네임 입니다.");
        }

        // 2. 아이디 검증: 영문 또는 숫자 4~10자, 한글 포함 시 2~10자
        String userId = params.get("userId");
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("아이디 값이 비어있습니다.");
        }
        if (!isValidUserId(userId)) {
            throw new IllegalArgumentException("아이디는 영문 또는 숫자 4~10자, 한글 포함 시 2~10자로 입력해주세요.");
        }

        // 3. 비밀번호 검증: 특수문자 포함 10~15자
        String rawPassword = params.get("password");
        if (rawPassword == null || rawPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("비밀번호 값이 비어있습니다.");
        }
        if (!isValidPassword(rawPassword)) {
            throw new IllegalArgumentException("비밀번호는 특수문자 1자 이상을 포함한 10~15 글자여야 합니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // 자기소개
        String introduction = params.get("introduction");

        // 저장
        memberRepository.save(new Member(username, userId, encodedPassword, introduction));
    }

    // 아이디 유효성 검사 (영문, 숫자 4~10자 / 한글 포함 시 2~10자)
    private boolean isValidUserId(String userId) {
        // 영문, 숫자 4~10자 또는 한글 포함 2~10자
        String regex = "^[a-zA-Z0-9]{4,10}$|^[가-힣a-zA-Z0-9]{2,10}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(userId);
        return matcher.matches();
    }

    // 비밀번호 유효성 검사 (특수문자 포함 10~15자)
    private boolean isValidPassword(String password) {
        String regex = "^(?=.*[!@#$%^&*])[A-Za-z0-9!@#$%^&*]{10,15}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public void updateMember(Long id, String userId, String username, String password, String introduction) {
        Optional<Member> optionalUser = memberRepository.findById(id);

        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("사용자 정보를 찾을 수 없습니다.");
        }

        Member existingUser = optionalUser.get();

        // 값 검증 후 업데이트
        if (userId != null && !userId.trim().isEmpty()) {
            if (!isValidUserId(userId)) {
                throw new IllegalArgumentException("아이디는 영문 또는 숫자 4~10자, 한글 포함 시 2~10자로 입력해주세요.");
            }
            existingUser.setUserId(userId);
        }

        if (username != null && !username.trim().isEmpty()) {
            if (memberRepository.existsByUsername(username)) {
                throw new IllegalArgumentException("이미 존재하는 닉네임 입니다.");
            }
            existingUser.setUsername(username);
        }

        if (introduction != null) {
            existingUser.setIntroduction(introduction);
        }

        // 비밀번호 암호화 후 업데이트
        if (password != null && !password.trim().isEmpty()) {
            if (!isValidPassword(password)) {
                throw new IllegalArgumentException("비밀번호는 특수문자 1자 이상을 포함한 10~15 글자여야 합니다.");
            }
            existingUser.setPassword(passwordEncoder.encode(password));
        }

        memberRepository.save(existingUser);
    }
}
