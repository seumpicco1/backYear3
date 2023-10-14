package com.example.intat3.repositories;

import com.example.intat3.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CategoryRepository  extends JpaRepository<Category,Integer>  {
}
