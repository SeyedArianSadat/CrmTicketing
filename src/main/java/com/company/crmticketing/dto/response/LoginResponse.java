package com.company.crmticketing.dto.response;

import lombok.Builder;
import lombok.Getter;
import java.util.Set;

@Getter
@Builder
public class LoginResponse {

    private final String accessToken;
    private final String refreshToken;
    private final long expiresIn;
    private final UserInfo user;

    @Builder.Default
    private final String tokenType = "Bearer";

    @Getter
    @Builder
    public static class UserInfo {
        private final Long id;
        private final String username;
        private final String email;
        private final Set<String> roles;
    }
}