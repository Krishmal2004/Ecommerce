package com.example.currency.finance.service;

import com.example.currency.finance.model.User;
import com.example.currency.finance.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        String email = oauth2User.getAttribute("email");
        String fullName = oauth2User.getAttribute("name");

        // --- GitHub Specific Logic ---
        if ("github".equals(provider)) {
            String login = oauth2User.getAttribute("login");

            // If name is missing, use the username
            if (fullName == null) {
                fullName = login;
            }
            // If email is missing (private), generate a placeholder
            if (email == null || email.isEmpty()) {
                email = login + "@github.com";
            }
        }
        // -----------------------------

        // 1. Save or Update User in Database
        saveUserToDatabase(email, fullName);

        // 2. IMPORTANT: Return a NEW OAuth2User with the fixed email
        // This ensures LoginSuccessHandler can find the "email" attribute
        Map<String, Object> attributes = new HashMap<>(oauth2User.getAttributes());
        attributes.put("email", email);
        attributes.put("name", fullName);

        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        return new DefaultOAuth2User(
                oauth2User.getAuthorities(),
                attributes,
                userNameAttributeName
        );
    }

    private void saveUserToDatabase(String email, String fullName) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);
        } else {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setFullName(fullName);
            newUser.setPassword("");
            newUser.setCreatedAt(LocalDateTime.now());
            newUser.setLastLogin(LocalDateTime.now());
            userRepository.save(newUser);
        }
    }
}