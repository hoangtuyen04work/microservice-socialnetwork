package com.hoangtuyen.identity.repository.httpclient;

import com.hoangtuyen.identity.configuration.AuthenticationrequestInterceptor;
import com.hoangtuyen.identity.dto.request.ProfileRequest;
import com.hoangtuyen.identity.dto.response.ProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@FeignClient(name = "${app.service.profile.name}", url = "${app.service.profile.url}",
        configuration = {AuthenticationrequestInterceptor.class})
public interface ProfileClient {
    @PostMapping(value = "/internal/user", produces = MediaType.APPLICATION_JSON_VALUE)
    ProfileResponse  createProfile(@RequestBody ProfileRequest profile);

    @GetMapping(value = "/internal/users", produces = MediaType.APPLICATION_JSON_VALUE)
    Set<ProfileResponse> getAll();

    @GetMapping(value = "/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ProfileResponse getMyInfo(@PathVariable String id);
}
























