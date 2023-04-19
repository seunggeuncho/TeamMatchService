package com.example.fighteam.mypage.controller.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MychatResponseDto {
    private LocalDateTime lastMessageTime;
    private String title;
    private String lastMessage;

    public MychatResponseDto(LocalDateTime lastMessageTime, String title, String lastMessage) {
        this.lastMessageTime = lastMessageTime;
        this.title = title;
        this.lastMessage = lastMessage;
    }
}
