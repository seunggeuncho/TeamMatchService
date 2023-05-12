package com.example.fighteam.payment.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
public class Post {

    @Id @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    private String title;
    private String content;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int recruitDate;
    private int postDeposit;
    private LocalDateTime date;
    private int count;
    private Boolean complete;
    private String subject;
}
