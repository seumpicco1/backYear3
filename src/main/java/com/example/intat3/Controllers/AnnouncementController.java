package com.example.intat3.Controllers;


import com.example.intat3.Dto.*;
import com.example.intat3.services.AnnouncementService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public List<AllAnnouncementDto> getAllAnnouncement ( @RequestParam(required=false) String mode){
        boolean isAdmin = isAdminChecker();
        String username = getUsernameUser();
//        try {
//
//        }catch (){
//
//        }
        if(isAdmin) {
            return service.getAllAnnouncement();
        }else if(mode!=null){
            return service.getAnnByDisplay(mode,0);
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
        String username = getUsernameUser();
        service.deleteAnn(id, username);
    }

    @PutMapping("/{id}")
        public UpdateDTO updateProduct(@PathVariable  Integer id, @Valid @RequestBody UpdateAnnouncementDto ann) {
        String username = getUsernameUser();
        return service.updateAnn( id, ann, username);
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
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().anyMatch(role -> role.toString().equals("ROLE_ADMIN"));
    }

    public String getUsernameUser(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
