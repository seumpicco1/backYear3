package com.example.intat3.Dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class AnnouncementDto {
    private  Integer id;
    private String announcementTitle;
    private String announcementDescription;
    private ZonedDateTime publishDate;
    private ZonedDateTime closeDate;
    private String announcementDisplay ;
    private Integer categoryId ;
    public  String getannouncementCategory (){
        return  category == null ? "-" : category.getName();
    }
    @JsonIgnore
    private  CategoryDTO category;
    private Integer viewer;

}
