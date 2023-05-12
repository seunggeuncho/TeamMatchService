//package com.example.fighteam.payment.domain;
//
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.Setter;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//@Entity
//@Getter @Setter
//
//public class Member {
//
//    @Id @GeneratedValue
//    @Column(name = "member_id")
//    private Long id;
//
//    private String name;
//    private String email;
//
//    private String passwd;
//    private String score;
//    private LocalDateTime joinDate;
//    private int deposit;
//    private String userNumber;
//
//    @OneToMany(mappedBy = "member")
//    private List<History> historyList = new ArrayList<>();
//
//
//}
///