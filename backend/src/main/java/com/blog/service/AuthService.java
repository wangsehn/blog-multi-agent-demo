package com.blog.service;

import com.blog.dto.LoginRequest;
import com.blog.dto.LoginResponse;
import com.blog.dto.RegisterRequest;
import com.blog.entity.User;

public interface AuthService {
    LoginResponse login(LoginRequest request);
    User register(RegisterRequest request);
    User getCurrentUser(Long userId);
    User getUserByUsername(String username);
}
