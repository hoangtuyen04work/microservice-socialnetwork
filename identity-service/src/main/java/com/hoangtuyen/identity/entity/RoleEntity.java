package com.hoangtuyen.identity.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "role")
@Builder
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String role;

    @ManyToMany(mappedBy = "roles", cascade =  CascadeType.REMOVE)
    Set<UserEntity> users;

    @ManyToMany(mappedBy = "roles", cascade =  CascadeType.REMOVE)
    Set<EmailEntity> mails;

}
