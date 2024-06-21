package com.hoangtuyen.identity.dto.request;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {

    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private LocalDate birthday;

}
