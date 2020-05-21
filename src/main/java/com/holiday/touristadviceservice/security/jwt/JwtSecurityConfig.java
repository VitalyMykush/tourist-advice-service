package com.holiday.touristadviceservice.security.jwt;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;

@Configuration
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Value("${jwt.secret.key}")
    private String secretKey;


    @Override
    public void configure(HttpSecurity builder){
        builder.addFilter(authenticationFilter())
                .addFilter(authorizationFilter());
    }

    @Bean
    public JwtAuthenticationFilter authenticationFilter() {
        assert authenticationManager != null;
        return new JwtAuthenticationFilter(jwtService(), authenticationManager);
    }

    @Bean
    public JwtService jwtService() {
        return new JwtTokenService(secretKey);
    }

    @Bean
    public JwtAuthorizationFilter authorizationFilter() {
        assert authenticationManager != null;
        return new JwtAuthorizationFilter(jwtService(), authenticationManager);
    }
}
