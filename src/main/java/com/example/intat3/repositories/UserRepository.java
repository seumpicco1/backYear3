package com.example.intat3.repositories;

import com.example.intat3.Entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends CustomUserRepository<User, Integer> {
    List<User> getAllByOrderByRoleAscUsernameAsc();
    User findByUsername(String username);

    boolean existsByUsername(String username);
    boolean existsByName(String name);
    boolean existsByEmail(String email);

//    User findUserByUsername(String username);

    @Query("SELECT CASE WHEN COUNT(u) > 1 THEN true ELSE false END FROM User u WHERE u.id = :userId OR u.username = :username")
    boolean doesUserExistByUsername(@Param("userId") Integer userId, @Param("username") String username);
    @Query("SELECT CASE WHEN COUNT(u) > 1 THEN true ELSE false END FROM User u WHERE u.id = :userId OR u.name = :name")
    boolean doesUserExistByName(@Param("userId") Integer userId, @Param("name") String name);
    @Query("SELECT CASE WHEN COUNT(u) > 1 THEN true ELSE false END FROM User u WHERE u.id = :userId OR u.email = :email")
    boolean doesUserExistByEmail(@Param("userId") Integer userId, @Param("email") String email);
}
