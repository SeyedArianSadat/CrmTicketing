package com.company.crmticketing.controller;

import com.company.crmticketing.dto.permission.PermissionCreateDto;
import com.company.crmticketing.dto.permission.PermissionResponseDto;
import com.company.crmticketing.dto.permission.PermissionUpdateDto;
import com.company.crmticketing.dto.response.ApiResponse;
import com.company.crmticketing.service.PermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/api/v1/permissions")
@RequiredArgsConstructor
@Tag(name = "Permission Management", description = "Permission CRUD and management APIs")
@SecurityRequirement(name = "bearerAuth")
public class PermissionController {

    private final PermissionService permissionService;

    // =========================================================================
    // ============================ CREATE OPERATIONS ==========================
    // =========================================================================

    @Operation(
            summary = "Create Permission",
            description = "Creates a new permission. Requires 'MANAGE_PERMISSIONS' permission."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Permission created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "Permission created successfully",
                                      "data": {
                                        "id": 1,
                                        "name": "user:read",
                                        "description": "Read user details",
                                        "category": "USER",
                                        "systemDefault": false,
                                        "roleCount": 0
                                      }
                                    }
                                    """)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Permission already exists or validation error"
            )
    })
    @PostMapping
    @PreAuthorize("hasPermission('MANAGE_PERMISSIONS')")
    public ResponseEntity<ApiResponse<PermissionResponseDto>> createPermission(
            @Parameter(description = "Permission creation data", required = true)
            @Valid @RequestBody PermissionCreateDto createDto,
            @Parameter(hidden = true) HttpServletRequest request
    ) {
        log.info("✨ Creating new permission: {}", createDto.name());
        var permission = permissionService.createPermission(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(
                permission,
                "Permission created successfully",
                HttpStatus.CREATED.value(),
                request.getRequestURI()
        ));
    }

    // =========================================================================
    // ============================ GET OPERATIONS =============================
    // =========================================================================

    @Operation(
            summary = "Get All Permissions",
            description = "Returns a list of all permissions. Requires 'READ_PERMISSION' permission."
    )
    @GetMapping
    @PreAuthorize("hasPermission('READ_PERMISSION')")
    public ResponseEntity<ApiResponse<Set<PermissionResponseDto>>> getAllPermissions(
            @Parameter(hidden = true) HttpServletRequest request
    ) {
        log.debug("📋 Getting all permissions");
        var permissions = permissionService.getAllPermissions();
        return ResponseEntity.ok(ApiResponse.success(
                permissions,
                "Permissions retrieved successfully",
                HttpStatus.OK.value(),
                request.getRequestURI()
        ));
    }

    @Operation(
            summary = "Get Permission by ID",
            description = "Returns permission details for the specified ID. Requires 'READ_PERMISSION' permission."
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasPermission('READ_PERMISSION')")
    public ResponseEntity<ApiResponse<PermissionResponseDto>> getPermissionById(
            @Parameter(description = "Permission ID", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(hidden = true) HttpServletRequest request
    ) {
        log.debug("🔍 Getting permission by id: {}", id);
        var permission = permissionService.getPermissionById(id);
        return ResponseEntity.ok(ApiResponse.success(
                permission,
                "Permission retrieved successfully",
                HttpStatus.OK.value(),
                request.getRequestURI()
        ));
    }

    @Operation(
            summary = "Get Permission by Name",
            description = "Returns permission details for the specified name. Requires 'READ_PERMISSION' permission."
    )
    @GetMapping("/name/{name}")
    @PreAuthorize("hasPermission('READ_PERMISSION')")
    public ResponseEntity<ApiResponse<PermissionResponseDto>> getPermissionByName(
            @Parameter(description = "Permission name", required = true, example = "READ_USER")
            @PathVariable String name,
            @Parameter(hidden = true) HttpServletRequest request
    ) {
        log.debug("🔍 Getting permission by name: {}", name);
        var permission = permissionService.getPermissionByName(name);
        return ResponseEntity.ok(ApiResponse.success(
                permission,
                "Permission retrieved successfully",
                HttpStatus.OK.value(),
                request.getRequestURI()
        ));
    }

    // =========================================================================
    // ============================ UPDATE OPERATIONS ==========================
    // =========================================================================

    @Operation(
            summary = "Update Permission",
            description = "Updates permission information. Requires 'MANAGE_PERMISSIONS' permission."
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasPermission('MANAGE_PERMISSIONS')")
    public ResponseEntity<ApiResponse<PermissionResponseDto>> updatePermission(
            @Parameter(description = "Permission ID", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Permission update data", required = true)
            @Valid @RequestBody PermissionUpdateDto updateDto,
            @Parameter(hidden = true) HttpServletRequest request
    ) {
        log.info("✏️ Updating permission with id: {}", id);
        var updatedPermission = permissionService.updatePermission(id, updateDto);
        return ResponseEntity.ok(ApiResponse.success(
                updatedPermission,
                "Permission updated successfully",
                HttpStatus.OK.value(),
                request.getRequestURI()
        ));
    }

    // =========================================================================
    // ============================ DELETE OPERATIONS ==========================
    // =========================================================================

    @Operation(
            summary = "Delete Permission",
            description = "Deletes a permission. Cannot delete system default permissions. Requires 'MANAGE_PERMISSIONS' permission."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Permission deleted successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409",
                    description = "Cannot delete system default permission"
            )
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission('MANAGE_PERMISSIONS')")
    public ResponseEntity<ApiResponse<Void>> deletePermission(
            @Parameter(description = "Permission ID", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(hidden = true) HttpServletRequest request
    ) {
        log.info("🗑️ Deleting permission with id: {}", id);
        permissionService.deletePermission(id);
        return ResponseEntity.ok(ApiResponse.success(
                "Permission deleted successfully",
                HttpStatus.OK.value(),
                request.getRequestURI()
        ));
    }
}