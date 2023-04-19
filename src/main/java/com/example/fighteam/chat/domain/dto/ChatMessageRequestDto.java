package com.example.fighteam.chat.domain.dto;

import lombok.Getter;

@Getter
public class ChatMessageRequestDto {
    private String message;
    private String sender;
    private String receiver;
    private Long roomid;

    public ChatMessageRequestDto(String message, String sender, String receiver, Long roomid) {
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
        this.roomid = roomid;
    }
}
