package com.example.fighteam.teamspace.domain.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AttendanceCheckRequestDto {

    private Long id;
    private String check;
    private String etc;

    public AttendanceCheckRequestDto(Long id, String check, String etc) {
        this.id = id;
        this.check = check;
        this.etc = etc;
    }

}
