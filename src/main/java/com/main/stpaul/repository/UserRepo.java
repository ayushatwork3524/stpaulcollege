package com.main.stpaul.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.main.stpaul.entities.User;

public interface UserRepo extends JpaRepository<User,String>{

    @Query("SELECT u FROM User u WHERE u.email=:email AND u.isDelete=false")
    User findByEmail(String email);
}
