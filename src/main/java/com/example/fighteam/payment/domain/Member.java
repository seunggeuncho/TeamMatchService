package com.example.fighteam.payment.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;
    private String email;

    private String passwd;
    private String score;
    private LocalDateTime joinDate;
    private int deposit;
    private String userNumber;

    @OneToMany(mappedBy = "member")
    private List<History> historyList = new ArrayList<>();

    public int plusDeposit(int cost) {
        this.deposit += cost;
        return this.deposit;
    }

    public int minusDeposit(int cost){
        int result = this.deposit - cost;
        validate(result);
        this.deposit = result;
        return deposit;
    }

    public void validate(int deposit) {
        if (deposit < 0){
            throw new IllegalStateException("잔액이 부족합니다.");
        }
    }
}
