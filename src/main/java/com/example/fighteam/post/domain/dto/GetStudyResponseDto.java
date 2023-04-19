package com.example.fighteam.post.domain.dto;

import com.example.fighteam.post.domain.repository.Comment;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Getter
public class GetStudyResponseDto {
    private int memberCount;
    private Date startDate;
    private Date endDate;
    private int deposit;
    private String languageContent;
    private String title;
    private String content;
    private List<Comment> comment;

    public GetStudyResponseDto(int memberCount, Date startDate, Date endDate, int deposit, String languageContent, String title, String content, List<Comment> comment) {
        this.memberCount = memberCount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.deposit = deposit;
        this.languageContent = languageContent;
        this.title = title;
        this.content = content;
        this.comment = comment;
    }
}
