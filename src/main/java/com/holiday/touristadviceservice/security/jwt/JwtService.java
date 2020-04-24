package com.holiday.touristadviceservice.security.jwt;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

public interface JwtService {
    String generateJwtToken(String username);
    String getUsername(String token);
    boolean validateToken(String token);

}
