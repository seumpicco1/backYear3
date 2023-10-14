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

    List<Announcement> findAllByUser(User user);

    @Modifying @Transactional
    @Query("update Announcement a set a.user.id = :adminId where a.user.id = :announcerId")
    void updateAnnouncementFromAnnouncerToAdmin(@Param("adminId") int adminId, @Param("announcerId") int announcerId);
}
