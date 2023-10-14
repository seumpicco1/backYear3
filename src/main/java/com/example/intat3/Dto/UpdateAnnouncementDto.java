package com.example.intat3.Dto;
import com.example.intat3.validation.DateValid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import java.time.ZonedDateTime;

@Getter
@Setter
@Validated
@DateValid
public class UpdateAnnouncementDto {
    private  Integer id;
    @NotNull(message = "must not be null") @NotBlank(message = "must not be blank") @Size(min = 1, max = 200 ,message = "size must be between 1 and 200")
    private String announcementTitle;
    @NotNull(message = "must not be null") @NotBlank(message = "must not be blank") @Size(min = 1, max = 10000 ,message = "size must be between 1 and 10000")
    private String announcementDescription;

    @Future(message = "must be a date in the present or in the future")
    private ZonedDateTime publishDate;

    @Future(message = "must be a future date")
    private ZonedDateTime closeDate;

    @Pattern(regexp = "[YN]", message = "must be either 'Y' or 'N'")
    private String announcementDisplay ;

    @NotNull(message = "must not be null") @Min(value = 1, message = "does not exists") @Max(value = 4, message = "does not exists")
    private Integer categoryId;

    @Min(value = 0, message = "must be a positive value")
    private Integer viewer;


}
