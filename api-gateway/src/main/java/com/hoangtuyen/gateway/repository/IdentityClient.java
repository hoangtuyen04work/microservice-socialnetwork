package com.hoangtuyen.gateway.repository;

import com.hoangtuyen.gateway.dto.request.IntrospectRequest;
import com.hoangtuyen.gateway.dto.response.ApiResponse;
import com.hoangtuyen.gateway.dto.response.IntrospectResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Mono;

public interface IdentityClient {
    @PostExchange(url = "/introspect", contentType = MediaType.APPLICATION_JSON_VALUE)
    Mono<ApiResponse<IntrospectResponse>> introspect(

            @RequestBody IntrospectRequest request);
}






