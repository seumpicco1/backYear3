package com.example.intat3.services;

import com.example.intat3.Dto.AnnouncementDto;
import com.example.intat3.Dto.UpdateAnnouncementDto;
import com.example.intat3.Dto.UserDTO;
import com.example.intat3.Entity.Announcement;
import com.example.intat3.Entity.Category;
import com.example.intat3.Entity.User;
import com.example.intat3.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    public List<User> getAllUser(){
        return repository.getAllByOrderByRoleAscUsernameAsc();
    }


    public  User getUserId (Integer userId){
                User a = repository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "User id " + userId +  " " + "does not exist !!!"));
                return  a ;

    }

    public User getUser(Integer id){
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,"Users id " + id +  " " + "does not exist !!!"));
    }

    public User createUser (User user){
        User result = repository.saveAndFlush(user);
        repository.refresh(result);
        return result;
    }

    public User updateUser (int id, User newInfoUser) {
        User oldInfoUser = repository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,
        "User "+id+"does not exist!!"));
        oldInfoUser.setUsername(newInfoUser.getUsername());
        oldInfoUser.setName(newInfoUser.getName());
        oldInfoUser.setEmail(newInfoUser.getEmail());
        oldInfoUser.setRole(newInfoUser.getRole());
        User updatedUser = repository.saveAndFlush(oldInfoUser);
        repository.refresh(updatedUser);
        return updatedUser;
    }

    public void deleteUser(int id){
            User u = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"User id " + id +  " " + "does not exist !!!"));
            repository.delete(u);
        }
}
