package com.example.intat3.services;

import com.example.intat3.Dto.AnnouncementDto;
import com.example.intat3.Entity.Announcement;
import com.example.intat3.Entity.File;
import com.example.intat3.repositories.AnnouncementRepository;
import com.example.intat3.repositories.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileService {
    @Autowired
    private FileRepository repository;

    @Autowired
    private AnnouncementRepository annRepository;

    public String uploadFile(MultipartFile[] arrayFiles, Integer annId){
        if(arrayFiles.length <= 5){
            try {
                Announcement ann = annRepository.findById(annId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Announcement id " + annId +  " " + "does not exist !!!"));
                String path_dir = "C:/Users/Loresea/Pictures/Int222/";

                for(MultipartFile file: arrayFiles){
                    byte[] data = file.getBytes();
                    Path path = Paths.get(path_dir+file.getOriginalFilename());
                    Files.write(path,data);
                    File uploadFile = new File(null,file.getOriginalFilename(),path.toString(),ann);
                    repository.save(File.builder()
                            .fileId(null)
                            .fileName(file.getOriginalFilename())
                            .filePath(path.toString())
                            .announcement(ann).build());
                }
                return "upload success";
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
            }
        }else {
            return "fail to upload";
        }

    }
}
