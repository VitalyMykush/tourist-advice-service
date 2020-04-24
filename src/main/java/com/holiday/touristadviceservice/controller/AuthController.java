package com.holiday.touristadviceservice.controller;

import com.holiday.touristadviceservice.entity.AppUser;
import com.holiday.touristadviceservice.repository.AppUserRepository;
import com.holiday.touristadviceservice.security.jwt.JwtTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping(path = "/auth", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
public class AuthController {

    private BCryptPasswordEncoder passwordEncoder;
    private AppUserRepository appUserRepository;
    private JwtTokenService jwtTokenService;
    private final String TOKEN_PREFIX = "Bearer ";

    @Autowired
    public AuthController(BCryptPasswordEncoder passwordEncoder, AppUserRepository appUserRepository, JwtTokenService jwtTokenService) {
        this.passwordEncoder = passwordEncoder;
        this.appUserRepository = appUserRepository;
        this.jwtTokenService = jwtTokenService;
    }

    @PostMapping("/signin")
    public ResponseEntity signin(@RequestBody Map<String, String> body) {
    //public ResponseEntity signin(@RequestParam("username") String username, @RequestParam("password") String password) {
        String username = body.get("username");
        String password = body.get("password");

        AppUser appUser = appUserRepository.findByUsername(body.get("username"))
                .orElseThrow(
                        () -> new UsernameNotFoundException("User " + username + " was not found in the database"));
        boolean validPassword = passwordEncoder.matches(password,appUser.getEncrytedPassword());
        if (validPassword) {
            String token = jwtTokenService.generateJwtToken(username);
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(username,password,new ArrayList<>()));
            return ResponseEntity.ok().header("Authorization", TOKEN_PREFIX + token).build();
        } else {
            throw new BadCredentialsException("Invalid username/password supplied");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        if(username != null && password != null) {
            appUserRepository.save(new AppUser(username, passwordEncoder.encode(password)));
            String token = jwtTokenService.generateJwtToken(username);
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(username,password,new ArrayList<>()));
            return ResponseEntity.ok().header("Authorization", TOKEN_PREFIX + token).build();
        }else {
            throw new BadCredentialsException("Invalid username/password supplied");
        }

    }

}
