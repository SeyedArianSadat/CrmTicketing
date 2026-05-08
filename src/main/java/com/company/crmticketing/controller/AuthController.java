package com.company.crmticketing.controller;

import com.company.crmticketing.dto.request.LoginRequest;
import com.company.crmticketing.dto.request.TokenRefreshRequest;
import com.company.crmticketing.dto.response.ApiResponse;
import com.company.crmticketing.dto.response.LoginResponse;
import com.company.crmticketing.dto.response.TokenRefreshResponse;
import com.company.crmticketing.security.provider.JwtTokenProvider;
import com.company.crmticketing.security.service.CustomUserDetailsService;
import com.company.crmticketing.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication and token management APIs")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserService userService;
    private final CustomUserDetailsService customUserDetailsService;  // ✅ اضافه شد

    @Operation(
            summary = "User Login",
            description = "Authenticates a user and returns JWT tokens (access and refresh)"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Login successful",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "Login successful",
                                      "data": {
                                        "accessToken": "eyJhbGciOiJIUzUxMiJ9...",
                                        "refreshToken": "eyJhbGciOiJIUzUxMiJ9...",
                                        "tokenType": "Bearer",
                                        "expiresIn": 3600000,
                                        "user": {
                                          "id": 1,
                                          "username": "admin",
                                          "email": "admin@example.com",
                                          "roles": ["ROLE_ADMIN", "ROLE_USER"]
                                        }
                                      },
                                      "timestamp": "2026-04-22T10:30:00",
                                      "statusCode": 200,
                                      "path": "/api/v1/auth/login"
                                    }
                                    """)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Invalid credentials",
                    content = @Content
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Validation error",
                    content = @Content
            )
    })
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Parameter(description = "Login credentials", required = true)
            @Valid @RequestBody LoginRequest request,
            @Parameter(hidden = true) HttpServletRequest httpRequest
    ) {
        log.info("🔐 Login attempt for user: {}", request.getUsername());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = tokenProvider.generateAccessToken(authentication);
        String refreshToken = tokenProvider.generateRefreshToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        var user = userService.getUserByUsername(userDetails.getUsername());

        LoginResponse.UserInfo userInfo = LoginResponse.UserInfo.builder()
                .id(user.id())
                .username(user.username())
                .email(user.email())
                .roles(user.roles())
                .build();

        LoginResponse response = LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(3600000L)
                .user(userInfo)
                .build();

        log.info("✅ User logged in successfully: {}", request.getUsername());

        return ResponseEntity.ok(ApiResponse.success(
                response,
                "Login successful",
                HttpStatus.OK.value(),
                httpRequest.getRequestURI()
        ));
    }

    @Operation(
            summary = "Refresh Token",
            description = "Generates new access token using a valid refresh token"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Token refreshed successfully",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "Token refreshed successfully",
                                      "data": {
                                        "accessToken": "eyJhbGciOiJIUzUxMiJ9...",
                                        "refreshToken": "eyJhbGciOiJIUzUxMiJ9...",
                                        "tokenType": "Bearer",
                                        "expiresIn": 3600000
                                      },
                                      "timestamp": "2026-04-22T10:30:00",
                                      "statusCode": 200
                                    }
                                    """)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Invalid or expired refresh token",
                    content = @Content
            )
    })
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenRefreshResponse>> refreshToken(
            @Parameter(description = "Refresh token request", required = true)
            @Valid @RequestBody TokenRefreshRequest request,
            @Parameter(hidden = true) HttpServletRequest httpRequest
    ) {
        log.info("🔄 Token refresh attempt");

        String refreshToken = request.getRefreshToken();

        if (!tokenProvider.validateToken(refreshToken)) {
            log.error("❌ Invalid refresh token");
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(
                            "Invalid refresh token",
                            HttpStatus.UNAUTHORIZED.value(),
                            httpRequest.getRequestURI()
                    ));
        }

        String username = tokenProvider.getUsernameFromToken(refreshToken);

        // ✅ استفاده از CustomUserDetailsService
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                username,
                null,
                userDetails.getAuthorities()
        );

        String newAccessToken = tokenProvider.generateAccessToken(authentication);
        String newRefreshToken = tokenProvider.generateRefreshToken(authentication);

        TokenRefreshResponse response = TokenRefreshResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .expiresIn(3600000L)
                .build();

        log.info("✅ Token refreshed successfully for user: {}", username);

        return ResponseEntity.ok(ApiResponse.success(
                response,
                "Token refreshed successfully",
                HttpStatus.OK.value(),
                httpRequest.getRequestURI()
        ));
    }

    @Operation(
            summary = "User Logout",
            description = "Logs out the current user and clears security context"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Logout successful",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "Logout successful",
                                      "timestamp": "2026-04-22T10:30:00",
                                      "statusCode": 200
                                    }
                                    """)
                    )
            )
    })
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@Parameter(hidden = true) HttpServletRequest httpRequest) {
        SecurityContextHolder.clearContext();
        log.info("👋 User logged out");

        return ResponseEntity.ok(ApiResponse.success(
                "Logout successful",
                HttpStatus.OK.value(),
                httpRequest.getRequestURI()
        ));
    }
}