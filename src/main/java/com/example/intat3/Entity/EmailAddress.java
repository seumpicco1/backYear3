package com.example.intat3.Entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "email")
public class EmailAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emailId")
    private Integer emailId;
    @Column(name = "emailAddress")
    private String email;
    @Column(name = "otp")
    private String otp;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER,
                cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
                })
    @JoinTable(name = "subscribe",
            joinColumns = {@JoinColumn(name = "emailId")},
            inverseJoinColumns = {@JoinColumn(name = "categoryId")})
    private List<Category> categoryList;
}
