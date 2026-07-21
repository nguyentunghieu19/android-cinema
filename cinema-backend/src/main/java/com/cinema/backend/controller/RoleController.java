package com.cinema.backend.controller;

import com.cinema.backend.entity.Role;
import com.cinema.backend.service.RoleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/api/roles")
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }
}