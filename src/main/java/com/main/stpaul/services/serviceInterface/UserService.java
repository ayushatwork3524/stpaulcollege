package com.main.stpaul.services.serviceInterface;
import java.util.List;

import com.main.stpaul.entities.User;

public interface UserService {
    User addUser(User user);
    List<User> getAllUsers();
    User getUserByEmail(String email);
    User getUserById(String id);
    User getUserByJwt(String jwt); 
    User updateUser(User user);  
}
