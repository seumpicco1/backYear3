package com.example.intat3.Dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.Date;

@Getter
@Setter
public class UpdateDTO {

    private String announcementTitle;
    private String announcementDescription;
    private ZonedDateTime publishDate;
    private ZonedDateTime closeDate;
    private String announcementDisplay ;
    private Integer categoryId ;


    public  String getannouncementCategory (){
        return  category == null ? "-" : category.getCategoryName();
    }
    @JsonIgnore
    private  CategoryDTO category;
    private Integer viewer;

}