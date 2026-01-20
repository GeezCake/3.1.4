package ru.kata.spring.boot_security.demo.service;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

@Service
public class UserServiceImpl implements UserService {

    private static final String DEFAULT_USER_ROLE = "ROLE_USER";

    private final UserDao userDao;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserDao userDao,
                           RoleService roleService,
                           PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void createUser(User user) {
        normalizeUsername(user);
        applyDefaultRoles(user);
        encodePasswordIfNeeded(user);
        userDao.addUser(user);
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        Objects.requireNonNull(user, "User must not be null");

        User existing = user.getId() == null ? null : userDao.getUserById(user.getId());

        normalizeUsername(user);

        if (existing != null) {
            preservePasswordIfBlank(user, existing);
            preserveRolesIfEmpty(user, existing);
        }

        encodePasswordIfNeeded(user);
        userDao.updateUser(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        userDao.removeUser(id);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userDao.getUserById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        return userDao.getUserByUsername(username);
    }

    private void normalizeUsername(User user) {
        if (user == null) {
            return;
        }

        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            user.setUsername(user.getEmail());
        }
    }

    private void applyDefaultRoles(User user) {
        if (user == null) {
            return;
        }

        Set<Role> roles = user.getRoles();
        if (roles != null && !roles.isEmpty()) {
            return;
        }

        Role defaultRole = roleService.getRoleByName(DEFAULT_USER_ROLE);
        if (defaultRole == null) {
            roleService.createRole(new Role(DEFAULT_USER_ROLE));
            defaultRole = roleService.getRoleByName(DEFAULT_USER_ROLE);
        }

        if (defaultRole != null) {
            user.getRoles().add(defaultRole);
        }
    }

    private void preservePasswordIfBlank(User user, User existing) {
        String rawPassword = user.getPassword();
        if (rawPassword == null || rawPassword.trim().isEmpty()) {
            user.setPassword(existing.getPassword());
        }
    }

    private void preserveRolesIfEmpty(User user, User existing) {
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(existing.getRoles());
        }
    }

    private void encodePasswordIfNeeded(User user) {
        if (user == null) {
            return;
        }

        String password = user.getPassword();
        if (password == null || password.trim().isEmpty()) {
            return;
        }

        if (!isBCryptHash(password)) {
            user.setPassword(passwordEncoder.encode(password));
        }
    }

    private boolean isBCryptHash(String value) {
        return value != null && value.matches("^\\$2[aby]\\$.*");
    }
}
