package com.hoangtuyen.identity.mapper;

import com.hoangtuyen.identity.dto.request.ProfileRequest;
import com.hoangtuyen.identity.dto.request.RegisterRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    ProfileRequest toProfileRequest(RegisterRequest registerRequest);

}