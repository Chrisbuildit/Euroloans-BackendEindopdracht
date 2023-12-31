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

    @GetMapping("/users/{id}")
    public ResponseEntity<Object> getUser(@PathVariable String id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody UserDto userDto) {
        return new ResponseEntity<>(userService.createUser(userDto), HttpStatus.CREATED);
    }
}
