package com.hoangtuyen.profile.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Sender {
    private String name;
    private String email;
}
