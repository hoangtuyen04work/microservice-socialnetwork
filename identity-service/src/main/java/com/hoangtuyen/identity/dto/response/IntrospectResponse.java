package com.hoangtuyen.identity.dto.response;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class IntrospectResponse {
    private boolean valid;

}

