package com.example.intat3.services;

import com.example.intat3.Entity.Announcement;
import com.example.intat3.Entity.Category;
import com.example.intat3.Entity.EmailAddress;
import com.example.intat3.repositories.AnnouncementRepository;
import com.example.intat3.repositories.EmailRepository;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
@Slf4j
@Service
@Component
public class    EmailServiceImpl implements EmailService{
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailRepository repository;

    @Autowired
    private CategoryService catService;

    @Autowired
    private AnnouncementRepository announcementRepository;

    private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();


    public void sendSimpleMail(String email, String subject, String html) throws IOException {
        log.info("simple mail was call");
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setSubject(subject);
            message.setContent(html,"text/html; charset=utf-8");
            mailSender.send(message);
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    @Override
    public String sendOTPMail(String email, String subject, String text) {

        String body = "OTP = "+text;
        try {
            String htmlTemplate = readHtmlFileToString("template/otp-email-template.html");
            htmlTemplate = htmlTemplate.replace("${otp}",text);
            htmlTemplate = htmlTemplate.replace("${email}",email);
            sendSimpleMail(email,subject,htmlTemplate);
            saveOTP(email, text);
            return "Mail send successfully";
        }catch (Exception e){
            return "Error while sending mail!";
        }
    }

    public void saveOTP(String emailAddress,String otp) {
        EmailAddress email = repository.findByEmail(emailAddress);
        if(email==null){
            email = new EmailAddress(null, emailAddress,null,null);
        }
        email.setOtp(otp);
        repository.saveAndFlush(email);
        EmailAddress finalEmail = email;
        executorService.schedule(()->expiredOTP(finalEmail.getEmail()),60, TimeUnit.SECONDS);
    }


    public void expiredOTP(String emailAddress) {
        EmailAddress email = repository.findByEmail(emailAddress);
        email.setOtp("");
        repository.saveAndFlush(email);
    }

    @Override
    public String OTPCheck(String emailAddress, String otp, Integer catId) {
        EmailAddress email = repository.findByEmail(emailAddress);
        Category category = catService.getCategory(catId);
        if(email.getOtp().equals(otp)){
            email.setOtp("");
            email.getCategoryList().add(category);
            try {
                repository.saveAndFlush(email);
            }catch (Exception e){
                if(e.getCause().getCause() instanceof SQLIntegrityConstraintViolationException){
                    SQLIntegrityConstraintViolationException sqlException = (SQLIntegrityConstraintViolationException) e.getCause().getCause();
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,sqlException.getMessage().contains("Duplicate")
                            ?"already subscribe these category":sqlException.getMessage());
                }
            }
            return "Success Authentication";
        }else{
            return "Fail Authentication";
        }
    }

    @Override
    public List<EmailAddress> getListCat(String email) {
        return repository.findByCategoryId(1);
    }

//    @Override
    public String sendNotification(String email, Integer catId) throws UnknownHostException {
        log.info("sendNotification was call");
        Base64.Encoder enc = Base64.getEncoder();
        String word = email+"&"+catId;
        String encodeStr = enc.encodeToString(word.getBytes());
        String unsubLink = "http://localhost:8080"+"/api/otps/unsub?encode="+encodeStr;
        String body = "You have a new notification for SIT ANNOUNCEMENT";
        String subject = "Notification";
        String cat = catService.getCategory(catId).getCategoryName();
        try {
            String htmlTemplate = readHtmlFileToString("template/email-template.html");
            htmlTemplate = htmlTemplate.replace("${email}",email);
            htmlTemplate = htmlTemplate.replace("${ann}",cat);
            htmlTemplate = htmlTemplate.replace("${unsub}",unsubLink);
            sendSimpleMail(email,subject,htmlTemplate);
            return "Mail send successfully";
        }catch (Exception e){
            return "Error while sending mail!";
        }
    }

    @Override
    public void sendEmailAfterUpdate(Announcement announcement) {
            List<EmailAddress> allEmail = repository.findByCategoryId(announcement.getCategory().getCategoryId());
                try {
                    for (EmailAddress e : allEmail) {
                        log.info(e.getEmail());
                        sendNotification(e.getEmail(), announcement.getCategory().getCategoryId());
                    }
                } catch (UnknownHostException ex) {
                    throw new RuntimeException(ex);
                }
        announcement.setNotification("Y");
        announcementRepository.saveAndFlush(announcement);
    }




//    @Scheduled(fixedRate = 1000*60)
    public void publishAnnouncementChecker() throws UnknownHostException {
        log.info("method call!");
        List<Announcement> publishAnn = announcementRepository.announcementPublishCheck();
        if(publishAnn!=null){
            for (Announcement a : publishAnn) {
                log.info(a.getAnnouncementTitle());
                List<EmailAddress> allEmail = repository.findByCategoryId(a.getCategory().getCategoryId());
                for (EmailAddress e : allEmail) {
                    log.info(e.getEmail());
                    sendNotification(e.getEmail(),a.getCategory().getCategoryId());
                }
                a.setNotification("Y");
                announcementRepository.saveAndFlush(a);
            }
        }
    }

    public String readHtmlFileToString(String filePath) throws IOException {
        // Use ClassPathResource to get the resource from the classpath
        ClassPathResource resource = new ClassPathResource(filePath);

        // Get the InputStream from the resource
        try (InputStream inputStream = resource.getInputStream()) {
            // Use FileCopyUtils to copy the content of the InputStream into a byte array
            byte[] bytes = FileCopyUtils.copyToByteArray(inputStream);

            // Convert the byte array to a String using UTF-8 encoding
            return new String(bytes, StandardCharsets.UTF_8);
        }
    }

    @Override
    public void unSubscribe(String email, Integer catId){
        EmailAddress emails =repository.findByEmail(email);
        System.out.println(emails.getEmail());
        emails.getCategoryList().removeIf(cat ->( cat.getCategoryId()==catId) );
        repository.saveAndFlush(emails);
    }
}
