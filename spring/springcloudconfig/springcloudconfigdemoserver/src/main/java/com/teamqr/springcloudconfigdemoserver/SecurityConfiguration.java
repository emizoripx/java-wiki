package com.teamqr.springcloudconfigdemoserver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.ignoringRequestMatchers(
                "/encrypt/**", "/decrypt/**"
        ));
        http.authorizeRequests((requests) -> requests.anyRequest()
            .authenticated());
        http.formLogin();
        http.httpBasic();
        return http.build();
    }
}
