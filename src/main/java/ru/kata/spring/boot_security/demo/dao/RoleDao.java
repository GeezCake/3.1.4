package ru.kata.spring.boot_security.demo.dao;

import java.util.List;

import ru.kata.spring.boot_security.demo.model.Role;

public interface RoleDao {

    void addRole(Role role);

    void updateRole(Role role);

    void removeRole(Long id);

    Role getRoleById(Long id);

    List<Role> getAllRoles();

    Role getRoleByName(String name);
}
