package com.example.intat3.Dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class AllAnnouncementDto {
    private Integer id;
    private String announcementTitle;
    public  String getAnnouncementCategory (){
        return  category == null ? "-" : category.getName();
    }
    private ZonedDateTime publishDate;
    private ZonedDateTime closeDate;
    private String announcementDisplay ;
    @JsonIgnore
    private  CategoryDTO category;
    private Integer viewer;
}
