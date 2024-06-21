package com.hoangtuyen.identity.mapper;

import com.hoangtuyen.identity.dto.request.LoginRequest;
import com.hoangtuyen.identity.dto.request.RegisterRequest;
import com.hoangtuyen.identity.dto.response.ProfileResponse;
import com.hoangtuyen.identity.dto.response.RoleResponse;
import com.hoangtuyen.identity.dto.response.UserResponse;
import com.hoangtuyen.identity.entity.EmailEntity;
import com.hoangtuyen.identity.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.Set;

@Mapper(componentModel = "spring", uses = RoleMapper.class)
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mappings({
            @Mapping(target = "roles", source = "roles")
    })
    UserResponse toUserResponse(UserEntity userEntity);

    UserEntity toUserEntity(RegisterRequest registerUserRequest);

    UserEntity toUserEntity(LoginRequest loginUserRequest);

    UserResponse toUserResponse(ProfileResponse profile);

    @Mappings({
            @Mapping(target = "id", source = "emailEntity.id"),
            @Mapping(target = "email", source = "emailEntity.email"),
            @Mapping(target = "userName", source = "profileResponse.userName"),
            @Mapping(target = "firstName", source = "profileResponse.firstName"),
            @Mapping(target = "lastName", source = "profileResponse.lastName"),
            @Mapping(target = "birthday", source = "profileResponse.birthday"),
            @Mapping(target = "roles", source = "emailEntity.roles"),
    })
    UserResponse toUserResponse(EmailEntity emailEntity, ProfileResponse profileResponse);

    @Mappings({
            @Mapping(target = "id", source = "userEntity.id"),
            @Mapping(target = "userName", source = "profileResponse.userName"),
            @Mapping(target = "firstName", source = "profileResponse.firstName"),
            @Mapping(target = "lastName", source = "profileResponse.lastName"),
            @Mapping(target = "birthday", source = "profileResponse.birthday"),
            @Mapping(target = "roles", source = "userEntity.roles"),
    })
    UserResponse toUserResponse(UserEntity userEntity, ProfileResponse profileResponse);
}