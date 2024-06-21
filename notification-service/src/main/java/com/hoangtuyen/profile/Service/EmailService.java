package com.hoangtuyen.profile.Service;

import com.hoangtuyen.profile.dto.request.EmailRequest;
import com.hoangtuyen.profile.dto.request.SendEmailRequest;
import com.hoangtuyen.profile.dto.request.Sender;
import com.hoangtuyen.profile.dto.response.EmailResponse;
import com.hoangtuyen.profile.exception.AppException;
import com.hoangtuyen.profile.exception.ErrorCode;
import com.hoangtuyen.profile.repository.httpclient.EmailClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Autowired
    EmailClient    emailClient;
    String apiKey = "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";

    public EmailResponse sendEmail(SendEmailRequest sendEmailRequest){
        EmailRequest emailRequest = EmailRequest.builder()
                .sender(Sender.builder()
                        .name("Hoang Tuyen")
                        .email("hoanghuutuyen060204@gmail.com")
                        .build()
                )
                .to(List.of(sendEmailRequest.getTo()))
                .subject(sendEmailRequest.getSubject())
                .htmlContent(sendEmailRequest.getHtmlContent())
                .build();
        try{
            return emailClient.sendEmail(apiKey, emailRequest);
        }
        catch(FeignException.FeignClientException e){
            System.err.print(e);
            throw new AppException(ErrorCode.CANNOT_SEND_EMAIL);
        }
    }
}
