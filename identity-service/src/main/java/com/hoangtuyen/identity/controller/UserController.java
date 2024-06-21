package com.hoangtuyen.identity.controller;
import com.hoangtuyen.identity.dto.request.RegisterRequest;
import com.hoangtuyen.identity.dto.response.ApiResponse;
import com.hoangtuyen.identity.dto.response.TokenResponse;
import com.hoangtuyen.identity.dto.response.UserResponse;
import com.hoangtuyen.identity.exception.AppException;
import com.hoangtuyen.identity.service.UserService;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
public class UserController {

    @Autowired
    private UserService userService;



    @GetMapping("/users")
    public ApiResponse<Set<UserResponse>> getUsers() {
        return ApiResponse.<Set<UserResponse>>builder()
                .data(userService.getAll())
                .message("successfully")
                .build();
    }

    @PutMapping("/user")
    public ApiResponse<UserResponse> editUser(@RequestBody RegisterRequest registerRequest) throws AppException, JOSEException {
        return ApiResponse.<UserResponse>builder()
                .data(userService.updateUser(registerRequest))
                .message("edit successfully")
                .build();
    }

    @DeleteMapping("/user")
    public ApiResponse<TokenResponse> deleteUser() throws AppException{
        userService.deleteUser();
        return ApiResponse.<TokenResponse>builder()
                .data(null)
                .message("delete successfully")
                .build();
    }
}
