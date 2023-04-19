package com.example.fighteam.chat.domain.dto;

import lombok.Getter;

@Getter
public class ChatMessageResponseDto {

    private Long message;

    public ChatMessageResponseDto(Long message) {
        this.message = message;
    }
}
