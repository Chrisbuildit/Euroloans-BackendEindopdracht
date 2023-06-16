package com.euroloans.eindopdracht.controller;

import com.euroloans.eindopdracht.dto.RoleDto;
import com.euroloans.eindopdracht.model.Role;
import com.euroloans.eindopdracht.repository.RoleRepository;
import com.euroloans.eindopdracht.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/roles")
    public ResponseEntity<Object> getRoles() {
        return ResponseEntity.ok(roleService.getRoles());
    }

}
