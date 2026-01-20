package ru.kata.spring.boot_security.demo.service;

import java.util.List;

import ru.kata.spring.boot_security.demo.model.Role;

public interface RoleService {

    void createRole(Role role);

    void updateRole(Role role);

    void deleteRole(Long id);

    Role getRoleById(Long id);

    List<Role> getAllRoles();

    Role getRoleByName(String name);
}
