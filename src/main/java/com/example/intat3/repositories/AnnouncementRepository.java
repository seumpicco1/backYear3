package com.example.intat3.repositories;

import com.example.intat3.Dto.AllAnnouncementDto;
import com.example.intat3.Dto.PageDTO;
import com.example.intat3.Entity.Announcement;
import com.example.intat3.Entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;

public interface AnnouncementRepository extends JpaRepository<Announcement,Integer> {
    List<Announcement> findAllByCategory(Category category);
    List<Announcement> findByCloseDateIsNullOrCloseDateAfterAndPublishDateBefore(ZonedDateTime currentDate, ZonedDateTime currentDate1);
    List<Announcement> findAllByAnnouncementDisplay(String mode);

}
