package com.example.fighteam.chat.domain.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
