package com.company.crmticketing.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenRefreshResponse {

    private final String accessToken;
    private final String refreshToken;
    private final long expiresIn;

    @Builder.Default
    private final String tokenType = "Bearer";
}