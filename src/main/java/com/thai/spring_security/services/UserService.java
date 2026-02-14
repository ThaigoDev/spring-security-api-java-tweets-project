package com.thai.spring_security.services;

import com.thai.spring_security.dtos.LoginRequest;
import com.thai.spring_security.dtos.LoginResponse;
import com.thai.spring_security.respositories.UserRespository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class UserService {
    private final UserRespository userRespository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;

    public UserService (UserRespository userRespository , BCryptPasswordEncoder passwordEncoder, JwtEncoder jwtEncoder)  {
        this.userRespository = userRespository;
        this.passwordEncoder =  passwordEncoder;
        this.jwtEncoder = jwtEncoder;
    }

    public LoginResponse findByUsername(LoginRequest loginRequest) {

        var user =  userRespository.findByUsername(loginRequest.username());
        if(user.isEmpty() || !user.get().isLoginCorrect(loginRequest, passwordEncoder)) {
            throw new BadCredentialsException("User or password is invalid");
        }
        var expiresIn = 300L;
        Instant now = Instant.now();
        var claims  = JwtClaimsSet .builder().issuer("mybackend").subject(user.get().getUserId().toString()).expiresAt(now.plusSeconds(expiresIn)).issuedAt(now).build();

        var jwtValue  = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        return new LoginResponse(jwtValue, expiresIn);

    }
}
