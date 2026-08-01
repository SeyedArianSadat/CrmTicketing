package com.company.crmticketing.service;

import com.company.crmticketing.dto.request.LoginRequest;
import com.company.crmticketing.dto.response.LoginResponse;
import com.company.crmticketing.model.Role;
import com.company.crmticketing.model.User;
import com.company.crmticketing.repository.UserRepository;
import com.company.crmticketing.security.model.SecurityUser;
import com.company.crmticketing.security.provider.JwtTokenProvider;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;

    @Test
    void loginAuthenticatesUserAndBuildsTokenResponse() {
        LoginRequest request = LoginRequest.builder()
                .username("admin")
                .password("secret")
                .build();
        User user = user();
        SecurityUser securityUser = new SecurityUser(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                securityUser,
                null,
                securityUser.getAuthorities()
        );

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(userRepository.findByUsernameWithRolesAndPermissions("admin"))
                .thenReturn(Optional.of(user));
        when(jwtTokenProvider.generateAccessToken(authentication)).thenReturn("access-token");
        when(jwtTokenProvider.generateRefreshToken(authentication)).thenReturn("refresh-token");
        when(jwtTokenProvider.getJwtExpiration()).thenReturn(3600L);

        LoginResponse response = authService.login(request);

        assertThat(response.getAccessToken()).isEqualTo("access-token");
        assertThat(response.getRefreshToken()).isEqualTo("refresh-token");
        assertThat(response.getExpiresIn()).isEqualTo(3600L);
        assertThat(response.getTokenType()).isEqualTo("Bearer");
        assertThat(response.getUser().getId()).isEqualTo(1L);
        assertThat(response.getUser().getUsername()).isEqualTo("admin");
        assertThat(response.getUser().getRoles()).containsExactly("ROLE_ADMIN");
    }

    @Test
    void loginTranslatesBadCredentialsMessage() {
        LoginRequest request = LoginRequest.builder()
                .username("admin")
                .password("wrong")
                .build();
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("bad"));

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessage("Invalid username or password.");
    }

    @Test
    void loginTranslatesDisabledAccountMessage() {
        LoginRequest request = LoginRequest.builder()
                .username("admin")
                .password("secret")
                .build();
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new DisabledException("disabled"));

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(DisabledException.class)
                .hasMessage("Account is disabled.");
    }

    @Test
    void loginThrowsWhenAuthenticatedUserCannotBeLoadedWithRoles() {
        LoginRequest request = LoginRequest.builder()
                .username("admin")
                .password("secret")
                .build();
        User user = user();
        SecurityUser securityUser = new SecurityUser(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(securityUser, null);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(userRepository.findByUsernameWithRolesAndPermissions("admin"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("User not found.");
    }

    private static User user() {
        Role role = new Role();
        role.setName("ROLE_ADMIN");

        User user = new User();
        user.setId(1L);
        user.setUsername("admin");
        user.setPassword("encoded");
        user.setEmail("admin@test.local");
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.addRole(role);
        return user;
    }
}
