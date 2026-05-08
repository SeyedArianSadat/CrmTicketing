package com.company.crmticketing.runner;

import com.company.crmticketing.dto.permission.PermissionCreateDto;
import com.company.crmticketing.dto.permission.PermissionResponseDto;
import com.company.crmticketing.dto.profile.UserProfileDto;
import com.company.crmticketing.dto.role.RoleCreateDto;
import com.company.crmticketing.dto.user.UserRegistrationDto;
import com.company.crmticketing.service.PermissionService;
import com.company.crmticketing.service.ProfileService;
import com.company.crmticketing.service.RoleService;
import com.company.crmticketing.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
@Order(2)
public class DataInitializer implements CommandLineRunner {

    private final PermissionService permissionService;
    private final RoleService roleService;
    private final UserService userService;
    private final ProfileService profileService;

    @Override
    @Transactional
    public void run(String... args) {
        if (permissionService.count() == 0) {
            log.info("🚀 Starting data initialization...");
            initPermissions();
            initRoles();
            initAdminUser();
            log.info("✅ Initial data loaded successfully");

            showAdminProfile();
        }
    }

    private void initPermissions() {
        log.info("📋 Creating permissions...");

        permissionService.createPermission(new PermissionCreateDto("CREATE_USER", "Create new users", "USER"));
        permissionService.createPermission(new PermissionCreateDto("READ_USER", "Read user details", "USER"));
        permissionService.createPermission(new PermissionCreateDto("UPDATE_USER", "Update user details", "USER"));
        permissionService.createPermission(new PermissionCreateDto("DELETE_USER", "Delete users", "USER"));

        permissionService.createPermission(new PermissionCreateDto("CREATE_ROLE", "Create new roles", "ROLE"));
        permissionService.createPermission(new PermissionCreateDto("READ_ROLE", "Read role details", "ROLE"));
        permissionService.createPermission(new PermissionCreateDto("UPDATE_ROLE", "Update role details", "ROLE"));
        permissionService.createPermission(new PermissionCreateDto("DELETE_ROLE", "Delete roles", "ROLE"));
        permissionService.createPermission(new PermissionCreateDto("ASSIGN_ROLE", "Assign roles to users", "ROLE"));

        permissionService.createPermission(new PermissionCreateDto("VIEW_AUDIT_LOG", "View audit logs", "ADMIN"));
        permissionService.createPermission(new PermissionCreateDto("SYSTEM_CONFIG", "Configure system settings", "ADMIN"));
        permissionService.createPermission(new PermissionCreateDto("VIEW_PROFILE", "View user profiles", "ADMIN"));
        permissionService.createPermission(new PermissionCreateDto("MANAGE_PERMISSIONS", "Manage permissions", "ADMIN"));

        log.info("✅ All permissions created successfully");
    }

    private void initRoles() {
        log.info("👥 Creating roles...");

        roleService.createRole(new RoleCreateDto("ROLE_SUPER_ADMIN", "Super Administrator - Full system access", 1));
        roleService.createRole(new RoleCreateDto("ROLE_ADMIN", "Administrator - Manage users and roles", 2));
        roleService.createRole(new RoleCreateDto("ROLE_USER", "Regular user - Basic access", 10));
        roleService.createRole(new RoleCreateDto("ROLE_GUEST", "Guest user - Limited access", 20));

        assignPermissionsToRoles();

        log.info("✅ All roles created successfully");
    }

    private void assignPermissionsToRoles() {
        log.info("🔗 Assigning permissions to roles...");

        var createUser = permissionService.getPermissionByName("CREATE_USER");
        var readUser = permissionService.getPermissionByName("READ_USER");
        var updateUser = permissionService.getPermissionByName("UPDATE_USER");
        var deleteUser = permissionService.getPermissionByName("DELETE_USER");
        var createRole = permissionService.getPermissionByName("CREATE_ROLE");
        var readRole = permissionService.getPermissionByName("READ_ROLE");
        var updateRole = permissionService.getPermissionByName("UPDATE_ROLE");
        var deleteRole = permissionService.getPermissionByName("DELETE_ROLE");
        var assignRole = permissionService.getPermissionByName("ASSIGN_ROLE");
        var viewProfile = permissionService.getPermissionByName("VIEW_PROFILE");

        // ROLE_SUPER_ADMIN - Full access to all permissions
        var superAdmin = roleService.getRoleByName("ROLE_SUPER_ADMIN");
        var allPermissionIds = permissionService.getAllPermissions().stream()
                .map(PermissionResponseDto::id)
                .collect(Collectors.toSet());
        // ✅ استفاده از HashSet
        roleService.assignPermissionsToRole(superAdmin.id(), new HashSet<>(allPermissionIds));

        // ROLE_ADMIN
        var admin = roleService.getRoleByName("ROLE_ADMIN");
        // ✅ استفاده از HashSet به جای Set.of مستقیم
        Set<Long> adminPermissionIds = new HashSet<>();
        adminPermissionIds.add(createUser.id());
        adminPermissionIds.add(readUser.id());
        adminPermissionIds.add(updateUser.id());
        adminPermissionIds.add(deleteUser.id());
        adminPermissionIds.add(createRole.id());
        adminPermissionIds.add(readRole.id());
        adminPermissionIds.add(updateRole.id());
        adminPermissionIds.add(deleteRole.id());
        adminPermissionIds.add(assignRole.id());
        adminPermissionIds.add(viewProfile.id());
        roleService.assignPermissionsToRole(admin.id(), adminPermissionIds);

        // ROLE_USER
        var user = roleService.getRoleByName("ROLE_USER");
        // ✅ استفاده از HashSet
        Set<Long> userPermissionIds = new HashSet<>();
        userPermissionIds.add(readUser.id());
        roleService.assignPermissionsToRole(user.id(), userPermissionIds);

        // ROLE_GUEST
        var guest = roleService.getRoleByName("ROLE_GUEST");
        // ✅ استفاده از HashSet خالی
        roleService.assignPermissionsToRole(guest.id(), new HashSet<>());

        log.info("✅ Permissions assigned successfully");
    }

    private void initAdminUser() {
        log.info("👤 Creating admin user...");

        var superAdminRole = roleService.getRoleByName("ROLE_SUPER_ADMIN");

        UserRegistrationDto adminRegistration = new UserRegistrationDto(
                "System",
                "Administrator",
                "admin",
                "admin@example.com",
                "Admin@123",
                "+1234567890",
                "https://example.com/avatars/admin.png"
        );

        var admin = userService.registerUser(adminRegistration);
        userService.addRoleToUser(admin.id(), superAdminRole.id());

        log.info("✅ Admin user created successfully!");
        log.info("   Username: admin");
        log.info("   Password: Admin@123");
        log.info("   Email: admin@example.com");
    }

    private void showAdminProfile() {
        log.info("📊 Admin Profile Summary:");

        try {
            UserProfileDto adminProfile = profileService.getUserProfileByUsername("admin");

            log.info("   ┌─────────────────────────────────────────");
            log.info("   │ 👤 User: {} {}", adminProfile.firstName(), adminProfile.lastName());
            log.info("   │ 📧 Email: {}", adminProfile.email());
            log.info("   │ 🔑 Username: {}", adminProfile.username());
            log.info("   │ ✅ Email Verified: {}", adminProfile.emailVerified());
            log.info("   │ 🔓 Account Locked: {}", !adminProfile.accountNonLocked());
            log.info("   │ 📅 Created: {}", adminProfile.createdAt());
            log.info("   ├─────────────────────────────────────────");
            log.info("   │ 👥 Roles: {}", adminProfile.roles().size());
            adminProfile.roles().forEach(role ->
                    log.info("   │   • {} (priority: {})", role.name(), role.priority())
            );
            log.info("   ├─────────────────────────────────────────");
            log.info("   │ 🔐 Permissions: {}", adminProfile.allPermissions().size());
            adminProfile.allPermissions().forEach(permission ->
                    log.info("   │   • {}", permission)
            );
            log.info("   ├─────────────────────────────────────────");
            log.info("   │ 📊 Statistics:");
            log.info("   │   • Total Roles: {}", adminProfile.statistics().totalRoles());
            log.info("   │   • Total Permissions: {}", adminProfile.statistics().totalPermissions());
            log.info("   │   • Password Expired: {}", adminProfile.statistics().isPasswordExpired());
            log.info("   │   • Failed Attempts: {}", adminProfile.statistics().failedAttempts());
            log.info("   └─────────────────────────────────────────");

        } catch (Exception e) {
            log.warn("⚠️ Could not fetch admin profile: {}", e.getMessage());
        }
    }
}