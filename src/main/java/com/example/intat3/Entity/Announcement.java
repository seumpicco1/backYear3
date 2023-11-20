package com.example.intat3.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "announcement")


public class Announcement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "announcementId" , nullable = false)
    private Integer id;

    @Column(name = "announcementTitle", nullable = false)
    private String announcementTitle;

    @Column(name = "announcementDescription", nullable = false)
    private String announcementDescription;

    @Column(name = "publishDate" ,nullable = true)
    private ZonedDateTime publishDate;

    @Column(name = "closeDate", nullable = true)
    private ZonedDateTime closeDate;

    @Column(name = "announcementDisplay", nullable = false)
    private String announcementDisplay ;

//     @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;

    @Column(name = "viewCount")
    private Integer viewer;

    @ManyToOne
    @JoinColumn(name = "announcementOwner", referencedColumnName = "username")
    private User announcementOwner;

    @Column(name = "notification")
    private String notification;

    @JsonIgnore
    @OneToMany(mappedBy = "fileId")
    private List<File> fileList;

}
