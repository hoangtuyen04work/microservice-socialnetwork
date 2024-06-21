package com.hoangtuyen.profile.mapper;

import com.hoangtuyen.profile.dto.ProfileRequest;
import com.hoangtuyen.profile.dto.ProfileResponse;
import com.hoangtuyen.profile.entity.ProfileEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    ProfileEntity toUserProfile(ProfileRequest profileRequest);
    ProfileResponse toUserProfileResponse(ProfileEntity profileEntity);
}
