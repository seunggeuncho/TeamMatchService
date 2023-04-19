package com.example.fighteam.user.domain.dto;

import lombok.Getter;

@Getter
public class JoinRequestDto {
    private String id;
    private String email;
    private String passwd;
    private String name;
    private String phone;

    public JoinRequestDto(String id, String email, String passwd, String name, String phone) {
        this.id = id;
        this.email = email;
        this.passwd = passwd;
        this.name = name;
        this.phone = phone;
    }
}
