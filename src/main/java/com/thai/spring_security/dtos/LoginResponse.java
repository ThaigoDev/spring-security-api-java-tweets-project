package com.thai.spring_security.dtos;

public record LoginResponse(String acessToken, Long expireIn) {
}
