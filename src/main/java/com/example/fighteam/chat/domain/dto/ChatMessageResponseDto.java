package com.example.fighteam.chat.domain.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessageResponseDto {

    private Long message;

    public ChatMessageResponseDto(Long message) {
        this.message = message;
    }
}
