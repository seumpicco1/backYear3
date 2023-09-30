package com.example.intat3.Dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JwtResponse {
    @NonNull
    private String token;
    private String refreshToken;
}
