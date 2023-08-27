package com.example.intat3.Converter;

import jakarta.persistence.AttributeConverter;

public class StringConverter implements AttributeConverter<String, String> {

    @Override
    public String convertToDatabaseColumn(String value) {
        return value;
    }

    @Override
    public String convertToEntityAttribute(String value){
        if(value==null){
            return null;
        }
        return value.trim();
    }
}
