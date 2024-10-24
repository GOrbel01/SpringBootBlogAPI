package org.fsq.blogapi.controller;

import org.fsq.blogapi.model.User;
import org.fsq.blogapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/users",produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping
    public void addUser(@RequestBody User user) {
        user.setPasswordHash(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @GetMapping
    public List<User> getUsers() {
        List<User> users = userRepository.findAll();
        users.forEach(user -> {
            user.getBlogs().forEach(blog -> {
                blog.setUser(null);
            });
        });
        return users;
    }
}
