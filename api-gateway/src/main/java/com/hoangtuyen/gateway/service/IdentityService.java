package com.hoangtuyen.gateway.service;

import com.hoangtuyen.gateway.dto.request.IntrospectRequest;
import com.hoangtuyen.gateway.dto.response.ApiResponse;
import com.hoangtuyen.gateway.dto.response.IntrospectResponse;
import com.hoangtuyen.gateway.repository.IdentityClient;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IdentityService {
    IdentityClient identityClient;

    public Mono<ApiResponse<IntrospectResponse>> introspect(String token){
        return identityClient.introspect(IntrospectRequest.builder()
                .token(token)
                .build());
    }
}