package com.example.currency.finance.service;

import com.example.currency.finance.dto.LoginRequest;
import com.example.currency. finance.dto.SignupRequest;
import com.example.currency. finance.model.User;

public interface AuthService {
    User authenticateUser(LoginRequest loginRequest) throws Exception;
    User registerUser(SignupRequest signupRequest) throws Exception;
}