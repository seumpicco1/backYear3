package com.example.intat3.Controllers;

import com.example.intat3.Config.JwtTokenUtil;
import com.example.intat3.Dto.JwtRequest;
import com.example.intat3.Dto.JwtResponse;
import com.example.intat3.Properties.JwtProperties;
import com.example.intat3.advices.ErrorResponse;
import com.example.intat3.services.JwtUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/token")
public class JwtAuthenticationController {


    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private JwtProperties jwtProperties;


    @PostMapping
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) {
        HttpStatus status = userDetailsService.login(authenticationRequest);
        if(status == HttpStatus.OK){
            final UserDetails userDetails = userDetailsService
                    .loadUserByUsername(authenticationRequest.getUsername());

            final String accessToken = jwtTokenUtil.generateAccessToken(userDetails);
            final String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);

            return ResponseEntity.ok(new JwtResponse(accessToken,refreshToken));
        } else if (status == HttpStatus.UNAUTHORIZED) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"incorrect password or username");
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "incorrect password or username");
        }
    }

    @GetMapping
    public ResponseEntity<?> getAccessTokenByRefreshToken(HttpServletRequest request) throws Exception{
        System.out.println("get");
        String rawRefreshToken = request.getHeader("Authorization");

        String username = null;
        String refreshToken = null;
        if(rawRefreshToken != null && rawRefreshToken.startsWith("Bearer ")){
            refreshToken = rawRefreshToken.substring(7);
            try{
                username = jwtTokenUtil.getUsernameFromToken(refreshToken);
            }catch (IllegalArgumentException e){
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,e.getMessage());
            }
            catch (ExpiredJwtException e){
                System.out.println(e.getMessage());
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"JWT Token has expired");
            }

        }else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        try {
            Claims claims = Jwts.parser().setSigningKey(jwtProperties.getSecretKey()).parseClaimsJws(refreshToken).getBody();
            System.out.println(claims.get("role"));
            if(claims.get("role") .equals("refresh")){
                String newAccessToken = jwtTokenUtil.generateAccessToken(userDetails);

                return ResponseEntity.ok(new JwtResponse(newAccessToken));
            }
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Unable to get JWT Token");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");

    }

    @ExceptionHandler(ResponseStatusException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleValidationException(ResponseStatusException ex, WebRequest request) {
        ErrorResponse er = new ErrorResponse(ex.getStatusCode().value(), "Entity attributes validation failed!", request.getDescription(false));
            er.addValidationError(ex.getBody().getTitle(), ex.getBody().getDetail());
        return ResponseEntity.status(ex.getStatusCode().value()).body(er);
    }

}
