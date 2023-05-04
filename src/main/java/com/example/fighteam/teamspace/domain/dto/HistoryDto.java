package com.example.fighteam.teamspace.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class HistoryDto {
    private long user_id;
    private LocalDateTime history_date;
    private String type;
    private int cost;


    public HistoryDto(long user_id, LocalDateTime history_date, String type, int cost){
        this.history_date = history_date;
        this.cost = cost;
        this.type = type;
        this.user_id = user_id;
    }
    public HistoryDto(){}

}
