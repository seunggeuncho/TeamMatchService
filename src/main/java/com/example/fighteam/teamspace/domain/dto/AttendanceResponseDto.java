package com.example.fighteam.teamspace.domain.dto;

import lombok.Getter;

import java.util.Date;

@Getter
public class AttendanceResponseDto {

    private int cost;
    private int userId;
    private int scores;
    private Date date;
    private int deposit;

    public AttendanceResponseDto(int cost, int userId, int scores, Date date, int deposit) {
        this.cost = cost;
        this.userId = userId;
        this.scores = scores;
        this.date = date;
        this.deposit = deposit;
    }
}
