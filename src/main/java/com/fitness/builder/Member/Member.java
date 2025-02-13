package com.fitness.builder.Member;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;
    private String userId;
    private String password;
    private String Introduction;
    private String role;

    public Member() {
    }

    public Member(String username, String userId, String encodedPassword, String introduction) {
        this.username = username;
        this.userId = userId;
        this.password = encodedPassword;
        this.Introduction = introduction;
    }


}
