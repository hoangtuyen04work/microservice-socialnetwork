package com.hoangtuyen.profile.service;

import com.hoangtuyen.profile.dto.ProfileRequest;
import com.hoangtuyen.profile.dto.ProfileResponse;
import com.hoangtuyen.profile.entity.ProfileEntity;
import com.hoangtuyen.profile.mapper.ProfileMapper;
import com.hoangtuyen.profile.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.HashSet;
import java.util.Set;

@Service
public class ProfileService {
    @Autowired
    private ProfileMapper profileMapper;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private TransactionTemplate transactionTemplate;

    public Set<ProfileResponse> getAll(){
        Set<ProfileResponse> listResponse = new HashSet<>();
        Set<ProfileEntity> listEntity = new HashSet<>(profileRepository.findAll());
        for(ProfileEntity profileEntity : listEntity){
            listResponse.add(profileMapper.toUserProfileResponse(profileEntity));
        }
        return listResponse;
    }

    public boolean existProfile(ProfileRequest profileRequest){
        return profileRepository.existsByUserId(profileRequest.getUserId());
    }

    public ProfileResponse createUser(ProfileRequest profileRequest){

        if(existProfile(profileRequest)){
            throw new RuntimeException("OH SHIT");
        }
        ProfileEntity profileEntity = profileMapper.toUserProfile(profileRequest);
        profileEntity = profileRepository.save(profileEntity);
        return profileMapper.toUserProfileResponse(profileEntity);
    }

    public ProfileResponse getProfile(String userId){
        ProfileEntity profileEntity = profileRepository.findByUserId(userId);
        return profileMapper.toUserProfileResponse(profileEntity);
    }

}
