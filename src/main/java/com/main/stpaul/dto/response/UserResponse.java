package com.main.stpaul.dto.response;

import java.time.LocalDateTime;

import com.main.stpaul.constants.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private String userId;
    private String name;
    private String email;
    private String contact;
    private String loginDate;

    private Role role;

    private boolean isActive;
    private LocalDateTime registerDate;
}
