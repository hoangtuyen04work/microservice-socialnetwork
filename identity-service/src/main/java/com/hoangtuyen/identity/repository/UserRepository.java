package com.hoangtuyen.identity.repository;

import com.hoangtuyen.identity.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    boolean existsByUserName(String userName);
    UserEntity findByUserName(String userName);
    List<UserEntity> findAll();
}