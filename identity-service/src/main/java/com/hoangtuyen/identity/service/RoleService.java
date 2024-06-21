package com.hoangtuyen.identity.service;

import com.hoangtuyen.identity.repository.RoleRepository;
import com.hoangtuyen.identity.entity.RoleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService{
    @Autowired
    private RoleRepository roleRepository;

    public RoleEntity findRole(String roleName) {
        return roleRepository.findByRole(roleName);
    }
}
