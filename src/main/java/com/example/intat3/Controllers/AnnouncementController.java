package com.example.intat3.Controllers;


import com.example.intat3.Dto.*;
import com.example.intat3.Entity.File;
import com.example.intat3.services.AnnouncementService;
import com.example.intat3.services.FileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


@CrossOrigin
@RestController
@RequestMapping("/api/announcements")

public class AnnouncementController {
    @Autowired
    private AnnouncementService service;

    @Autowired
    private FileService fileService;

    @GetMapping
    public List<AllAnnouncementDto> getAllAnnouncement ( @RequestParam(required=false) String mode){
        boolean isAdmin = isAdminChecker();
        String username = getUsernameUser();

        if(isAdmin) {
            return service.getAllAnnouncement();
        }else if(mode!=null){
            return service.getAnnByDisplay(mode,0);
        }else {
            return service.getAllAnnouncementByUser(username);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getById(@PathVariable Integer id, @RequestParam(defaultValue = "false") boolean count){
        String username = getUsernameUser();
        AnnouncementDto ann = service.getAnnouncementById(id, count, username);
//        ByteArrayResource resource = downloadFile(id);
        List<FileDto> files = fileService.downloadFile(id);
        Map<String,Object> body = new HashMap<>();
        body.put("announcement",ann);
        body.put("file",files);
        return ResponseEntity.ok().body(body);

    }

    @PostMapping
    public AnnouncementDto createAnnouncement(@Valid @RequestBody UpdateAnnouncementDto ann){
        String username = getUsernameUser();
        AnnouncementDto newAnn = service.createAnn(ann, username);
        return newAnn;
    }

    @DeleteMapping ("/{id}")
    public void deleteOffice(@PathVariable Integer id){
        String username = getUsernameUser();
        service.deleteAnn(id, username);
    }

    @PutMapping("/{id}")
        public UpdateDTO updateProduct(@PathVariable  Integer id, @Valid @RequestBody UpdateAnnouncementDto ann) {
        String username = getUsernameUser();
        UpdateDTO newAnn = service.updateAnn( id, ann, username);
        fileService.deleteFileByAnnouncement(id);
        return newAnn;
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
