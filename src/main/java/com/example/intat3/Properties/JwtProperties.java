package com.example.intat3.Properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
@Getter@Setter
public class JwtProperties {
    private String secretKey;
    private Long refreshTokenIntervalInHour;
    private Long accessTokenIntervalInMinute;
}
