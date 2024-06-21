package com.hoangtuyen.profile.dto.response;

import com.hoangtuyen.profile.dto.request.Recipient;
import com.hoangtuyen.profile.dto.request.Sender;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailResponse {
    private String messageId;
}
