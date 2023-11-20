package com.example.intat3.Controllers;


import com.example.intat3.Dto.*;
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
    public ResponseEntity<MultiValueMap<String, Object>> getById(@PathVariable Integer id, @RequestParam(defaultValue = "false") boolean count){
        String username = getUsernameUser();
        AnnouncementDto ann = service.getAnnouncementById(id, count, username);
        ByteArrayResource resource = downloadFile(id);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"files.zip\"");
        headers.setContentType(MediaType.MULTIPART_MIXED);

        MultiValueMap<String,Object> body = new LinkedMultiValueMap();
        body.add("Json",ann);
        body.add("file",resource);

        return ResponseEntity.ok()
                .headers(headers)
                .body(body);
    }

    @PostMapping
    public AnnouncementDto createAnnouncement(@Valid @RequestBody UpdateAnnouncementDto ann, @RequestBody(required = false) MultipartFile[] file){
        String username = getUsernameUser();
        AnnouncementDto newAnn = service.createAnn(ann, username);
        if(file!=null){
            fileService.uploadFile(file,newAnn.getId());
        }
        return newAnn;
    }

    @DeleteMapping ("/{id}")
    public void deleteOffice(@PathVariable Integer id){
        String username = getUsernameUser();
        service.deleteAnn(id, username);
    }

    @PutMapping("/{id}")
        public UpdateDTO updateProduct(@PathVariable  Integer id, @Valid @RequestBody UpdateAnnouncementDto ann, @RequestBody(required = false) MultipartFile[] file) {
        String username = getUsernameUser();
        UpdateDTO newAnn = service.updateAnn( id, ann, username);
        if(file!=null){
            fileService.deleteFileByAnnouncement(id);
            fileService.uploadFile(file, id);
        }
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

    public ByteArrayResource downloadFile(Integer annId){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(baos)){
            Map<String,byte[]> dataBytes = fileService.downloadFile(annId);
            for(Map.Entry<String,byte[]> data : dataBytes.entrySet()){
                ZipEntry zipEntry = new ZipEntry(data.getKey());
                zipOutputStream.putNextEntry(zipEntry);
                zipOutputStream.write(data.getValue());
                zipOutputStream.closeEntry();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ByteArrayResource resource = new ByteArrayResource(baos.toByteArray());
//        return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM)
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"multiple_files.zip\"")
//                .body(Resource);
        return resource;
    }
}
