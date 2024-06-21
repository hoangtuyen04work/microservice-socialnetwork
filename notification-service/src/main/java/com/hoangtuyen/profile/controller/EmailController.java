package com.hoangtuyen.profile.controller;

import com.hoangtuyen.profile.Service.EmailService;
import com.hoangtuyen.profile.dto.ApiResponse;
import com.hoangtuyen.profile.dto.request.SendEmailRequest;
import com.hoangtuyen.profile.dto.response.EmailResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailController {
    EmailService emailService;

    @PostMapping("/email/send")
    ApiResponse<EmailResponse> sendEmail(@RequestBody SendEmailRequest sendEmailRequest){
        return ApiResponse.<EmailResponse>builder()
                .code(1001)
                .result(emailService.sendEmail(sendEmailRequest))
                .message("send email sucessfully")
                .build();
    }
}
