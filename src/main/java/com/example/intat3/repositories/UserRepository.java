package com.example.intat3.repositories;

import com.example.intat3.Entity.Announcement;
import com.example.intat3.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserRepository extends CustomUserRepository<User, Integer> {
    List<User> getAllByOrderByRoleAscUsernameAsc();
}
