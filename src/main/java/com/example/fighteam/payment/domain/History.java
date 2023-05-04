package com.example.fighteam.payment.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class History {

    @Id
    @GeneratedValue
    @Column(name = "history_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apply_id")
    private Apply apply;



    @Enumerated(EnumType.STRING)
    private HistoryType type;

    private int cost;

    private LocalDateTime date;

    private int balance;

    @Override
    public String toString() {
        return "History{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", cost=" + cost +
                ", date=" + date +
                ", balance=" + balance +
                '}';
    }

    public History() {}


    public History(Member member, Apply apply, HistoryType type, int cost, int balance) {
        this(member, type, cost, balance);
        this.apply = apply;

    }

    public History(Member member,  HistoryType type, int cost,  int balance) {
        this.member = member;
        this.type = type;
        this.cost = cost;
        this.balance = balance;
        this.date = LocalDateTime.now();
    }
}
