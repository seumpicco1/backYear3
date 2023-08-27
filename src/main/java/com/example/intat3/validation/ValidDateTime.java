package com.example.intat3.validation;

import com.example.intat3.Dto.UpdateAnnouncementDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class ValidDateTime implements ConstraintValidator<DateValid, UpdateAnnouncementDto> {

    @Override
    public boolean isValid(UpdateAnnouncementDto ann, ConstraintValidatorContext constraintValidatorContext) {
        if(ann == null) return true;
        if(ann.getPublishDate()==null || ann.getCloseDate() == null) return true;
        if(ann.getCloseDate().isAfter(ann.getPublishDate())) return true;
        if(ann.getCloseDate().isBefore(ann.getPublishDate())|| ann.getCloseDate().compareTo(ann.getPublishDate())==0){
            String fieldName = "closeDate";
            String massage = "must be later than publish date";
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(massage).addPropertyNode(fieldName).addConstraintViolation();
            return false;
        }
        return false;
    }
}
