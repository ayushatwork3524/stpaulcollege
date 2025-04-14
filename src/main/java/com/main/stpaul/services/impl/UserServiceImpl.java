package com.main.stpaul.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.main.stpaul.entities.User;
import com.main.stpaul.jwtSecurity.JwtProvider;
import com.main.stpaul.repository.UserRepo;
import com.main.stpaul.services.serviceInterface.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public User addUser(User user) {
        String id = UUID.randomUUID().toString();
        user.setUserId(id);
        return this.userRepo.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return this.userRepo.findAll();
    }

    @Override
    public User getUserByEmail(String email) {
        return this.userRepo.findByEmail(email);
    }

    @Override
    public User getUserById(String id) {
        return this.userRepo.findById(id).get();
    }

    @Override
    public User getUserByJwt(String jwt) {
        String email = JwtProvider.getEmailFromJwt(jwt);
        return this.userRepo.findByEmail(email);
    }

    @Override
    public User updateUser(User user) {
        return this.userRepo.save(user);
    }
    
}
