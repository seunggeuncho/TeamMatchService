package com.example.fighteam.post.service;

import com.example.fighteam.post.domain.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostService {

    @Transactional(readOnly = true)
    public void searchProject(SearchProjectRequestDto searchProjectRequestDto) {

    }

    public void searchStudy(SearchStudyRequestDto searchStudyRequestDto) {

    }

    @Transactional
    public void createProject(CreateProjectRequestDto createProjectRequestDto) {

    }

    public void createStudy(CreateStudyRequestDto createStudyRequestDto) {

    }

    public void detailProject() {

    }

    public void detailStudy() {

    }
}
