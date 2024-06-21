package com.hoangtuyen.identity.repository;

import com.hoangtuyen.identity.entity.EmailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepository extends JpaRepository<EmailEntity, String> {
    boolean existsByEmail(String email);
    EmailEntity findByEmail(String email);
    void deleteByEmail(String email);
}
