package com.example.intat3.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidator implements ConstraintValidator<PasswordValid,String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        String regex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!*]).*$";
        Pattern p = Pattern.compile(regex);

        if(s==null||s.trim().isEmpty()  ) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("must not be blank").addConstraintViolation();
            return false;
        }else if(s.trim().length()>14||s.trim().length()<8){
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("size must be between 8 and 14").addConstraintViolation();
            return false;
        }
        else if(!p.matcher(s).matches()){
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("must be 8-14 characters long, at least 1 of uppercase, lowercase, number and special characters").addConstraintViolation();
            return false;
        }
        return true;
    }
}
