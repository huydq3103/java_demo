package org.example.javademo.controller;

import org.example.javademo.enity.UserEntity;
import org.example.javademo.request.UserRequest;
import org.example.javademo.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserService userService;

    @GetMapping("/get-all")
    public List<UserEntity> getAllUsers() {
        return userService.getUsers();
    }

    @PostMapping("/add")
    public UserEntity addUser(@RequestBody UserRequest user) {
        return userService.addUser(user);
    }


}
