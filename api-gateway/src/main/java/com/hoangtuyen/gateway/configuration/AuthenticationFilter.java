package com.hoangtuyen.gateway.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoangtuyen.gateway.dto.response.ApiResponse;
import com.hoangtuyen.gateway.service.IdentityService;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@Slf4j
public class AuthenticationFilter implements GlobalFilter, Ordered {

    @Autowired
    IdentityService identityService;

    @Autowired
    ObjectMapper objectMapper; // Đảm bảo rằng ObjectMapper được khởi tạo

    @NonFinal
    private String[] publicEndpoints = {
            "/identity/signup",
            "/notification/email/send",
            "/identity/login/google",
            "/oauth2/**",
            "/identity/login/oauth2/code/google"
    };
    private final AntPathMatcher pathMatcher = new AntPathMatcher();


    @NonFinal
    @Value("${app.api-prefix}")
    private String apiPrefix ;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if(isPublicEndpoint(exchange.getRequest()))
            return chain.filter(exchange);
        // Get token from authorization header
        List<String> authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
        if (CollectionUtils.isEmpty(authHeader)) {
            return unauthenticated(exchange.getResponse());
        }
        String token = authHeader.get(0).replace("Bearer ", "");
        log.info("Token: {}", token);

        return identityService.introspect(token)
                .flatMap(introspectResponse -> {
                    // Kiểm tra tính hợp lệ của token
                    if (introspectResponse.getResult() != null && introspectResponse.getResult().isValid()) {
                        return chain.filter(exchange);
                    } else {
                        return unauthenticated(exchange.getResponse());
                    }
                })
                .onErrorResume(throwable -> {
                    log.error("Error during token introspection", throwable);
                    return unauthenticated(exchange.getResponse());
                });
    }

    @Override
    public int getOrder() {
        return -1; // Đặt thứ tự của filter
    }

    Mono<Void> unauthenticated(ServerHttpResponse response) {
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(1401)
                .message("Unauthenticated")
                .build();

        String body;
        try {
            body = objectMapper.writeValueAsString(apiResponse);
        } catch (JsonProcessingException e) {
            log.error("Error serializing response", e);
            body = "{\"code\":1401,\"message\":\"Unauthenticated\"}";
        }

        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        return response.writeWith(
                Mono.just(response.bufferFactory().wrap(body.getBytes()))
        );
    }

    private boolean isPublicEndpoint(ServerHttpRequest request) {
        String path = request.getURI().getPath();
        for (String endpoint : publicEndpoints) {
            if (pathMatcher.match(apiPrefix + endpoint, path)) {
                return true;
            }
        }
        return false;
    }





}