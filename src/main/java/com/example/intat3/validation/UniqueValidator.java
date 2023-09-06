package com.example.intat3.validation;

import com.example.intat3.repositories.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;


public class UniqueValidator implements ConstraintValidator<UniqueValid,String> {

    public UserRepository repository1;
    boolean nameCheck;
    boolean usernameCheck;

    public UniqueValidator(UserRepository repository1) {
        this.repository1 = repository1;
    }

    @Override
    public void initialize(UniqueValid constraintAnnotation) {
        this.nameCheck = constraintAnnotation.nameCheck();
        this.usernameCheck = constraintAnnotation.usernameCheck();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext){
        if(value == null){
            return true;
        }
        boolean valueValid = this.usernameCheck?this.repository1.existsByUsername(value):
                this.nameCheck?this.repository1.existsByName(value):this.repository1.existsByEmail(value);
        return !valueValid;
    }
}
