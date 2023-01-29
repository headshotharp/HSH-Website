package de.headshotharp.web.server.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import de.headshotharp.web.database.Role;
import de.headshotharp.web.server.jpa.RoleRepository;

@RestController
public class PermissionsController {

    @Autowired
    private RoleRepository roleRepo;

    @GetMapping("/roles")
    public List<Role> getRoles() {
        return roleRepo.findAll();
    }
}
