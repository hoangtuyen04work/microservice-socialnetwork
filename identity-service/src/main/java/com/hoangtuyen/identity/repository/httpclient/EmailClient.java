package com.hoangtuyen.identity.repository.httpclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "${app.service.profile.name}", url = "${app.service.profile.url}")
public interface EmailClient {
    @PostMapping(value = "/send/email", produces = MediaType.APPLICATION_JSON_VALUE)
    public void sendEmail(@RequestParam("email") String email);
}
