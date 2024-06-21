package com.hoangtuyen.profile.dto;


import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponse {
    private String id;
    private String userId;
    private String userName;
    private String firstName;
    private String lastName;
    private LocalDate birthday;
}
