package com.holiday.touristadviceservice.controller;

import com.holiday.touristadviceservice.entity.AppUser;
import com.holiday.touristadviceservice.repository.AppUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(path = "/users", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
public class UserController {
    private BCryptPasswordEncoder passwordEncoder;
    private AppUserRepository appUserRepository;

    public UserController(BCryptPasswordEncoder passwordEncoder, AppUserRepository appUserRepository) {
        this.passwordEncoder = passwordEncoder;
        this.appUserRepository = appUserRepository;
    }

    @GetMapping("/info")
    public ResponseEntity getUserInfo(@AuthenticationPrincipal Principal principal) {
        Map<Object, Object> model = new HashMap<>();
        model.put("username", principal.getName());
        return ResponseEntity.ok(model);
    }

    @GetMapping("/logout")
    public ResponseEntity logoutInfo() {
        Map<Object, Object> model = new HashMap<>();
        model.put("message", "Logout success!");
        return ResponseEntity.ok(model);
    }

    @PostMapping("/sign-up")
    public ResponseEntity signUp(@RequestBody @Valid AppUser user, BindingResult result) {

        if (result.hasErrors())
            return ResponseEntity.badRequest().build();

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Optional<AppUser> appUser = appUserRepository.findByUsername(user.getUsername());
        if (appUser.isPresent()) {
            Map<String, String> body = new HashMap<>();
            body.put("Message", "An account for that username already exists.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
        } else {
            appUserRepository.save(user);
            return ResponseEntity.ok().build();
        }
    }
}
