package com.example.intat3.Dto;

import com.example.intat3.Entity.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UpdateUserDTO {
    private String username;
    private String name;
    private String email;
    private Role role;
}
