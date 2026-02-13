package com.thai.spring_security.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name ="user_id")
    private UUID userId;

    @Column(unique = true)
    private String username;

    private String password;

     @ManyToMany(cascade =  CascadeType.ALL, fetch = FetchType.EAGER)
     @JoinTable(
             name = "users_roles",
             joinColumns = @JoinColumn(name = "user_id"),
             inverseJoinColumns = @JoinColumn(name = "role_id")
     )
    private Set<Role> roles;


}
