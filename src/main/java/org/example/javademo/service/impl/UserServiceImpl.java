package org.example.javademo.service.impl;

import org.example.javademo.enity.UserEntity;
import org.example.javademo.repositories.IUserEntityRepository;
import org.example.javademo.request.UserRequest;
import org.example.javademo.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserEntityRepository userRepository;

    @Override
    public List<UserEntity> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserEntity addUser(UserRequest request) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName(request.getUserName());
        userEntity.setPassword(request.getPassword());
        userEntity.setEmail(request.getEmail());
        userEntity.setActive(true);
        return userRepository.save(userEntity);
    }

}
