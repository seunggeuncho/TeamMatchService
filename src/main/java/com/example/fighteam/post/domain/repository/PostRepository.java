package com.example.fighteam.post.domain.repository;

import com.example.fighteam.post.domain.dto.*;

import java.util.List;

public interface PostRepository {
    Long insertproject(CreatePostDto createPostDto);
    Boolean removepost(DeleteProjectRequestDto deleteProjectRequestDto);

    CreatePostDto chooseproject(Long post_id);

    CreatePostDto changeproject(CreatePostDto createPostDto);

    Boolean checkUser(Long user_id, Long post_id);

    Boolean insertlanguage(Long post_id, String language);

    Boolean inserttype(Long post_id, String type);

    List<GetBoardResponseDto> findAllBoard(String topic, int page, int count);

    List<String>findlanguage(Long post_id);

    List<String>findcontent(Long post_id);

    GetPostDetailResponseDto findById(Long user_id);

    int countboard(String topic);
}
