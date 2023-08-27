package com.example.intat3.Dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDTO {
    private Integer categoryId;
    private String categoryName ;

    public Integer getId(){
        return categoryId;
    }

    public  String getName(){
        return  categoryName;
    }

}
