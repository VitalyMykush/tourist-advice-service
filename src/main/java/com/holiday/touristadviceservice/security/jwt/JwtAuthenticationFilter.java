package com.holiday.touristadviceservice.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.holiday.touristadviceservice.entity.AppUser;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService, AuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                AppUser user = objectMapper.readValue(request.getInputStream(), AppUser.class);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), Collections.emptyList());
                return this.getAuthenticationManager().authenticate(authentication);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)  {
        String token = jwtService.generateJwtToken(((User) authResult.getPrincipal()).getUsername());
        response.addHeader(JwtConstants.HEADER_AUTHORIZATION, JwtConstants.TOKEN_PREFIX + token);
    }
}
