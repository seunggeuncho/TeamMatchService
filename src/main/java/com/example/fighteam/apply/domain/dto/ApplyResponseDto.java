package com.example.fighteam.apply.domain.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class ApplyResponseDto {

    private List<Integer> scores;
    private List<String> email;

    public ApplyResponseDto(List<Integer> scores, List<String> email) {
        this.scores = scores;
        this.email = email;
    }
}
