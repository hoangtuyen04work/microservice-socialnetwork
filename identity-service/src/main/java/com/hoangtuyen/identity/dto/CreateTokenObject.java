package com.hoangtuyen.identity.dto;

import com.hoangtuyen.identity.entity.RoleEntity;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
@RequiredArgsConstructor
public class CreateTokenObject {
    String id;
    String name;
    Set<RoleEntity> roles;
}
