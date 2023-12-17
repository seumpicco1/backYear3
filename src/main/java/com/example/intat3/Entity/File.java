package com.example.intat3.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "file")
@Builder
public class File {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "fileId")
    private Integer fileId;

    @Column(name = "fileName")
    private String fileName;

    @Column(name = "filePath")
    private String filePath;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "announcementId")
//    @Column(name = "announcementId")
    private Announcement announcement;
}
