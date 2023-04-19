package com.example.fighteam.mypage.controller.dto;

import lombok.Getter;

@Getter
public class MypageResponseDto {
    private String name;
    private String email;
    private int deposit;

    public MypageResponseDto(String name, String email, int deposit) {
        this.name = name;
        this.email = email;
        this.deposit = deposit;
    }
}
