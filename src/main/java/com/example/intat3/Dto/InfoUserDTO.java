package com.example.intat3.Dto;

import com.example.intat3.Entity.Role;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class InfoUserDTO {
    private Integer id;
    private String username;
    private String name;
    private String email;
    private Role role;
    private ZonedDateTime createdOn;
    private ZonedDateTime updatedOn;
}
