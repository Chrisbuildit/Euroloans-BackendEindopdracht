package com.euroloans.eindopdracht.controller;

import com.euroloans.eindopdracht.dto.UserDto;
import com.euroloans.eindopdracht.model.Role;
import com.euroloans.eindopdracht.model.User;
import com.euroloans.eindopdracht.repository.RoleRepository;
import com.euroloans.eindopdracht.repository.UserRepository;
import com.euroloans.eindopdracht.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/username")
    public ResponseEntity<Object> getUser(@RequestParam String username) {
        return ResponseEntity.ok(userService.getUser(username));
    }

    @PostMapping("/users")
    public ResponseEntity<String> createUser(@RequestBody UserDto userDto) {
        return new ResponseEntity<>(userService.createUser(userDto), HttpStatus.CREATED);
    }
}
