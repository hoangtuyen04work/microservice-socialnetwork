package com.hoangtuyen.profile.repository;

import com.hoangtuyen.profile.entity.ProfileEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileRepository extends Neo4jRepository<ProfileEntity, String> {
    boolean existsByUserId( String userId);
    List<ProfileEntity> findAll();
    ProfileEntity findByUserId(String userId);

}
