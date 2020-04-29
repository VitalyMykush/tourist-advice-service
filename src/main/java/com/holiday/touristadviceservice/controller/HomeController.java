package com.holiday.touristadviceservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/home")
    public ResponseEntity home() {
        return ResponseEntity.ok().body("THIS SERVER FOR GET AND MODIFY YOU ADVICE");
    }
}
