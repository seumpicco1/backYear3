package com.example.intat3.Dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import java.time.ZonedDateTime;

@Getter
@Setter

public class UserDTO {
//     private Integer id;
    private String username;
    private String name;
    private String email;
    private String role;
//     private ZonedDateTime createOn;
//     private ZonedDateTime updateOn;

}
