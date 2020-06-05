package com.holiday.touristadviceservice.security.jwt;

import io.jsonwebtoken.*;

import java.util.Base64;
import java.util.Date;

public class JwtTokenService implements JwtService {

    private final String secretKey;
    private SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;

    public JwtTokenService(final String secretKey) {
        assert secretKey != null;
        this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    @Override
    public String generateJwtToken(String username) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date validity = new Date(nowMillis + JwtConstants.TOKEN_VALIDITY);
        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(validity)
                .setSubject(username)
                .signWith(signatureAlgorithm, secretKey)
                .compact();
    }

    @Override
    public String getUsername(String token) {

        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token).getBody().getSubject();
    }

    @Override
    public boolean validateToken(String token) {
        Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        return claims.getBody().getExpiration().before(new Date());
    }
}

