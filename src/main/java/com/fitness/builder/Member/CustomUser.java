package com.fitness.builder.Member;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@Getter
@Setter
public class CustomUser extends User {
    private Long id;
    private String userId;
    private String username;
    private String introduction;

    public CustomUser(Long id, String userId, String password, String username, String introduction, List<GrantedAuthority> authorities) {
        super(userId, password, authorities);
        this.userId = userId;
        this.id = id;
        this.username = username;
        this.introduction = introduction;
    }


}
