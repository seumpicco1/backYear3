package com.example.intat3.Controllers;

import com.example.intat3.Dto.InfoUserDTO;
import com.example.intat3.Dto.OnlyUserDTO;
import com.example.intat3.Dto.UpdateUserDTO;
import com.example.intat3.Entity.User;
import com.example.intat3.Exception.UserException;
import com.example.intat3.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@CrossOrigin(origins = {"http://25.18.60.149:5173", "http://intproj22.sit.kmutt.ac.th/at3"})
@RestController
@RequestMapping("/api/users")

public class UserController {

    @Autowired
    private UserService service;

    @GetMapping
    public  List<InfoUserDTO> allUser(){
        return  service.getAllUser();
    }

    @PostMapping("/match")
    public ResponseEntity<String> passwordMatchChecker(@RequestBody OnlyUserDTO user){
        try{
            service.passwordChecker(user);
            throw new ResponseStatusException(HttpStatus.OK,"Password Matched");
        }catch (UserException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,e.getMessage());
        }
    }

    @PostMapping
    public InfoUserDTO createUser(@RequestBody @Valid User user){
            InfoUserDTO success =  service.createUser(user);
            return success;
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Integer id, @RequestBody @Valid UpdateUserDTO user){
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

