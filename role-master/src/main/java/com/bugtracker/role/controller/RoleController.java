package com.bugtracker.role.controller;

import com.bugtracker.role.entity.Role;
import com.bugtracker.role.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    RoleService roleService;

    @GetMapping("/role/{id}")
    public Role getRoleById(@PathVariable("id") Long roleId) {

        return roleService.getRoleById(roleId);
    }
}
