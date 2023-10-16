package com.example.intat3.services;


import com.example.intat3.Dto.InfoUserDTO;
import com.example.intat3.Dto.OnlyUserDTO;
import com.example.intat3.Dto.UpdateUserDTO;
import com.example.intat3.Entity.User;
import com.example.intat3.Exception.UserException;
import com.example.intat3.repositories.AnnouncementRepository;
import com.example.intat3.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    @Autowired
    private AnnouncementRepository announcementRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private Argon2PasswordEncoder passEncode;

    public List<InfoUserDTO> getAllUser() {
        List<User> list = repository.getAllByOrderByRoleAscUsernameAsc();
        return list.stream().map(x -> modelMapper.map(x, InfoUserDTO.class)).collect(Collectors.toList());
    }

    public InfoUserDTO getUserId(Integer id) {
        User user =  repository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Users id " + id + " " + "does not exist !!!"));
        return modelMapper.map(user, InfoUserDTO.class);
    }

    public InfoUserDTO createUser(User user) {
        String password = user.getPassword();
        String encodedPassword = passEncode.encode(password);
        System.out.println(encodedPassword);
        user.setPassword(encodedPassword);
        User result = repository.saveAndFlush(user);
        repository.refresh(result);
        return modelMapper.map(result, InfoUserDTO.class);
    }

    public boolean passwordChecker(OnlyUserDTO user) throws UserException {
        User info = repository.findByUsername(user.getUsername());
        if (info != null) {
            boolean check = passEncode.matches(user.getPassword(), info.getPassword());
            if (!check) {
                throw new UserException("Password NOT Matched");
            }
            return check;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Username " + user.getUsername() + " does not exist !!!");
        }
    }

    public InfoUserDTO updateUser(int id, UpdateUserDTO newInfoUser) {
        User oldInfoUser = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "User " + id + " does not exist!!"));
        oldInfoUser.setUsername(newInfoUser.getUsername());
        oldInfoUser.setName(newInfoUser.getName());
        oldInfoUser.setEmail(newInfoUser.getEmail());
        oldInfoUser.setRole(newInfoUser.getRole());
        User updatedUser = repository.saveAndFlush(oldInfoUser);
        repository.refresh(updatedUser);
        return modelMapper.map(updatedUser, InfoUserDTO.class);
    }

    public void deleteUser(String username,int targetId) {
        System.out.println(username);
        User admin = repository.findByUsername(username);
        User u = repository.findById(targetId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User id " + targetId + " " + "does not exist !!!"));
        System.out.println(u.getUsername());
        if(!admin.getUsername().equals(u.getUsername())){
            announcementRepository.updateAnnouncementFromAnnouncerToAdmin(admin,u);
            repository.delete(u);
        }else{
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Forbidden");
        }

    }

}
