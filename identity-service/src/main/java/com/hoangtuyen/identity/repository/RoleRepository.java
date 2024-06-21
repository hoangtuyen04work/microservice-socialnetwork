package com.hoangtuyen.identity.repository;

import com.hoangtuyen.identity.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, String> {
    public RoleEntity findByRole(String role);
}
