package ru.kata.spring.boot_security.demo.service;

import java.util.List;

import ru.kata.spring.boot_security.demo.model.User;

public interface UserService {

    void createUser(User user);

    void updateUser(User user);

    void deleteUser(Long id);

    User getUserById(Long id);

    List<User> getAllUsers();

    User getUserByUsername(String username);
}

