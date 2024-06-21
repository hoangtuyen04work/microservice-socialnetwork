package com.hoangtuyen.identity.controller;


import com.hoangtuyen.identity.dto.request.*;
import com.hoangtuyen.identity.dto.response.*;
import com.hoangtuyen.identity.exception.AppException;
import com.hoangtuyen.identity.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@Slf4j
@RestController
public class AuthenticationController {

    @Autowired
    AuthenticationService authenticationService;

    @GetMapping("/login/google")
    public ApiResponse<AuthenticationResponse> loginGoogle(@AuthenticationPrincipal OAuth2User principal) throws AppException, JOSEException {
        return ApiResponse.<AuthenticationResponse>builder()
                .data(authenticationService.loginGoogle(principal))
                .message("successfully")
                .build();
    }

    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest introspectRequest) throws AppException, ParseException, JOSEException {
        return ApiResponse.<IntrospectResponse>builder()
                .message("token is valid")
                .data(authenticationService.introspect(introspectRequest.getToken()))
                .build();
    }

    @PostMapping("/signup")
    public ApiResponse<AuthenticationResponse> signup(@RequestBody RegisterRequest registerRequest) throws JOSEException, AppException {
        return ApiResponse
                .<AuthenticationResponse>builder()
                .data(authenticationService.signup(registerRequest))
                .message("Sigup successful")
                .build();
    }

    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> login(@RequestBody LoginRequest loginRequest) throws JOSEException, AppException {
        return ApiResponse
                .<AuthenticationResponse>builder()
                .data(authenticationService.login(loginRequest))
                .message("login successful")
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<TokenResponse> test(@RequestBody LogoutRequest logoutRequest) throws AppException, ParseException, JOSEException {
        authenticationService.logout(logoutRequest);
        return ApiResponse.<TokenResponse>builder()
                .message("logout successful")
                .data(null)
                .build();
    }

    @PostMapping("/refreshtoken")
    public ApiResponse<String> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) throws AppException, ParseException, JOSEException {
        return ApiResponse.<String>builder()
                .data(authenticationService.refreshToken(refreshTokenRequest))
                .message("refresh successful")
                .build();
    }
}
