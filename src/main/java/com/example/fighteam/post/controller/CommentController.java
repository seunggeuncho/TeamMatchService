package com.example.fighteam.post.controller;

import com.example.fighteam.post.domain.dto.CreateCommentDto;
import com.example.fighteam.post.domain.dto.GetCommentDto;
import com.example.fighteam.post.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/comment")
public class CommentController {

    CommentService commentService;
    public CommentController(CommentService commentService){
        this.commentService = commentService;
    }

    @PostMapping("/save")
    public ResponseEntity save(CreateCommentDto createCommentDto){
        Long post_id = commentService.createComment(createCommentDto);
        if (post_id != null)
        {
            List<GetCommentDto> getCommentDto;
            getCommentDto = commentService.getComment(post_id);
            return new ResponseEntity<>(getCommentDto, HttpStatus.OK);
        }else{
            return new ResponseEntity<>("해당 게시글은 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }
    }
}
