package com.company.crmticketing.service;

import com.company.crmticketing.dto.request.LoginRequest;
import com.company.crmticketing.dto.response.LoginResponse;
import com.company.crmticketing.model.User;
import com.company.crmticketing.repository.UserRepository;
import com.company.crmticketing.security.model.SecurityUser;
import com.company.crmticketing.security.provider.JwtTokenProvider;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public LoginResponse login(LoginRequest request) {

        log.info("Login attempt for username: {}", request.getUsername());

        try {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            SecurityUser securityUser =
                    (SecurityUser) authentication.getPrincipal();

            User user = userRepository
                    .findByUsernameWithRolesAndPermissions(securityUser.getUsername())
                    .orElseThrow(() ->
                            new EntityNotFoundException("User not found."));

            String accessToken =
                    jwtTokenProvider.generateAccessToken(authentication);

            String refreshToken =
                    jwtTokenProvider.generateRefreshToken(authentication);

            log.info("User [{}] logged in successfully.", user.getUsername());

            return LoginResponse.builder()

                    .accessToken(accessToken)

                    .refreshToken(refreshToken)

                    .expiresIn(jwtTokenProvider.getJwtExpiration())

                    .user(
                            LoginResponse.UserInfo.builder()
                                    .id(user.getId())
                                    .username(user.getUsername())
                                    .email(user.getEmail())
                                    .roles(
                                            user.getRoles()
                                                    .stream()
                                                    .map(role -> role.getName())
                                                    .collect(java.util.stream.Collectors.toSet())
                                    )
                                    .build()
                    )

                    .build();

        } catch (BadCredentialsException ex) {

            log.warn("Invalid credentials for {}", request.getUsername());

            throw new BadCredentialsException("Invalid username or password.");
        } catch (LockedException ex) {

            log.warn("Locked account: {}", request.getUsername());

            throw new LockedException("Account is locked.");
        } catch (DisabledException ex) {

            log.warn("Disabled account: {}", request.getUsername());

            throw new DisabledException("Account is disabled.");
        } catch (AuthenticationException ex) {

            log.error("Authentication failed: {}", ex.getMessage());

            throw ex;
        }
    }
}
