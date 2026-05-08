package com.company.crmticketing.controller;

import com.company.crmticketing.dto.response.ApiResponse;
import com.company.crmticketing.dto.role.RoleCreateDto;
import com.company.crmticketing.dto.role.RoleResponseDto;
import com.company.crmticketing.dto.role.RoleUpdateDto;
import com.company.crmticketing.service.RoleService;
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
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
@Tag(name = "Role Management", description = "Role CRUD and permission assignment APIs")
@SecurityRequirement(name = "bearerAuth")
public class RoleController {

    private final RoleService roleService;

    // =========================================================================
    // ============================ CREATE OPERATIONS ==========================
    // =========================================================================

    @Operation(
            summary = "Create Role",
            description = "Creates a new role. Requires 'CREATE_ROLE' permission."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Role created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "Role created successfully",
                                      "data": {
                                        "id": 1,
                                        "name": "ROLE_MANAGER",
                                        "description": "Manager role",
                                        "priority": 5,
                                        "permissions": [],
                                        "userCount": 0
                                      }
                                    }
                                    """)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Role already exists or validation error"
            )
    })
    @PostMapping
    @PreAuthorize("hasPermission('CREATE_ROLE')")
    public ResponseEntity<ApiResponse<RoleResponseDto>> createRole(
            @Parameter(description = "Role creation data", required = true)
            @Valid @RequestBody RoleCreateDto createDto,
            @Parameter(hidden = true) HttpServletRequest request
    ) {
        log.info("✨ Creating new role: {}", createDto.name());
        var role = roleService.createRole(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(
                role,
                "Role created successfully",
                HttpStatus.CREATED.value(),
                request.getRequestURI()
        ));
    }

    // =========================================================================
    // ============================ GET OPERATIONS =============================
    // =========================================================================

    @Operation(
            summary = "Get All Roles",
            description = "Returns a list of all roles. Requires 'READ_ROLE' permission."
    )
    @GetMapping
    @PreAuthorize("hasPermission('READ_ROLE')")
    public ResponseEntity<ApiResponse<Set<RoleResponseDto>>> getAllRoles(
            @Parameter(hidden = true) HttpServletRequest request
    ) {
        log.debug("📋 Getting all roles");
        var roles = roleService.getAllRoles();
        return ResponseEntity.ok(ApiResponse.success(
                roles,
                "Roles retrieved successfully",
                HttpStatus.OK.value(),
                request.getRequestURI()
        ));
    }

    @Operation(
            summary = "Get Role by ID",
            description = "Returns role details for the specified ID. Requires 'READ_ROLE' permission."
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasPermission('READ_ROLE')")
    public ResponseEntity<ApiResponse<RoleResponseDto>> getRoleById(
            @Parameter(description = "Role ID", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(hidden = true) HttpServletRequest request
    ) {
        log.debug("🔍 Getting role by id: {}", id);
        var role = roleService.getRoleById(id);
        return ResponseEntity.ok(ApiResponse.success(
                role,
                "Role retrieved successfully",
                HttpStatus.OK.value(),
                request.getRequestURI()
        ));
    }

    @Operation(
            summary = "Get Role by Name",
            description = "Returns role details for the specified name. Requires 'READ_ROLE' permission."
    )
    @GetMapping("/name/{name}")
    @PreAuthorize("hasPermission('READ_ROLE')")
    public ResponseEntity<ApiResponse<RoleResponseDto>> getRoleByName(
            @Parameter(description = "Role name", required = true, example = "ROLE_ADMIN")
            @PathVariable String name,
            @Parameter(hidden = true) HttpServletRequest request
    ) {
        log.debug("🔍 Getting role by name: {}", name);
        var role = roleService.getRoleByName(name);
        return ResponseEntity.ok(ApiResponse.success(
                role,
                "Role retrieved successfully",
                HttpStatus.OK.value(),
                request.getRequestURI()
        ));
    }

    // =========================================================================
    // ============================ UPDATE OPERATIONS ==========================
    // =========================================================================

    @Operation(
            summary = "Update Role",
            description = "Updates role information. Requires 'UPDATE_ROLE' permission."
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasPermission('UPDATE_ROLE')")
    public ResponseEntity<ApiResponse<RoleResponseDto>> updateRole(
            @Parameter(description = "Role ID", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Role update data", required = true)
            @Valid @RequestBody RoleUpdateDto updateDto,
            @Parameter(hidden = true) HttpServletRequest request
    ) {
        log.info("✏️ Updating role with id: {}", id);
        var updatedRole = roleService.updateRole(id, updateDto);
        return ResponseEntity.ok(ApiResponse.success(
                updatedRole,
                "Role updated successfully",
                HttpStatus.OK.value(),
                request.getRequestURI()
        ));
    }

    // =========================================================================
    // ============================ PERMISSION MANAGEMENT ======================
    // =========================================================================

    @Operation(
            summary = "Assign Permissions to Role",
            description = "Assigns multiple permissions to a role. Requires 'ASSIGN_ROLE' permission."
    )
    @PostMapping("/{id}/permissions")
    @PreAuthorize("hasPermission('ASSIGN_ROLE')")
    public ResponseEntity<ApiResponse<RoleResponseDto>> assignPermissionsToRole(
            @Parameter(description = "Role ID", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "List of permission IDs", required = true, example = "[1, 2, 3]")
            @RequestBody Set<Long> permissionIds,
            @Parameter(hidden = true) HttpServletRequest request
    ) {
        log.info("🔗 Assigning {} permissions to role {}", permissionIds.size(), id);
        var updatedRole = roleService.assignPermissionsToRole(id, permissionIds);
        return ResponseEntity.ok(ApiResponse.success(
                updatedRole,
                "Permissions assigned successfully",
                HttpStatus.OK.value(),
                request.getRequestURI()
        ));
    }

    @Operation(
            summary = "Add Permission to Role",
            description = "Adds a single permission to a role. Requires 'ASSIGN_ROLE' permission."
    )
    @PostMapping("/{id}/permissions/{permissionId}")
    @PreAuthorize("hasPermission('ASSIGN_ROLE')")
    public ResponseEntity<ApiResponse<RoleResponseDto>> addPermissionToRole(
            @Parameter(description = "Role ID", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Permission ID", required = true, example = "1")
            @PathVariable Long permissionId,
            @Parameter(hidden = true) HttpServletRequest request
    ) {
        log.info("🔗 Adding permission {} to role {}", permissionId, id);
        var updatedRole = roleService.addPermissionToRole(id, permissionId);
        return ResponseEntity.ok(ApiResponse.success(
                updatedRole,
                "Permission added successfully",
                HttpStatus.OK.value(),
                request.getRequestURI()
        ));
    }

    @Operation(
            summary = "Remove Permission from Role",
            description = "Removes a permission from a role. Requires 'ASSIGN_ROLE' permission."
    )
    @DeleteMapping("/{id}/permissions/{permissionId}")
    @PreAuthorize("hasPermission('ASSIGN_ROLE')")
    public ResponseEntity<ApiResponse<RoleResponseDto>> removePermissionFromRole(
            @Parameter(description = "Role ID", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Permission ID", required = true, example = "1")
            @PathVariable Long permissionId,
            @Parameter(hidden = true) HttpServletRequest request
    ) {
        log.info("🔗 Removing permission {} from role {}", permissionId, id);
        var updatedRole = roleService.removePermissionFromRole(id, permissionId);
        return ResponseEntity.ok(ApiResponse.success(
                updatedRole,
                "Permission removed successfully",
                HttpStatus.OK.value(),
                request.getRequestURI()
        ));
    }

    // =========================================================================
    // ============================ DELETE OPERATIONS ==========================
    // =========================================================================

    @Operation(
            summary = "Delete Role",
            description = "Deletes a role. Cannot delete roles with assigned users. Requires 'DELETE_ROLE' permission."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Role deleted successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409",
                    description = "Cannot delete role with assigned users"
            )
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission('DELETE_ROLE')")
    public ResponseEntity<ApiResponse<Void>> deleteRole(
            @Parameter(description = "Role ID", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(hidden = true) HttpServletRequest request
    ) {
        log.info("🗑️ Deleting role with id: {}", id);
        roleService.deleteRole(id);
        return ResponseEntity.ok(ApiResponse.success(
                "Role deleted successfully",
                HttpStatus.OK.value(),
                request.getRequestURI()
        ));
    }
}