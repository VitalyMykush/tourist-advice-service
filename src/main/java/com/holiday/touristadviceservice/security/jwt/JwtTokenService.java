package com.holiday.touristadviceservice.security.jwt;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenService implements JwtService {

    @Value("${jwt.secret.key}")
    public String SECRET_KEY;
    private long JWT_TOKEN_VALIDITY = 3600000; // 1h
    private SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;

    @PostConstruct
    public void init() {
        SECRET_KEY = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
    }

    @Override
    public String generateJwtToken(String username) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date validity = new Date(nowMillis + JWT_TOKEN_VALIDITY);
        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(validity)
                .setSubject(username)
                .signWith(signatureAlgorithm, SECRET_KEY)
                .compact();
    }

    @Override
    public String getUsername(String token){

        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token).getBody().getSubject();
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            if (claims.getBody().getExpiration().before(new Date())) {
                return false;
            }
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtAuthenticationException("Expired or invalid JWT token");
        }
    }
}

