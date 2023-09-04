package com.example.intat3.Controllers;


import com.example.intat3.Dto.*;
import com.example.intat3.services.AnnouncementService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@CrossOrigin(origins = {"http://25.18.60.149:5173", "http://intproj22.sit.kmutt.ac.th/at3"})
@RestController
@RequestMapping("/api/announcements")

public class AnnouncementController {
    @Autowired
    private AnnouncementService service;

    @GetMapping("")
    public List<AllAnnouncementDto> getAllAnnouncement (@RequestParam(defaultValue = "admin") String mode){
        if(mode.equals("admin")) {
            return service.getAllAnnouncement();
        }else if(mode.equals("active")){
            return service.getAnnByDisplay(mode,0);
        }else{
            return service.getAnnByDisplay(mode,0);
        }
    }
    @GetMapping("/{id}")
    public AnnouncementDto getById(@PathVariable Integer id, @RequestParam(defaultValue = "false") boolean count){
        return service.getAnnouncementById(id, count);
    }

    @PostMapping("")
    public AnnouncementDto createAnnouncement(@Valid @RequestBody UpdateAnnouncementDto ann){
        return service.createAnn(ann);
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

}
