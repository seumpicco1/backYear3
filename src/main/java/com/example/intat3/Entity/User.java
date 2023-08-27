package com.example.intat3.Entity;

import com.example.intat3.Converter.StringConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
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
    @Column(name= "username", nullable = false)
    @Convert(converter = StringConverter.class)
    private String username;

    @NotNull(message = "must not be null") @NotBlank(message = "must not be blank") @Size(min = 1, max = 100 ,message = "size must be between 1 and 100")
    @Column(name= "name", nullable = false)
    @Convert(converter = StringConverter.class)
    private String name;

    @NotNull(message = "must not be null") @NotBlank(message = "must not be blank") @Size(min = 1, max = 150 ,message = "size must be between 1 and 150")
    @Column(name= "email", nullable = false)
    @Convert(converter = StringConverter.class)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "role" , nullable = false)
    private  Role role;

    @Column(name = "createdOn" ,insertable = false, updatable = false)
    private ZonedDateTime createdOn;

    @Column(name = "updatedOn" ,insertable = false, updatable = false)
    private ZonedDateTime updatedOn;

}
