package com.example.intat3.repositories;

import com.example.intat3.Entity.Announcement;
import com.example.intat3.Entity.Category;
import com.example.intat3.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AnnouncementRepository extends JpaRepository<Announcement,Integer> {
    List<Announcement> findAllByCategory(Category category);

    List<Announcement> findAllByAnnouncementOwner(User user);

    @Modifying @Transactional
    @Query("UPDATE Announcement a SET a.announcementOwner = :adminUsername WHERE a.announcementOwner = :announcerUsername")
    void updateAnnouncementFromAnnouncerToAdmin(User adminUsername, User announcerUsername);
}
