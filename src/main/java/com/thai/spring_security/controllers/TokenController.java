package com.thai.spring_security.controllers;

import com.thai.spring_security.services.UserService;
import com.thai.spring_security.dtos.LoginRequest;
import com.thai.spring_security.dtos.LoginResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class TokenController {
    private final JwtEncoder jwtEncoder;
    private final UserService userService;
    public TokenController(JwtEncoder jwtEncoder, UserService userService) {
        this.jwtEncoder = jwtEncoder;
        this.userService =userService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login (@RequestBody LoginRequest loginRequest) {
         return ResponseEntity.ok(userService.findByUsername(loginRequest)) ;
    }
}
