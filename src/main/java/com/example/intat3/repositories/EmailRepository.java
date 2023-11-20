package com.example.intat3.repositories;

import com.example.intat3.Entity.EmailAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmailRepository extends JpaRepository<EmailAddress,Integer> {
    EmailAddress findByEmail(String emailAddress);

    @Query("SELECT e FROM EmailAddress e JOIN Category c WHERE c.categoryId = :id")
    List<EmailAddress> findByCategoryId(Integer id);

    @Query("SELECT e FROM EmailAddress e JOIN Category c WHERE c.categoryId = :id AND e.email = :email")
    EmailAddress findByCategoryIdAndEmail(Integer id, String email);
}
