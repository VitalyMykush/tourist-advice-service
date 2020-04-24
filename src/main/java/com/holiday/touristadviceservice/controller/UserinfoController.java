package com.holiday.touristadviceservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UserinfoController {

    @GetMapping("/me")
    public ResponseEntity getUserinfo(@AuthenticationPrincipal Principal principal) {
        Map<Object, Object> model = new HashMap<>();
        model.put("username", principal.getName());
        return ResponseEntity.ok(model);
    }
}
