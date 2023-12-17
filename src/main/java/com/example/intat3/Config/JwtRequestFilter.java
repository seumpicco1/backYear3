package com.example.intat3.Config;

import com.example.intat3.services.JwtUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        logger.info(request.getRequestURL().toString());
        logger.info(request.getMethod());
        String requestTokenHeader = request.getHeader("Authorization");
        String username = null;
        String jwtToken = null;
        // JWT Token is in the form "Bearer token". Remove Bearer word and get
        // only the Token
        System.out.println(request.getHeader("Authorization"));
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")&& requestTokenHeader.length() > 15) {
            jwtToken = requestTokenHeader.substring(7);
            try {
//                System.out.println(1);
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
//                System.out.println(2);
            } catch (ExpiredJwtException e) {
                logger.warn("JWT Token has expired");
            }  catch (Exception e) {
                logger.warn("Unable to get JWT Token");
            }

        } else {
            logger.warn("JWT Token does not begin with Bearer String (Token Not Found)");
        }
        // Once we get the token validate it.
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);
            // if token is valid configure Spring Security to manually set
            // authentication
            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }

        }
        chain.doFilter(request, response);

    }

}
