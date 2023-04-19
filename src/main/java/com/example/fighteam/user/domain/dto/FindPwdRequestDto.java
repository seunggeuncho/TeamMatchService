package com.example.fighteam.user.domain.dto;

import lombok.Getter;

@Getter
public class FindPwdRequestDto {
    private String id;
    private String email;

    public FindPwdRequestDto(String id, String email) {
        this.id = id;
        this.email = email;
    }
}
