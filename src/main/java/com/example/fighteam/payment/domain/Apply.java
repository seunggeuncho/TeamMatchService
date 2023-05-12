package com.example.fighteam.payment.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter

public class Apply {

    @Id @GeneratedValue
    @Column(name = "apply_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamspace_id")
    private TeamSpace teamspace;

    private int userDeposit;

    
    private Boolean status;

    public Apply() {
    }

    public Apply(Member member, Post post, int userDeposit, Boolean status) {
        this.member = member;
        this.post = post;
        this.userDeposit = userDeposit;
        this.status = status;
    }

    public void plusUserDeposit(int cost) {
        this.userDeposit += cost;
    }

    public void minusUserDeposit(int cost) {
        int result = this.userDeposit - cost;
        validate(result);
        this.userDeposit = result;
    }

    public void validate(int deposit) {
        if(deposit < 0){
            throw new IllegalStateException("잔액이 부족합니다.");
        }
    }
}
