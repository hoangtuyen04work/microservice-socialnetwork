package com.hoangtuyen.identity.configuration;


import com.hoangtuyen.identity.constant.Constant;
import com.hoangtuyen.identity.entity.RoleEntity;
import com.hoangtuyen.identity.entity.UserEntity;
import com.hoangtuyen.identity.repository.RoleRepository;
import com.hoangtuyen.identity.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;

    @NonFinal
    static final String ADMIN_USER_NAME = "admin";

    @NonFinal
    static final String ADMIN_PASSWORD = "admin";

    @Bean
    @ConditionalOnProperty(
            prefix = "spring",
            value = "datasource.driverClassName",
            havingValue = "com.mysql.cj.jdbc.Driver")
    ApplicationRunner applicationRunner(UserRepository userRepository, RoleRepository roleRepository) {
        RoleEntity userRole = roleRepository.save(RoleEntity.builder()
                .role(Constant.USER)
                .build());

        RoleEntity adminRole = roleRepository.save(RoleEntity.builder()
                .role(Constant.ADMIN)
                .build());

        var roles = new HashSet<RoleEntity>();
        roles.add(adminRole);
        roles.add(userRole);
        UserEntity user = UserEntity.builder()
                .userName(ADMIN_USER_NAME)
                .password(passwordEncoder.encode(ADMIN_PASSWORD))
                .roles(roles)
                .build();
        userRepository.save(user);
        return null;
    }
}
