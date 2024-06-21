package com.hoangtuyen.identity.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "mail")
public class EmailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String email;
    @ManyToMany(cascade =  CascadeType.REMOVE)
    @JoinTable(
            name = "role_mails",
            joinColumns = @JoinColumn(name = "mail_id"),
            inverseJoinColumns = @JoinColumn(name = "roles_id")
    )
    Set<RoleEntity> roles;

}
