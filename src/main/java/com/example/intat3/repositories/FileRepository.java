package com.example.intat3.repositories;

import com.example.intat3.Entity.Announcement;
import com.example.intat3.Entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Integer> {
    List<File> findByAnnouncement(Announcement announcement);

    @Modifying
    @Transactional
    @Query("DELETE FROM File f WHERE f.announcement = :ann")
    void deleteFilesByAnnouncement(Announcement ann);

//    void delete(List<File> fileList);
}
