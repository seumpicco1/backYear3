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
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/api/files")
public class FileController {
    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public String uploadFile(@RequestBody MultipartFile[] file){
        return fileService.uploadFile(file, 1);
    }

    @GetMapping("/download/{annId}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable Integer annId){
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
        ByteArrayResource Resource = new ByteArrayResource(baos.toByteArray());
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"multiple_files.zip\"")
                .body(Resource);
    }
}
