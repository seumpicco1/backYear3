package com.example.intat3.services;

import com.example.intat3.Dto.*;
import com.example.intat3.Entity.Announcement;
import com.example.intat3.Entity.Category;
import com.example.intat3.Entity.Role;
import com.example.intat3.Entity.User;
import com.example.intat3.repositories.AnnouncementRepository;
import com.example.intat3.repositories.CategoryRepository;
import com.example.intat3.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnnouncementService {
    @Autowired
    private AnnouncementRepository announcementrepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private EmailService emailService;

    public AnnouncementDto getAnnouncementById(Integer announcementId, boolean count, String username  ) {
        Announcement a = announcementrepository.findById(announcementId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Announcement id " + announcementId +  " " + "does not exist !!!"));
        System.out.println(username);
        User user = null;
        if(!username.contains("anonymousUser")&& username != null){
            user = userRepository.findByUsername(username);
            if(user.getRole().equals(Role.admin)){
                return modelMapper.map(a,AnnouncementDto.class);
            }
            else if(!a.getAnnouncementOwner().getUsername().equals(user.getUsername())){
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Forbidden");
            }
        }

        if (count) {
            a.setViewer(a.getViewer()+1);
            announcementrepository.saveAndFlush(a);
        }

        return modelMapper.map(a,AnnouncementDto.class);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<AllAnnouncementDto> getAllAnnouncement() {
        List<Announcement> aa = announcementrepository.findAll();
        Collections.reverse(aa);
        return aa.stream().map(x->modelMapper.map(x, AllAnnouncementDto.class)).collect(Collectors.toList());
    }
    @PreAuthorize("hasAnyRole('ADMIN','ANNOUNCER')")
    public List<AllAnnouncementDto> getAllAnnouncementByUser(String username){
        User user = userRepository.findByUsername(username);
        List<Announcement>list =  announcementrepository.findAllByAnnouncementOwner(user);
        Collections.reverse(list);
        return list.stream().map(x -> modelMapper.map(x,AllAnnouncementDto.class)).collect(Collectors.toList());
    }

    public AnnouncementDto createAnn( UpdateAnnouncementDto upAnn, String username) {
        if (upAnn.getAnnouncementDisplay() == null) {
            upAnn.setAnnouncementDisplay("N");
        }
        else {
            upAnn.setAnnouncementDisplay(upAnn.getAnnouncementDisplay());
        }
        if(upAnn.getViewer() == null){
            upAnn.setViewer(0);
        }
        User user = userRepository.findByUsername(username);
        Category cat = categoryRepository.findById(upAnn.getCategoryId()).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Category id " + upAnn.getCategoryId() + " does not exist !!!"));

        Announcement aa = modelMapper.map(upAnn,Announcement.class);
        aa.setCategory(cat);
        aa.setAnnouncementOwner(user);
        announcementrepository.saveAndFlush(aa);
        return modelMapper.map(aa,AnnouncementDto.class);
    }

    public void deleteAnn(int id , String username){
        Announcement a = announcementrepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Announcement id " + id +  " " + "does not exist !!!"));
        User user = null;
        if(!username.contains("anonymousUser")){
            user = userRepository.findByUsername(username);
            if(user.getRole().equals(Role.admin)){
                announcementrepository.delete(a);
                return;
            }else if(!a.getAnnouncementOwner().getUsername().equals(user.getUsername())){
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Forbidden");
            }
        }
        announcementrepository.delete(a);

    }

    public UpdateDTO updateAnn(int id, UpdateAnnouncementDto newAnn, String username) {
        Announcement curAnn = announcementrepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Announcement id " + id + " " + "does not exist !!!"));
        Category cat = categoryRepository.findById(newAnn.getCategoryId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category id " + newAnn.getCategoryId() + " does not exist !!!"));
        User user = null;
        Announcement nAnn = modelMapper.map(newAnn,Announcement.class);

        curAnn.setCategory(cat);
        curAnn.setAnnouncementTitle(nAnn.getAnnouncementTitle());
        curAnn.setAnnouncementDescription(nAnn.getAnnouncementDescription());
        curAnn.setPublishDate(nAnn.getPublishDate());
        curAnn.setCloseDate(nAnn.getCloseDate());
        curAnn.setAnnouncementDisplay(nAnn.getAnnouncementDisplay());
        if(!username.contains("anonymousUser")){
            user = userRepository.findByUsername(username);
            if(user.getRole().equals(Role.admin)){
                announcementrepository.saveAndFlush(curAnn) ;
                return  modelMapper.map(curAnn,UpdateDTO.class);
            }
            else if(!curAnn.getAnnouncementOwner().getUsername().equals(user.getUsername())){
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Forbidden");
            }
        }
        announcementrepository.saveAndFlush(curAnn) ;
        emailService.sendEmailAfterUpdate(curAnn);
        return  modelMapper.map(curAnn,UpdateDTO.class);
    }

    public PageDTO<AllAnnouncementDto> getAllPageAnn(int page, int size, String mode, int catId){
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
         List<AllAnnouncementDto> all = getAnnByDisplay(mode,catId);
        int start = page * size;
        int end = Math.min(start + size, all.size());
        System.out.println(all.size());
        Page<AllAnnouncementDto> ann = new PageImpl<>(all.subList(start,end), pageable, all.size());
        return modelMapper.map(ann, PageDTO.class);
    }

    @PreAuthorize("hasAnyRole('ADMIN','ANONYMOUS','ANNOUNCER')")
    public List<AllAnnouncementDto> getAnnByDisplay(String mode, int cat){
        List<Announcement> all = new ArrayList<>();
        if(cat==0){
            all = announcementrepository.findAll();
        }else {
            Category category = categoryRepository.findById(cat).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Category id " + cat + " does not exist !!!"));
            all = announcementrepository.findAllByCategory(category);
        }

        List<Announcement> filtered = new ArrayList<>();
        all.forEach(x -> {
            ZonedDateTime current = ZonedDateTime.now();
            if(mode.equals("active")){
                if((x.getPublishDate()==null || current.compareTo(x.getPublishDate())>0) && (x.getCloseDate()==null|| current.compareTo(x.getCloseDate())<0)){
                    filtered.add(x);
                }
            } else {
                if((x.getCloseDate() != null && current.compareTo(x.getCloseDate())>0) && x.getAnnouncementDisplay().equals("Y") ){
                    filtered.add(x);
                }
            }
        });
        Collections.reverse(filtered);
        return filtered.stream().map(x->modelMapper.map(x, AllAnnouncementDto.class)).collect(Collectors.toList());
    }


}
