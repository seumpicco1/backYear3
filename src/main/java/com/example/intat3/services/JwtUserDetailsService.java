package com.example.intat3.services;

import com.example.intat3.Dto.JwtRequest;
import com.example.intat3.Entity.Role;
import com.example.intat3.Entity.User;
import com.example.intat3.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository repository;

    @Autowired
    private Argon2PasswordEncoder passEncode;

    private String rolePrefix = "ROLE_";
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("User not found with username: " + username);
        }else {
            return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(), mapRoleToAuthorities(user.getRole()));
        }
    }

    public Collection<? extends GrantedAuthority> mapRoleToAuthorities(Role role){
        List<GrantedAuthority> listRole = new ArrayList<GrantedAuthority>();
        listRole.add(new SimpleGrantedAuthority("ROLE_"+role.name().toUpperCase()));
        return listRole;
    }

    public HttpStatus login(JwtRequest user) {
        User info = repository.findByUsername(user.getUsername());
        if (info != null) {
            boolean check = passEncode.matches(user.getPassword(), info.getPassword());
            if (!check) {
                return HttpStatus.UNAUTHORIZED;
            }else {
                return HttpStatus.OK;
            }
        } else {
            return HttpStatus.NOT_FOUND;
        }
}

}
