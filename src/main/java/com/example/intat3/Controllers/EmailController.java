package com.example.intat3.Controllers;

import com.example.intat3.Dto.EmailDTO;
import com.example.intat3.Dto.InfoUserDTO;
import com.example.intat3.Entity.Category;
import com.example.intat3.Entity.EmailAddress;
import com.example.intat3.Entity.User;
import com.example.intat3.services.EmailService;
import com.example.intat3.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Base64;
import java.util.List;
import java.util.Random;

@CrossOrigin
@RestController
@RequestMapping("/api/otps")
public class EmailController {
    @Autowired
    private EmailService service;

    @Autowired
    private UserService userService;

//    @GetMapping("/{email}")
//    public List<EmailAddress> get(@PathVariable String email){
//        return service.getListCat(email);
//    }

    @GetMapping("/unsub")
    public void unSubscribe(@RequestParam String encode){
        Base64.Decoder dec = Base64.getDecoder();
        String strDecoded = new String(dec.decode(encode));
        String [] words = strDecoded.split("&");
        System.out.println(words[0]+"----"+words[1]);
        service.unSubscribe(words[0],Integer.valueOf(words[1]));
    }

    @PostMapping
    public String EmailOneTimePassword(@RequestBody(required = false) EmailDTO email){
        String username = getUsernameUser();
        Random random = new Random();
        int otp = random.nextInt(999999);
        System.out.println(username);
        if(!username.equals("anonymousUser") && email == null){
            InfoUserDTO user = userService.getUserByUsername(username);
            service.sendOTPMail(user.getEmail(),"OTP",String.valueOf(otp));
        }else if(username.equals("anonymousUser") && email == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Required email");
        }else {
            service.sendOTPMail(email.getEmail(), "OTP", String.valueOf(otp));
        }
        return "Email was send";
    }

    @PostMapping("/check")
    public String OTPCheck(@RequestBody EmailDTO email){
        return service.OTPCheck(email.getEmail(), email.getOtp(), email.getCatId());
    }

    public String getUsernameUser(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
