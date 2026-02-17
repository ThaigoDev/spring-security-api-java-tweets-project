package com.thai.spring_security.controllers;

import com.thai.spring_security.dtos.CreateAccountRequest;
import com.thai.spring_security.dtos.LoginRequest;
import com.thai.spring_security.dtos.LoginResponse;
import com.thai.spring_security.dtos.UserResponse;
import com.thai.spring_security.entities.Role;
import com.thai.spring_security.respositories.RoleRepository;
import com.thai.spring_security.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class UserController {

     private final UserService userService;
     private final RoleRepository roleRepository;
     public UserController(UserService userService, RoleRepository roleRepository) {
         this.userService = userService;
         this.roleRepository = roleRepository;
     }

     @PostMapping("/users")
     public ResponseEntity<Void> createAccount (@RequestBody CreateAccountRequest createAccountRequest) {
         userService.createUserAccount(createAccountRequest);
         return ResponseEntity.ok().build();
     }

     @GetMapping("/users")
     @PreAuthorize("hasAuthority('SCOPE_admin')")
    public ResponseEntity<List<UserResponse>> allUsers () {

         return ResponseEntity.ok().body(userService.allUsers());
     }

}
