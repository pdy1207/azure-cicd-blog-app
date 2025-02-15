package com.fitness.builder.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // userId로 사용자 조회
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);  // 사용자 없으면 null 반환
    }
}
