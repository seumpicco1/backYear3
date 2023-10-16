package com.example.intat3.Entity;

import com.example.intat3.Converter.StringConverter;
import com.example.intat3.validation.PasswordValid;
import com.example.intat3.validation.UniqueValid;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.*;

import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "userId", nullable = false)
    private Integer id;

    @NotNull(message = "must not be null") @NotBlank(message = "must not be blank") @Size(min = 1, max = 45 ,message = "size must be between 1 and 45")
    @Column(name= "username", nullable = false, unique = true)
    @Convert(converter = StringConverter.class)
    @UniqueValid(usernameCheck = true, message = "does not unique")
    private String username;

    @Convert(converter = StringConverter.class)
    @Column(name="password", nullable = false)
    @PasswordValid
    private String password;

    @NotNull(message = "must not be null") @NotBlank(message = "must not be blank") @Size(min = 1, max = 100 ,message = "size must be between 1 and 100")
    @Column(name= "name", nullable = false, unique = true)
    @Convert(converter = StringConverter.class)
    @UniqueValid(nameCheck = true, message = "does not unique")
    private String name;


    @NotNull(message = "must not be null") @NotBlank(message = "must not be blank") @Size(min = 1, max = 150 ,message = "size must be between 1 and 150")
    @Column(name= "email", nullable = false, unique = true)
    @Convert(converter = StringConverter.class)
    @Email(message = "Email should be valid")
    @UniqueValid(message = "does not unique")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "role" , nullable = false)
    private  Role role;

    @Column(name = "createdOn" ,insertable = false, updatable = false)
    private ZonedDateTime createdOn;

    @Column(name = "updatedOn" ,insertable = false, updatable = false)
    private ZonedDateTime updatedOn;

    @JsonIgnore
    @OneToMany(mappedBy = "announcementOwner", cascade = CascadeType.ALL)
    private List<Announcement> announcementsList;

}
