package com.hoangtuyen.profile.controller.external;

import com.hoangtuyen.profile.dto.ProfileResponse;
import com.hoangtuyen.profile.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ExternalProfileController {

    @Autowired
    private ProfileService profileService;

    @GetMapping("/user/{id}")
    ProfileResponse getProfile(@PathVariable String id){
        return profileService.getProfile(id);
    }



}
