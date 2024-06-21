package com.hoangtuyen.identity.mapper;

import com.hoangtuyen.identity.dto.response.RoleResponse;
import com.hoangtuyen.identity.entity.RoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "role", source = "role")
    RoleResponse toRoleResponse(RoleEntity roleEntity);

}
