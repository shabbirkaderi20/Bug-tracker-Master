package com.bugtracker.role.service;

import com.bugtracker.role.entity.Role;
import com.bugtracker.role.exception.RoleNotFoundException;
import com.bugtracker.role.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    RoleRepository roleRepository;


    public Role getRoleById(Long roleId) {

        Role roleReturned= roleRepository.findByRoleId(roleId);

        if(roleReturned.getRole().equals("")) {
            throw new RoleNotFoundException();
        }else {
            return roleReturned;
        }
    }
}
