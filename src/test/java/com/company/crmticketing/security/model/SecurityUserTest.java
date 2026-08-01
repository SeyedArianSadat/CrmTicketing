package com.company.crmticketing.security.model;

import com.company.crmticketing.model.Permission;
import com.company.crmticketing.model.Role;
import com.company.crmticketing.model.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class SecurityUserTest {

    @Test
    void exposesRolesPermissionsAndFullNameAsUserDetails() {
        Permission permission = new Permission();
        permission.setName("ticket:read");
        Role role = new Role();
        role.setName("ROLE_ADMIN");
        role.addPermission(permission);

        User user = user();
        user.setFirstName("Ada");
        user.setLastName("Lovelace");
        user.addRole(role);

        SecurityUser securityUser = new SecurityUser(user);

        assertThat(securityUser.getUsername()).isEqualTo("ada");
        assertThat(securityUser.getFullName()).isEqualTo("Ada Lovelace");
        assertThat(securityUser.hasRole("ROLE_ADMIN")).isTrue();
        assertThat(securityUser.hasPermission("ticket:read")).isTrue();
        assertThat(securityUser.isAdmin()).isTrue();
        assertThat(securityUser.getAuthorities())
                .extracting("authority")
                .containsExactlyInAnyOrder("ROLE_ADMIN", "ticket:read");
    }

    @Test
    void disabledWhenUnderlyingUserIsDeleted() {
        User user = user();
        user.setDeleted(true);

        SecurityUser securityUser = new SecurityUser(user);

        assertThat(securityUser.isEnabled()).isFalse();
    }

    @Test
    void lockedWhenLockedUntilIsInTheFuture() {
        User user = user();
        user.setLockedUntil(LocalDateTime.now().plusMinutes(10));

        SecurityUser securityUser = new SecurityUser(user);

        assertThat(securityUser.isAccountNonLocked()).isFalse();
    }

    private static User user() {
        User user = new User();
        user.setId(1L);
        user.setUsername("ada");
        user.setPassword("encoded");
        user.setEmail("ada@test.local");
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        return user;
    }
}
