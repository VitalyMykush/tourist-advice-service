package com.holiday.touristadviceservice.security.jwt;

public class JwtConstants {
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final long TOKEN_VALIDITY = 3_600_000; //1h
}
