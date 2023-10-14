package com.example.intat3.Dto;

import com.example.intat3.Converter.StringConverter;
import com.example.intat3.Entity.Role;
import com.example.intat3.validation.UniqueValid;
import jakarta.persistence.Convert;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UpdateUserDTO {
    @NotNull(message = "must not be null") @NotBlank(message = "must not be blank") @Size(min = 1, max = 45 ,message = "size must be between 1 and 45")
    @Convert(converter = StringConverter.class)
    @UniqueValid(usernameCheck = true, message = "does not unique")
    private String username;
    @NotNull(message = "must not be null") @NotBlank(message = "must not be blank") @Size(min = 1, max = 100 ,message = "size must be between 1 and 100")
    @Convert(converter = StringConverter.class)
    @UniqueValid(nameCheck = true, message = "does not unique")
    private String name;
    @NotNull(message = "must not be null") @NotBlank(message = "must not be blank") @Size(min = 1, max = 150 ,message = "size must be between 1 and 150")
    @Convert(converter = StringConverter.class)
    @Email(message = "Email should be valid")
    @UniqueValid(message = "does not unique")
    private String email;

    private Role role;
}
