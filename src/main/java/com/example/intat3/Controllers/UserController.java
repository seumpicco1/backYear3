package com.example.intat3.Controllers;

import com.example.intat3.Dto.AnnouncementDto;
import com.example.intat3.Dto.UpdateAnnouncementDto;
import com.example.intat3.Dto.UserDTO;
import com.example.intat3.Entity.User;
import com.example.intat3.services.CategoryService;
import com.example.intat3.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "http://25.18.60.149:5173")
@RestController
@RequestMapping("/api/users")
@Validated

public class UserController {

    @Autowired
    private UserService service;

    @GetMapping
    public  List<User> allUser(){
        return  service.getAllUser();
    }

    @PostMapping
    public User createUser(@RequestBody @Valid User user){
        return service.createUser(user);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Integer id, @RequestBody @Valid User user){
        return service.updateUser(id, user);
    }

    @GetMapping("/{id}")
    public User getUserId(@PathVariable Integer id){
        return service.getUserId(id);
    }

    @DeleteMapping ("/{id}")
    public void deleteUser(@PathVariable Integer id){
        service.deleteUser(id);
    }

}
