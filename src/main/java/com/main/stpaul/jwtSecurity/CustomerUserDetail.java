package com.main.stpaul.jwtSecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.main.stpaul.entities.User;
import com.main.stpaul.repository.UserRepo;

@Component
public class CustomerUserDetail implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepo.findByEmail(username);
        if (user == null) {
            throw new BadCredentialsException("User Not found");
        }
        return org.springframework.security.core.userdetails.User.builder().username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().toString())
                .authorities(user.getAuthorities()).build();
    }

}
