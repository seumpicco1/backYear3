package com.example.intat3.validation;

import com.example.intat3.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;



public class UniqueValidator implements ConstraintValidator<UniqueValid,String> {

    public UserRepository repository1;
    boolean nameCheck;
    boolean usernameCheck;

    public UniqueValidator(UserRepository repository1) {
        this.repository1 = repository1;
    }

    @Override
    public void initialize(UniqueValid constraintAnnotation) {
//        false
        this.nameCheck = constraintAnnotation.nameCheck();
//        true
        this.usernameCheck = constraintAnnotation.usernameCheck();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String idPath = request.getRequestURI().split("/").length <= 3?null: request.getRequestURI().split("/")[3];
        if(value == null){
            return true;
        }
//        Create
        if(idPath == null){

//          ถ้าชื่อไม่ซ้ำ return true ถ้าซ้ำ return false
            boolean valueValid = this.usernameCheck?this.repository1.existsByUsername(value):this.nameCheck?this.repository1.existsByName(value):this.repository1.existsByEmail(value);
            System.out.println(valueValid);
            return !valueValid;
        }else {

//        Update
            Integer id = Integer.valueOf(idPath);
            boolean updateValid = this.usernameCheck?this.repository1.doesUserExistByUsername(id,value):
                    this.nameCheck?this.repository1.doesUserExistByName(id,value):this.repository1.doesUserExistByEmail(id,value);
            return !updateValid;
        }


    }


}
