package com.hoangtuyen.identity.service;


import com.hoangtuyen.identity.dto.CreateTokenObject;
import com.hoangtuyen.identity.dto.request.*;
import com.hoangtuyen.identity.dto.response.*;
import com.hoangtuyen.identity.entity.EmailEntity;
import com.hoangtuyen.identity.mapper.ProfileMapper;
import com.hoangtuyen.identity.mapper.UserMapper;
import com.hoangtuyen.identity.entity.UserEntity;
import com.hoangtuyen.identity.exception.AppException;
import com.hoangtuyen.identity.exception.ErrorCode;
import com.hoangtuyen.identity.repository.httpclient.ProfileClient;
import com.nimbusds.jose.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
@RequiredArgsConstructor
public class AuthenticationService{
    UserMapper userMapper;
    TokenService tokenService;
    UserService userService;
    ProfileMapper profileMapper;
    ProfileClient profileClient;
    PasswordEncoder passwordEncoder;
    EmailService emailService;

    public void logout(LogoutRequest logoutRequest) throws AppException, ParseException, JOSEException {
        tokenService.unableToken(logoutRequest.getToken());
    }

    @Transactional
    public AuthenticationResponse loginGoogle(OAuth2User principal) throws  JOSEException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken)authentication;
        OAuth2User oAuth2User = token.getPrincipal();
        if(emailService.existsByEmail(oAuth2User.getAttribute("email"))){
            EmailEntity emailEntity = emailService.findByEmail(oAuth2User.getAttribute("email"));
            ProfileResponse profileResponse = profileClient.getMyInfo(emailEntity.getId());
            return AuthenticationResponse.builder()
                    .info(userMapper.toUserResponse(emailEntity, profileResponse))
                    .token(tokenService.generateToken(buildCreateTokenObject(emailEntity), false))
                    .build();
        }
        else{
            EmailEntity emailEntity = new EmailEntity();
            emailEntity.setEmail(oAuth2User.getAttribute("email"));
            userService.setRoleUser(emailEntity);
            emailEntity = emailService.save(emailEntity);
            ProfileRequest profileRequest = userService.setAttributeProfileRequest(emailEntity, principal);
            ProfileResponse profileResponse = profileClient.createProfile(profileRequest);
            return AuthenticationResponse.builder()
                    .info(userMapper.toUserResponse(emailEntity, profileResponse))
                    .token(tokenService.generateToken(buildCreateTokenObject(emailEntity), false))
                    .build();
        }
    }

    public AuthenticationResponse signup(RegisterRequest registerRequest) throws AppException, JOSEException {
        if(userService.existByName(registerRequest.getUserName())){
            throw  new AppException(ErrorCode.USER_EXISTED);
        }
        UserEntity userEntity = userMapper.toUserEntity(registerRequest);
        userService.setRoleUser(userEntity);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userEntity = userService.create(userEntity);
        ProfileRequest profileRequest = profileMapper.toProfileRequest(registerRequest);
        profileRequest.setUserId(userEntity.getId());
        var profileResponse = profileClient.createProfile(profileRequest);
        return AuthenticationResponse.builder()
                .info(userMapper.toUserResponse(userEntity, profileResponse))
                .token(tokenService.generateToken(buildCreateTokenObject(userEntity), false))
                .build();
    }

    public AuthenticationResponse login(LoginRequest loginRequest) throws AppException, JOSEException {
        if(!userService.existByName(loginRequest.getUserName())){
            throw  new AppException(ErrorCode.USER_NOT_EXISTED);
        }
        UserEntity userEntity = userService.getByName(loginRequest.getUserName());
        if(!isCorrectPassword(userEntity, loginRequest.getPassword()))
            throw new AppException(ErrorCode.WRONG_PASSWORD_OR_USERNAME);
        ProfileResponse profileResponse = getProfileClient(userEntity.getId());
        return AuthenticationResponse.builder()
                .info(userMapper.toUserResponse(userEntity, profileResponse))
                .token(tokenService.generateToken(buildCreateTokenObject(userEntity), false))
                .build();
    }

    private ProfileResponse getProfileClient(String userId){
        return profileClient.getMyInfo(userId);
    }

    public String refreshToken(RefreshTokenRequest refreshTokenRequest) throws AppException, JOSEException {
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity userEntity = userService.findById(id);
        return tokenService.generateToken(buildCreateTokenObject(userEntity), true);
    }

    private CreateTokenObject buildCreateTokenObject(UserEntity userEntity){
        return CreateTokenObject.builder()
                .id(userEntity.getId())
                .name(userEntity.getUserName())
                .roles(userEntity.getRoles())
                .build();
    }

    private CreateTokenObject buildCreateTokenObject(EmailEntity emailEntity){
        return CreateTokenObject.builder()
                .id(emailEntity.getId())
                .name(emailEntity.getEmail())
                .roles(emailEntity.getRoles())
                .build();
    }

    boolean isCorrectPassword(UserEntity userEntity, String password){
        return passwordEncoder.matches(userEntity.getPassword(), password);
    }

    public IntrospectResponse introspect(String token) throws AppException, ParseException, JOSEException {
        boolean isValid = tokenService.verifyToken(token) != null;
        return IntrospectResponse.builder().valid(isValid).build();
    }
}
