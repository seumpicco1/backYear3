package com.example.intat3.Controllers;

import com.example.intat3.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.intat3.Entity.Category;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    private CategoryService service;

    @GetMapping
    public List<Category> getAllCategory (){
        return  service.getAllCategory();
    }
    
    @GetMapping("/{id}")
    public Category getCategory(@PathVariable int id){ return service.getCategory(id); }
}
