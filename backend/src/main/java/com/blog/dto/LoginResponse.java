package com.blog.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private String tokenType;
    private UserInfo user;

    @Data
    public static class UserInfo {
        private Long id;
        private String username;
        private String nickname;
        private Integer role;
    }
}
