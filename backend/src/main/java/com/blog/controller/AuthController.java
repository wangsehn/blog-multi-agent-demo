package com.blog.controller;

import com.blog.dto.LoginRequest;
import com.blog.dto.LoginResponse;
import com.blog.dto.RegisterRequest;
import com.blog.dto.Result;
import com.blog.entity.User;
import com.blog.mapper.UserMapper;
import com.blog.service.AuthService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UserMapper userMapper;

    public AuthController(AuthService authService, UserMapper userMapper) {
        this.authService = authService;
        this.userMapper = userMapper;
    }

    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody LoginRequest request) {
        try {
            LoginResponse response = authService.login(request);
            return Result.success(response);
        } catch (RuntimeException e) {
            return Result.error(401, e.getMessage());
        }
    }

    @PostMapping("/register")
    public Result<LoginResponse> register(@RequestBody RegisterRequest request) {
        try {
            LoginResponse response = authService.register(request);
            return Result.success(response);
        } catch (RuntimeException e) {
            return Result.error(400, e.getMessage());
        }
    }

    @GetMapping("/me")
    public Result<LoginResponse.UserInfo> getCurrentUser() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            User user = userMapper.selectOne(
                    new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<User>()
                            .eq(User::getUsername, userDetails.getUsername()));
            if (user == null) {
                return Result.error(404, "User not found");
            }
            LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo();
            userInfo.setId(user.getId());
            userInfo.setUsername(user.getUsername());
            userInfo.setNickname(user.getNickname());
            userInfo.setRole(user.getRole());
            return Result.success(userInfo);
        } catch (Exception e) {
            return Result.error(500, e.getMessage());
        }
    }
}
