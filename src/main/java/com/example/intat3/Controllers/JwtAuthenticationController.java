package com.example.intat3.Controllers;

import com.example.intat3.Config.JwtTokenUtil;
import com.example.intat3.Dto.JwtRequest;
import com.example.intat3.Dto.JwtResponse;
import com.example.intat3.Properties.JwtProperties;
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
import org.springframework.web.bind.annotation.*;
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
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
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
            }catch (ExpiredJwtException e){
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"JWT Token has expired");
            }catch (IllegalArgumentException e){
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,e.getMessage());
            }
            System.out.println("check1");
        }else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (jwtTokenUtil.validateToken(refreshToken, userDetails)) {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            usernamePasswordAuthenticationToken
                    .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            System.out.println("check3");
        }try {
            Claims claims = Jwts.parser().setSigningKey(jwtProperties.getSecretKey()).parseClaimsJws(refreshToken).getBody();
            System.out.println(claims.get("role"));
            if(claims.get("role") .equals("refresh")){
                String newAccessToken = jwtTokenUtil.generateAccessToken(userDetails);

                return ResponseEntity.ok(new JwtResponse(newAccessToken));
            }
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Unable to get JWT Token");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized 1");

    }
}
