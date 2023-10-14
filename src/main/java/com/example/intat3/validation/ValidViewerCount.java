package com.example.intat3.validation;

import com.example.intat3.Dto.UpdateAnnouncementDto;
import com.example.intat3.services.AnnouncementService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class ValidViewerCount implements ConstraintValidator<ViewValid, Integer> {
    @Autowired
    private AnnouncementService service;
    private Integer id ;
    @Override
    public void initialize(ViewValid constraintAnnotation) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String idPath = request.getRequestURI().split("/")[3];
        id = Integer.valueOf(idPath);
    }

    @Override
    public boolean isValid(Integer ann, ConstraintValidatorContext constraintValidatorContext) {

//        if (ann > service.getAnnouncementById(id, true).getViewer()  && ann != null) {
//            System.out.println(true);
            return true;

//        } else {
//            return false;
//        }

    }
}
