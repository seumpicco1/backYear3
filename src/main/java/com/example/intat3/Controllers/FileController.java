package com.example.intat3.Controllers;

import com.example.intat3.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/files")
public class FileController {
    @Autowired
    private FileService fileService;

    @PostMapping("/upload/{id}")
    public ResponseEntity uploadFile(@RequestParam List<MultipartFile> file,@PathVariable Integer id){
        System.out.println(id);
        return fileService.uploadFile(file, id);
    }


}
