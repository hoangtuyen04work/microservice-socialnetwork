package com.hoangtuyen.profile.controller.internal;

import com.hoangtuyen.profile.dto.ProfileRequest;
import com.hoangtuyen.profile.dto.ProfileResponse;
import com.hoangtuyen.profile.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
public class InternalProfileController {

    @Autowired
    private ProfileService profileService;

    @PostMapping("/internal/user")
    ProfileResponse createProflie(@RequestBody ProfileRequest profileRequest){
        return profileService.createUser(profileRequest);
    }

    @GetMapping("/internal/users")
    Set<ProfileResponse> getAll(){
        return profileService.getAll();
    }
}












