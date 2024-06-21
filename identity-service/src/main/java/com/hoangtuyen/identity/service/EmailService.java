package com.hoangtuyen.identity.service;

import com.hoangtuyen.identity.entity.EmailEntity;
import com.hoangtuyen.identity.repository.EmailRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
@RequiredArgsConstructor
public class EmailService {
    EmailRepository emailRepository;
    public boolean existsByEmail(String email){
        return emailRepository.existsByEmail(email);
    }
    public EmailEntity findByEmail(String email){
        return emailRepository.findByEmail(email);
    }
    public EmailEntity save(EmailEntity emailEntity){
        return  emailRepository.save(emailEntity);
    }
}
