package com.example.fighteam.teamspace.domain.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AttendanceCheckRequestDto {

    private Long[] user_id;
    private Long teamspace_id;
    private String etc;
    private LocalDateTime calendar_date;
    private String[] att_checks;

    public AttendanceCheckRequestDto(Long[] user_id,Long teamspace_id, String etc, LocalDateTime calendar_date, String[] att_checks) {
        this.user_id = user_id;
        this.teamspace_id = teamspace_id;
        this.etc = etc;
        this.calendar_date = calendar_date;
        this.att_checks = att_checks;
    }
    public AttendanceCheckRequestDto(){

    }

}
