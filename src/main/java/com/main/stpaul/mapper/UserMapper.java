package com.main.stpaul.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.main.stpaul.dto.request.RegisterRequest;
import com.main.stpaul.dto.response.UserResponse;
import com.main.stpaul.entities.User;

@Component
public class UserMapper {
    
    @Autowired
    private ModelMapper modelMapper;

    public UserResponse toUserResponse(User user){
        return this.modelMapper.map(user, UserResponse.class);
    }

    public User toUser(RegisterRequest user){
        return this.modelMapper.map(user, User.class);
    }
}
