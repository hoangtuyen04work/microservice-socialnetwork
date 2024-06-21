package com.hoangtuyen.profile.dto.request;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendEmailRequest {
    Recipient to;
    String subject;
    String htmlContent;
}
