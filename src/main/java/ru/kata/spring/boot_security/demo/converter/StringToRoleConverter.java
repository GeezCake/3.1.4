package ru.kata.spring.boot_security.demo.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.service.RoleService;

@Component
public class StringToRoleConverter implements Converter<String, Role> {

    private final RoleService roleService;

    public StringToRoleConverter(RoleService roleService) {
        this.roleService = roleService;
    }

    @Override
    public Role convert(String source) {
        if (source == null) {
            return null;
        }

        String value = source.trim();
        if (value.isEmpty()) {
            return null;
        }

        try {
            Long id = Long.parseLong(value);
            return roleService.getRoleById(id);
        } catch (NumberFormatException e) {
            return roleService.getRoleByName(value);
        }
    }
}
