package com.thai.spring_security.services;

import com.thai.spring_security.dtos.CreateAccountRequest;
import com.thai.spring_security.dtos.LoginRequest;
import com.thai.spring_security.dtos.LoginResponse;
import com.thai.spring_security.dtos.UserResponse;
import com.thai.spring_security.entities.Role;
import com.thai.spring_security.entities.User;
import com.thai.spring_security.respositories.RoleRepository;
import com.thai.spring_security.respositories.UserRespository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRespository userRespository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;
    private final RoleRepository roleRepository;

    public UserService (UserRespository userRespository , BCryptPasswordEncoder passwordEncoder, JwtEncoder jwtEncoder, RoleRepository roleRepository)  {
        this.userRespository = userRespository;
        this.passwordEncoder =  passwordEncoder;
        this.jwtEncoder = jwtEncoder;
        this.roleRepository = roleRepository;
    }

    public LoginResponse findByUsername(LoginRequest loginRequest) {

        var user =  userRespository.findByUsername(loginRequest.username());
        if(user.isEmpty() || !user.get().isLoginCorrect(loginRequest, passwordEncoder)) {
            throw new BadCredentialsException("User or password is invalid");
        }
        var expiresIn = 300L;
        Instant now = Instant.now();
        var scopes = user.get().getRoles().stream().map(Role::getName).collect(Collectors.joining(" "));
        var claims  = JwtClaimsSet .builder()
                .issuer("mybackend")
                .subject(
                        user.get().getUserId().toString()
                )
                .expiresAt(
                        now.plusSeconds(expiresIn))
                .issuedAt(now)
                .claim("scope", scopes)
                .build();

        var jwtValue  = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        return new LoginResponse(jwtValue, expiresIn);

    }

    public void createUserAccount(CreateAccountRequest createAccountRequest) {
        var basicRole = roleRepository.findByName(Role.Values.BASIC.name());
        var userFromDB = userRespository.findByUsername(createAccountRequest.username());
        if(userFromDB.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT);
        }

        User user = new User();
        user.setUsername(createAccountRequest.username());
        user.setPassword(passwordEncoder.encode(createAccountRequest.password()) );
        user.setRoles(Set.of(basicRole));
        userRespository.save(user);




    }

    public List<UserResponse> allUsers() {
         return userRespository.findAll().stream().map(user-> new UserResponse(user.getUsername(), user.getPassword())).toList();
    }
}
