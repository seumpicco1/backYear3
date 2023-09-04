package com.example.intat3.repositories;

import com.example.intat3.Entity.User;
import java.util.List;

public interface UserRepository extends CustomUserRepository<User, Integer> {
    List<User> getAllByOrderByRoleAscUsernameAsc();
    User findByUsername(String username);
}
