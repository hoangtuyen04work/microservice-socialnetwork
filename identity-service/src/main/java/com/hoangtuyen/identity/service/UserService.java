package com.hoangtuyen.identity.service;

import com.hoangtuyen.identity.dto.request.ProfileRequest;
import com.hoangtuyen.identity.dto.request.RegisterRequest;
import com.hoangtuyen.identity.entity.EmailEntity;
import com.hoangtuyen.identity.entity.RoleEntity;
import com.hoangtuyen.identity.mapper.UserMapper;
import com.hoangtuyen.identity.repository.RoleRepository;
import com.hoangtuyen.identity.repository.UserRepository;
import com.hoangtuyen.identity.dto.response.UserResponse;
import com.hoangtuyen.identity.entity.UserEntity;
import com.hoangtuyen.identity.exception.AppException;
import com.hoangtuyen.identity.exception.ErrorCode;
import com.hoangtuyen.identity.repository.httpclient.ProfileClient;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
@RequiredArgsConstructor
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    RoleRepository roleRepository;
    ProfileClient profileClient;

    public ProfileRequest setAttributeProfileRequest(EmailEntity emailEntity, OAuth2User oAuth2User) {
        ProfileRequest profileRequest = new ProfileRequest();
        oAuth2User.getAttribute("name");
        profileRequest.setLastName(oAuth2User.getAttribute("given_name"));
        profileRequest.setFirstName(oAuth2User.getAttribute("family_name"));
        profileRequest.setUserId(emailEntity.getId());
        return profileRequest;
    }

    public Set<UserResponse> getAll(){
        Set<UserResponse> result = new HashSet<>();
        Set<UserEntity> allUser = new HashSet<>(userRepository.findAll());
        for(UserEntity userEntity: allUser){
            result.add(userMapper.toUserResponse(userEntity, profileClient.getMyInfo(userEntity.getId())));
        }
        return result;
    }

    public UserResponse getById(String id) throws AppException {
        UserEntity userEntity = userRepository.getById(id);
        if(userEntity.getId() == null){
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
        return userMapper.toUserResponse(userEntity);
    }

    public UserEntity findById(String id) throws AppException {
        if(!existById(id)) throw new AppException(ErrorCode.USER_NOT_EXISTED);
        return userRepository.findById(id).get();
    }

    public boolean existByName(String name){
        return userRepository.existsByUserName(name);
    }

    @PostAuthorize("returnObject.id == authentication.name")
    public UserResponse updateUser(RegisterRequest registerUserRequest) throws AppException, JOSEException {
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity userEntity = findById(id);
        return userMapper.toUserResponse(save(userEntity));
    }

    public UserEntity save(UserEntity userEntity){
        return userRepository.save(userEntity);
    }


    public void deleteUser() throws AppException {
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!userRepository.existsById(id)){
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
        userRepository.deleteById(id);
    }

    public boolean existById(String id){
        return userRepository.existsById(id);
    }

    public UserEntity create(UserEntity userEntity){
        return userRepository.save(userEntity);
    }

    public UserEntity getByName(String name){
        if(existByName(name)){
            return userRepository.findByUserName(name);
        }
        return null;
    }

    public void setRoleUser(UserEntity userEntity){
        Set<RoleEntity> roles = new HashSet<>();
        roles.add(roleRepository.findByRole("USER"));
        roles.add(roleRepository.findByRole("ADMIN"));
        userEntity.setRoles(roles);
    }

    public void setRoleUser(EmailEntity emailEntity){
        Set<RoleEntity> roles = new HashSet<>();
        roles.add(roleRepository.findByRole("USER"));
        emailEntity.setRoles(roles);
    }



}
