package org.example.javademo.service;

import org.example.javademo.enity.UserEntity;
import org.example.javademo.request.UserRequest;

import java.util.List;

public interface IUserService {

    List<UserEntity> getUsers();

    UserEntity addUser(UserRequest userEntity);
}
