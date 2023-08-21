package com.euroloans.eindopdracht.controller;

import com.euroloans.eindopdracht.dto.AuthDto;

import com.euroloans.eindopdracht.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    //Self gemaak
    @PostMapping("/auth")
    public ResponseEntity<Object> signIn(@RequestBody AuthDto authDto) {
        return ResponseEntity.ok(authService.signIn(authDto));
    }
}
