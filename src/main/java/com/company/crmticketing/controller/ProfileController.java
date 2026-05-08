package com.company.crmticketing.controller;

import com.company.crmticketing.dto.profile.UserProfileDto;
import com.company.crmticketing.dto.response.ApiResponse;
import com.company.crmticketing.security.model.SecurityUser;
import com.company.crmticketing.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
@Tag(name = "User Profile", description = "User profile and statistics APIs")
@SecurityRequirement(name = "bearerAuth")
public class ProfileController {

    private final ProfileService profileService;

    // =========================================================================
    // ============================ PROFILE OPERATIONS =========================
    // =========================================================================

    @Operation(
            summary = "Get Current User Profile",
            description = "Returns complete profile of the currently authenticated user including roles, permissions, and statistics"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Profile retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "Profile retrieved successfully",
                                      "data": {
                                        "id": 1,
                                        "username": "admin",
                                        "email": "admin@example.com",
                                        "firstName": "System",
                                        "lastName": "Administrator",
                                        "fullName": "System Administrator",
                                        "enabled": true,
                                        "accountNonLocked": true,
                                        "emailVerified": true,
                                        "roles": [
                                          {
                                            "id": 1,
                                            "name": "ROLE_ADMIN",
                                            "description": "Administrator role",
                                            "priority": 1,
                                            "permissions": ["READ_USER", "WRITE_USER"]
                                          }
                                        ],
                                        "allPermissions": ["READ_USER", "WRITE_USER"],
                                        "statistics": {
                                          "totalRoles": 1,
                                          "totalPermissions": 2,
                                          "isPasswordExpired": false,
                                          "daysSinceLastLogin": 0,
                                          "failedAttempts": 0
                                        }
                                      },
                                      "timestamp": "2026-04-22T10:30:00",
                                      "statusCode": 200
                                    }
                                    """)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content
            )
    })
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileDto>> getMyProfile(
            @Parameter(hidden = true) @AuthenticationPrincipal SecurityUser currentUser,
            @Parameter(hidden = true) HttpServletRequest request
    ) {
        log.debug("👤 Getting profile for current user: {}", currentUser.getUsername());
        var profile = profileService.getUserProfile(currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success(
                profile,
                "Profile retrieved successfully",
                HttpStatus.OK.value(),
                request.getRequestURI()
        ));
    }
}