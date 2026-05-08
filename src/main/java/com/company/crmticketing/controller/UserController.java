package com.company.crmticketing.controller;

import com.company.crmticketing.dto.response.ApiResponse;
import com.company.crmticketing.dto.user.ChangePasswordDto;
import com.company.crmticketing.dto.user.UserResponseDto;
import com.company.crmticketing.dto.user.UserUpdateDto;
import com.company.crmticketing.security.model.SecurityUser;
import com.company.crmticketing.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "User CRUD and management APIs")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    // =========================================================================
    // ============================ GET OPERATIONS =============================
    // =========================================================================

    @Operation(
            summary = "Get Current User",
            description = "Returns the profile of the currently authenticated user"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "User retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(value = """
                                {
                                  "success": true,
                                  "message": "Current user retrieved successfully",
                                  "data": {
                                    "id": 1,
                                    "username": "admin",
                                    "email": "admin@example.com",
                                    "firstName": "System",
                                    "lastName": "Administrator",
                                    "fullName": "System Administrator",
                                    "enabled": true,
                                    "emailVerified": true,
                                    "roles": ["ROLE_ADMIN", "ROLE_USER"]
                                  },
                                  "timestamp": "2026-04-22T10:30:00",
                                  "statusCode": 200
                                }
                                """)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - User not authenticated or token invalid",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                {
                                  "success": false,
                                  "message": "Unauthorized",
                                  "timestamp": "2026-04-22T10:30:00",
                                  "statusCode": 401
                                }
                                """)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                {
                                  "success": false,
                                  "message": "User not found with id: 999",
                                  "timestamp": "2026-04-22T10:30:00",
                                  "statusCode": 404
                                }
                                """)
                    )
            )
    })
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponseDto>> getCurrentUser(
            @Parameter(hidden = true) @AuthenticationPrincipal SecurityUser currentUser,
            @Parameter(hidden = true) HttpServletRequest request
    ) {
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Unauthorized", HttpStatus.UNAUTHORIZED.value(), request.getRequestURI()));
        }

        log.debug("🔍 Getting current user info");
        var user = userService.getUserById(currentUser.getId());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("User not found", HttpStatus.NOT_FOUND.value(), request.getRequestURI()));
        }

        return ResponseEntity.ok(ApiResponse.success(
                user,
                "Current user retrieved successfully",
                HttpStatus.OK.value(),
                request.getRequestURI()
        ));
    }

    @Operation(
            summary = "Get User by ID",
            description = "Returns user details for the specified ID. Requires ADMIN role or ownership."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "User retrieved successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Access denied"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "User not found"
            )
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or @securityUserService.isCurrentUser(#id, authentication)")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUserById(
            @Parameter(description = "User ID", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(hidden = true) HttpServletRequest request
    ) {
        log.debug("🔍 Getting user by id: {}", id);
        var user = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success(
                user,
                "User retrieved successfully",
                HttpStatus.OK.value(),
                request.getRequestURI()
        ));
    }

    @Operation(
            summary = "Get User by Username",
            description = "Returns user details for the specified username. Requires ADMIN role."
    )
    @GetMapping("/username/{username}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUserByUsername(
            @Parameter(description = "Username", required = true, example = "admin")
            @PathVariable String username,
            @Parameter(hidden = true) HttpServletRequest request
    ) {
        log.debug("🔍 Getting user by username: {}", username);
        var user = userService.getUserByUsername(username);
        return ResponseEntity.ok(ApiResponse.success(
                user,
                "User retrieved successfully",
                HttpStatus.OK.value(),
                request.getRequestURI()
        ));
    }

    @Operation(
            summary = "Get All Users",
            description = "Returns a list of all active users. Requires ADMIN role."
    )
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<Set<UserResponseDto>>> getAllUsers(
            @Parameter(hidden = true) HttpServletRequest request
    ) {
        log.debug("📋 Getting all users");
        var users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success(
                users,
                "Users retrieved successfully",
                HttpStatus.OK.value(),
                request.getRequestURI()
        ));
    }

    // =========================================================================
    // ============================ UPDATE OPERATIONS ==========================
    // =========================================================================

    @Operation(
            summary = "Update User",
            description = "Updates user information. Requires ADMIN role or ownership."
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or @securityUserService.isCurrentUser(#id, authentication)")
    public ResponseEntity<ApiResponse<UserResponseDto>> updateUser(
            @Parameter(description = "User ID", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "User update data", required = true)
            @Valid @RequestBody UserUpdateDto updateDto,
            @Parameter(hidden = true) HttpServletRequest request
    ) {
        log.info("✏️ Updating user with id: {}", id);
        var updatedUser = userService.updateUser(id, updateDto);
        return ResponseEntity.ok(ApiResponse.success(
                updatedUser,
                "User updated successfully",
                HttpStatus.OK.value(),
                request.getRequestURI()
        ));
    }

    @Operation(
            summary = "Change Password",
            description = "Changes user password. Requires ADMIN role or ownership."
    )
    @PostMapping("/{id}/change-password")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or @securityUserService.isCurrentUser(#id, authentication)")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @Parameter(description = "User ID", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Password change data", required = true)
            @Valid @RequestBody ChangePasswordDto passwordDto,
            @Parameter(hidden = true) HttpServletRequest request
    ) {
        log.info("🔐 Changing password for user id: {}", id);
        userService.changePassword(id, passwordDto);
        return ResponseEntity.ok(ApiResponse.success(
                "Password changed successfully",
                HttpStatus.OK.value(),
                request.getRequestURI()
        ));
    }

    // =========================================================================
    // ============================ ROLE MANAGEMENT ============================
    // =========================================================================

    @Operation(
            summary = "Add Role to User",
            description = "Assigns a role to a user. Requires ADMIN role."
    )
    @PostMapping("/{id}/roles/{roleId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<UserResponseDto>> addRoleToUser(
            @Parameter(description = "User ID", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Role ID", required = true, example = "1")
            @PathVariable Long roleId,
            @Parameter(hidden = true) HttpServletRequest request
    ) {
        log.info("🔗 Adding role {} to user {}", roleId, id);
        var updatedUser = userService.addRoleToUser(id, roleId);
        return ResponseEntity.ok(ApiResponse.success(
                updatedUser,
                "Role added successfully",
                HttpStatus.OK.value(),
                request.getRequestURI()
        ));
    }

    @Operation(
            summary = "Remove Role from User",
            description = "Removes a role from a user. Requires ADMIN role."
    )
    @DeleteMapping("/{id}/roles/{roleId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<UserResponseDto>> removeRoleFromUser(
            @Parameter(description = "User ID", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Role ID", required = true, example = "1")
            @PathVariable Long roleId,
            @Parameter(hidden = true) HttpServletRequest request
    ) {
        log.info("🔗 Removing role {} from user {}", roleId, id);
        var updatedUser = userService.removeRoleFromUser(id, roleId);
        return ResponseEntity.ok(ApiResponse.success(
                updatedUser,
                "Role removed successfully",
                HttpStatus.OK.value(),
                request.getRequestURI()
        ));
    }

    // =========================================================================
    // ============================ ACCOUNT MANAGEMENT =========================
    // =========================================================================

    @Operation(
            summary = "Enable User",
            description = "Enables a disabled user account. Requires ADMIN role."
    )
    @PostMapping("/{id}/enable")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<UserResponseDto>> enableUser(
            @Parameter(description = "User ID", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(hidden = true) HttpServletRequest request
    ) {
        log.info("🔓 Enabling user with id: {}", id);
        var updatedUser = userService.enableUser(id);
        return ResponseEntity.ok(ApiResponse.success(
                updatedUser,
                "User enabled successfully",
                HttpStatus.OK.value(),
                request.getRequestURI()
        ));
    }

    @Operation(
            summary = "Disable User",
            description = "Disables a user account. Requires ADMIN role."
    )
    @PostMapping("/{id}/disable")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<UserResponseDto>> disableUser(
            @Parameter(description = "User ID", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(hidden = true) HttpServletRequest request
    ) {
        log.info("🔒 Disabling user with id: {}", id);
        var updatedUser = userService.disableUser(id);
        return ResponseEntity.ok(ApiResponse.success(
                updatedUser,
                "User disabled successfully",
                HttpStatus.OK.value(),
                request.getRequestURI()
        ));
    }

    @Operation(
            summary = "Unlock User",
            description = "Unlocks a locked user account. Requires ADMIN role."
    )
    @PostMapping("/{id}/unlock")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<UserResponseDto>> unlockUser(
            @Parameter(description = "User ID", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(hidden = true) HttpServletRequest request
    ) {
        log.info("🔓 Unlocking user with id: {}", id);
        var updatedUser = userService.unlockUser(id);
        return ResponseEntity.ok(ApiResponse.success(
                updatedUser,
                "User unlocked successfully",
                HttpStatus.OK.value(),
                request.getRequestURI()
        ));
    }

    @Operation(
            summary = "Verify Email",
            description = "Marks user email as verified. Requires ADMIN role or ownership."
    )
    @PostMapping("/{id}/verify-email")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or @securityUserService.isCurrentUser(#id, authentication)")
    public ResponseEntity<ApiResponse<UserResponseDto>> verifyEmail(
            @Parameter(description = "User ID", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(hidden = true) HttpServletRequest request
    ) {
        log.info("✅ Verifying email for user id: {}", id);
        var updatedUser = userService.verifyEmail(id);
        return ResponseEntity.ok(ApiResponse.success(
                updatedUser,
                "Email verified successfully",
                HttpStatus.OK.value(),
                request.getRequestURI()
        ));
    }

    // =========================================================================
    // ============================ DELETE OPERATIONS ==========================
    // =========================================================================

    @Operation(
            summary = "Delete User",
            description = "Soft deletes a user account. Requires ADMIN role."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "User deleted successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409",
                    description = "Cannot delete main admin user"
            )
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @Parameter(description = "User ID", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(hidden = true) HttpServletRequest request
    ) {
        log.info("🗑️ Deleting user with id: {}", id);
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success(
                "User deleted successfully",
                HttpStatus.OK.value(),
                request.getRequestURI()
        ));
    }
}