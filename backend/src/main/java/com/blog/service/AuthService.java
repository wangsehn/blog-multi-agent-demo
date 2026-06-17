package com.blog.service;

import com.blog.dto.LoginRequest;
import com.blog.dto.LoginResponse;
import com.blog.dto.RegisterRequest;

public interface AuthService {
    LoginResponse login(LoginRequest request);
    LoginResponse register(RegisterRequest request);
    LoginResponse.UserInfo getCurrentUserInfo(Long userId);
}
