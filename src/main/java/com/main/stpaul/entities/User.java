package com.main.stpaul.entities;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.main.stpaul.constants.Role;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
public class User implements UserDetails{
    @Id
    private String userId;
    private String name;
    private String email;
    private String password;
    private String contact;
    private LocalDateTime loginDate;

    @Enumerated(EnumType.STRING)
    private Role role=Role.MANAGER;

    private boolean isDelete = false;
    private boolean isActive = true;
    
    private LocalDateTime registerDate = LocalDateTime.now();
    private LocalDateTime updatedDate = LocalDateTime.now();
    private LocalDateTime deleteDate;


    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Session> sessions;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<CollegeFees> collegeFees;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_"+this.role.toString()));
    }
    @Override
    public String getUsername() {
        return this.email;
    }
}
