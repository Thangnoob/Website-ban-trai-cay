package com.example.user_service.config;

import com.example.user_service.entity.User;
import com.example.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
public class UserDetailsConfig {

    @Autowired
    private UserService userService;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            User user = userService.findUserByEmail(username);
            if (user == null) {
                throw new UsernameNotFoundException("Người dùng không tồn tại: " + username);
            }
            return org.springframework.security.core.userdetails.User
                    .withUsername(user.getEmail()) // Đảm bảo username là email
                    .password(user.getPassword())
                    .roles("USER")
                    .build();
        };
    }
}