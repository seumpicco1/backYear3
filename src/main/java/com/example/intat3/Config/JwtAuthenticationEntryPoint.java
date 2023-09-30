package com.example.intat3.Config;

import com.example.intat3.advices.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Serializable;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ErrorResponse body = new ErrorResponse(HttpServletResponse.SC_UNAUTHORIZED,"Authentication","request access");
        body.addValidationError("unauthorized","authentication failed");
        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);
//        response.sendError(HttpServletResponse.SC_CONFLICT, "Unauthorized");
    }

}
