package com.example.fighteam.post.service;

import com.example.fighteam.post.domain.dto.*;
import com.example.fighteam.post.domain.repository.CommentRepository;
import com.example.fighteam.post.domain.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class PostService {

    private final PostRepository postRepository;



    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public void searchProject(SearchProjectRequestDto searchProjectRequestDto) {

    }

    public void searchStudy(SearchStudyRequestDto searchStudyRequestDto) {

    }
    public List<GetBoardResponseDto> findAll(String topic, int page){

        List<GetBoardResponseDto> getBoardResponseDto = new ArrayList<>();
        getBoardResponseDto = postRepository.findAllBoard(topic,page, 10);

        for(GetBoardResponseDto g: getBoardResponseDto){
            List<String> language = postRepository.findlanguage(g.getPost_id());
            List<String> type = postRepository.findcontent(g.getPost_id());
            String lan = String.join(", ",language);
            String tp = String.join(", ",type);
            g.setLanguageContent(lan);
            g.setTypeContent(tp);
        }
        return getBoardResponseDto;
    }


    public void createProject(CreatePostDto createPostDto) {
        List<String> language = createPostDto.getLanguageContent();
        List<String> type = createPostDto.getTypeContent();
        Long post_id;
        post_id = postRepository.insertproject(createPostDto);
        for(String L: language){
            postRepository.insertlanguage(post_id, L);
        }
        for (String T: type){
            postRepository.inserttype(post_id, T);
        }

    }

    public GetPostDetailResponseDto findById(Long post_id){

        GetPostDetailResponseDto getPostDetailResponseDto = new GetPostDetailResponseDto();
        getPostDetailResponseDto = postRepository.findById(post_id);
        List<String> language = postRepository.findlanguage(getPostDetailResponseDto.getPost_id());
        List<String> type = postRepository.findcontent(getPostDetailResponseDto.getPost_id());
        String lan = String.join(", ",language);
        String tp = String.join(", ",type);
        getPostDetailResponseDto.setLanguageContent(lan);
        getPostDetailResponseDto.setTypeContent(tp);
        return getPostDetailResponseDto;
    }

    public void createStudy(CreateStudyRequestDto createStudyRequestDto) {

    }
    public int CountBoardList(String topic){
        int num = 0;
        num = postRepository.countboard(topic);
        return num;
    }

    public void detailProject() {

    }

    public void detailStudy() {

    }

    public void updateProject(UpdateProjectRequestDto updateProjectRequestDto){

    }

    public void deleteProject(DeleteProjectRequestDto deleteProjectRequestDto){

    }

    public void updateStudy(UpdateStudyRequestDto updateStudyRequestDto){

    }

}
