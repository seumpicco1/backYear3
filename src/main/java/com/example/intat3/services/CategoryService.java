package com.example.intat3.services;

import com.example.intat3.Entity.Category;
import com.example.intat3.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public Category getCategory(int id) {
        return categoryRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category id " + id + "Does Not Exist !!!"));
    }

    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }
}