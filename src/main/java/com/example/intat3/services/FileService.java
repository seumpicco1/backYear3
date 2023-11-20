package com.example.intat3.services;

import com.example.intat3.Entity.Announcement;
import com.example.intat3.Entity.File;
import com.example.intat3.repositories.AnnouncementRepository;
import com.example.intat3.repositories.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Map<String,byte[]> downloadFile(Integer id){
        Announcement ann = annRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Not found"));
        List<File> fileList = repository.findByAnnouncement(ann);
        try {
            Map<String,byte[]> dataFileList = new HashMap<>();
            for(File f: fileList){
                Path path  = Paths.get(f.getFilePath());
                byte[] data =Files.readAllBytes(path);
                dataFileList.put(f.getFileName(),data);
            }
            return dataFileList;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteFileByAnnouncement(Integer id){
        Announcement ann = annRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
        repository.deleteFilesByAnnouncement(ann);
    }
}
