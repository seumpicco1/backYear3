package com.example.intat3.Controllers;


import com.example.intat3.Dto.*;
import com.example.intat3.Entity.Announcement;
import com.example.intat3.advices.ErrorResponse;
import com.example.intat3.services.AnnouncementService;
import com.example.intat3.validation.ViewValid;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://25.18.60.149:5173")
@RestController
@RequestMapping("/api/announcements")

public class AnnouncementController {
    @Autowired
    private AnnouncementService service;
    @Autowired
    private ModelMapper modelMapper;
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex, WebRequest request) {
        ErrorResponse er = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Announcement attributes validation failed!", request.getDescription(false));
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            er.addValidationError(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(er);
    }
}
