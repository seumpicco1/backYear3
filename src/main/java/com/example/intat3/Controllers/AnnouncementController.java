package com.example.intat3.Controllers;


import com.example.intat3.Dto.*;
import com.example.intat3.services.AnnouncementService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@CrossOrigin
@RestController
@RequestMapping("/api/announcements")

public class AnnouncementController {
    @Autowired
    private AnnouncementService service;

    @GetMapping
    public List<AllAnnouncementDto> getAllAnnouncement (){
        boolean isAdmin = isAdminChecker();
        String username = getUsernameUser();
        if(isAdmin) {
            return service.getAllAnnouncement();
        }else {
            return service.getAllAnnouncementByUser(username);
        }
    }

    @GetMapping("/{id}")
    public AnnouncementDto getById(@PathVariable Integer id, @RequestParam(defaultValue = "false") boolean count){
        String username = getUsernameUser();
        return service.getAnnouncementById(id, count, username);
    }

    @PostMapping
    public AnnouncementDto createAnnouncement(@Valid @RequestBody UpdateAnnouncementDto ann){
        String username = getUsernameUser();
        return service.createAnn(ann, username);
    }

    @DeleteMapping ("/{id}")
    public void deleteOffice(@PathVariable Integer id){
        service.deleteAnn(id);
    }

    @PutMapping("/{id}")
        public UpdateDTO updateProduct(@PathVariable  Integer id, @Valid @RequestBody UpdateAnnouncementDto ann) {
        return service.updateAnn( id, ann);
    }

    @GetMapping("/pages")
    public PageDTO<AllAnnouncementDto> getAllPageAnn(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size,
        @RequestParam(defaultValue = "0") int category,
        @RequestParam(defaultValue = "active") String mode){
        return service.getAllPageAnn(page, size, mode ,category);
    }

    public boolean isAdminChecker(){
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().anyMatch(role -> role.equals("ROLE_ADMIN"));
    }

    public String getUsernameUser(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
