package com.example.fighteam.user.domain.dto;

import lombok.Getter;

@Getter
public class LoginRequestDto {
    private String email;
    private String passwd;

    public LoginRequestDto(String email, String passwd) {
        this.email = email;
        this.passwd = passwd;
    }
}
