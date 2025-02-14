package com.fitness.builder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        CSRF 공격 제어 off
        http.csrf((csrf) -> csrf.disable());
        http.authorizeHttpRequests((authorize) ->
                authorize
                        .requestMatchers("/login").not().authenticated()  // 로그인된 사용자는 로그인 페이지 접근 불가
                        .requestMatchers("/write").authenticated()  // 로그인한 사용자만 접근 가능
                        .requestMatchers("/my-page").authenticated()
                        .requestMatchers("/**").permitAll() // 나머지 URL은 모두 허용
        );

        http.formLogin((formLogin) -> formLogin.loginPage("/login")
                        .defaultSuccessUrl("/")
                        .failureUrl("/login?error=true")
                //실패시 보내는 url
        );

        http.logout(logout -> logout.logoutUrl("/logout"));

        return http.build();
    }
}