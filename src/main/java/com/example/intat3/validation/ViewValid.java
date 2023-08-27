package com.example.intat3.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
//@Documented
@Constraint(validatedBy = ValidViewerCount.class)
public @interface ViewValid {
    String message() default "viewer value is not lower than or equal current value";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
