package com.example.currency.config;

import com.example.currency.finance.service.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final com.example.currency.finance.config.LoginSuccessHandler loginSuccessHandler;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService,
                          com.example.currency.finance.config.LoginSuccessHandler loginSuccessHandler) {
        this.customOAuth2UserService = customOAuth2UserService;
        this.loginSuccessHandler = loginSuccessHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Public pages
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/signup", "/css/**", "/js/**", "/images/**").permitAll()
                        .anyRequest().authenticated()
                )
                // Manual Form Login
                .formLogin(form -> form
                        .loginPage("/login")
                        // --- THE FIX IS HERE ---
                        // We change the default processing URL to "/perform_login"
                        // This stops Spring Security from intercepting "POST /login"
                        // allowing your AuthController to handle it instead.
                        .loginProcessingUrl("/perform_login")
                        .permitAll()
                )
                // Google/GitHub OAuth2 Login
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                        .successHandler(loginSuccessHandler)
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .permitAll()
                )
                .csrf(csrf -> csrf.disable());

        return http.build();
    }
}