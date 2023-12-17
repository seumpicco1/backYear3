package com.example.intat3.services;

import com.example.intat3.Dto.FileDto;
import com.example.intat3.Entity.Announcement;
import com.example.intat3.Entity.File;
import com.example.intat3.repositories.AnnouncementRepository;
import com.example.intat3.repositories.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class FileService {

    @Value("${upload.file.path}")
    private String path;

    @Autowired
    private FileRepository repository;

    @Autowired
    private AnnouncementRepository annRepository;

    @PreAuthorize("hasAnyRole('ADMIN','ANNOUNCER')")
    public ResponseEntity   uploadFile(List<MultipartFile> arrayFiles, Integer annId){
        if(arrayFiles.size() <= 5){
            try {
                Announcement ann = annRepository.findById(annId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Announcement id " + annId +  " " + "does not exist !!!"));
                String path_dir = path+annId;
                System.out.println(path);
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
                return new ResponseEntity("upload successful", HttpStatus.OK);
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
            }
        }else {
            return new ResponseEntity("Failed to upload files", HttpStatus.FORBIDDEN);
        }

    }

public List<FileDto> downloadFile(Integer id){
    Announcement ann = annRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Not found"));
    List<File> fileList = repository.findByAnnouncement(ann);
    List<FileDto> fileDtoList = new ArrayList<>();
    for (File file: fileList) {
        FileDto fileDto = convertFileToBase64(file.getFilePath());
        fileDto.setName(file.getFileName());
        fileDtoList.add(fileDto);
    }
    return fileDtoList;
}

    public void deleteFileByAnnouncement(Integer id){
        Announcement ann = annRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
        repository.deleteFilesByAnnouncement(ann);
    }

    private FileDto convertFileToBase64(String filePath) {
        Path path = Paths.get(filePath);
        try {
//             fileContent = new byte[0];
            byte[] fileContent = Files.readAllBytes(new java.io.File(path.toUri()).toPath());

            String base64 = Base64.getEncoder().encodeToString(fileContent);
            FileDto fileDto = new FileDto();
            fileDto.setData(base64);
            return fileDto;
        } catch (IOException e) {
            // Handle exception, e.g., log or throw a custom exception
            e.printStackTrace();
            return null;
        }
    }
}

