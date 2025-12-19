package com.example.currency.finance.service;

import com.example.currency.finance.dto.LoginRequest;
import com.example.currency. finance.dto.SignupRequest;
import com.example.currency. finance.model.User;
import com.example.currency.finance. repository.UserRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    // Constructor injection - no @Autowired needed
    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User authenticateUser(LoginRequest loginRequest) throws Exception {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new Exception("User not found"));

        // NOTE: In production, use BCryptPasswordEncoder here!
        // if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword()))
        if (!user.getPassword().equals(loginRequest.getPassword())) {
            throw new Exception("Invalid credentials");
        }

        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
        return user;
    }

    @Override
    public User registerUser(SignupRequest signupRequest) throws Exception {
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new Exception("Email is already in use");
        }

        User user = new User();
        user.setFullName(signupRequest.getFullname());
        user.setEmail(signupRequest.getEmail());
        // NOTE: In production, encode this password!
        user.setPassword(signupRequest.getPassword());

        return userRepository.save(user);
    }
}