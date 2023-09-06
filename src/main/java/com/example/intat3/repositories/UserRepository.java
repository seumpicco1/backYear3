package com.example.intat3.repositories;

import com.example.intat3.Entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface UserRepository extends CustomUserRepository<User, Integer> {
    List<User> getAllByOrderByRoleAscUsernameAsc();
    User findByUsername(String username);
    User findByName(String name);
    User findByEmail(String email);

    boolean existsByUsername(String username);
    boolean existsByName(String name);
    boolean existsByEmail(String email);
}
