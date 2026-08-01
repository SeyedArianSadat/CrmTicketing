package com.company.crmticketing.controller.rest;

import com.company.crmticketing.dto.request.LoginRequest;
import com.company.crmticketing.dto.response.LoginResponse;
import com.company.crmticketing.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthControllerTest {

    private final AuthService authService = mock();
    private final AuthController controller = new AuthController(authService);

    @Test
    void loginReturnsServiceResponse() {
        LoginRequest request = LoginRequest.builder()
                .username("admin")
                .password("secret")
                .build();
        LoginResponse response = LoginResponse.builder()
                .accessToken("access")
                .refreshToken("refresh")
                .expiresIn(3600)
                .build();
        when(authService.login(request)).thenReturn(response);

        ResponseEntity<LoginResponse> result = controller.login(request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isSameAs(response);
    }
}
