package com.example.fighteam.post.controller;

import com.example.fighteam.post.domain.dto.*;
import com.example.fighteam.post.service.CommentService;
import com.example.fighteam.post.service.PostService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RequestMapping("/post")
public class PostController {
    @Autowired
    public PostController(PostService postService,CommentService commentService) {
        this.postService = postService;
        this.commentService = commentService;
    }


    PostService postService;

    CommentService commentService;

    @GetMapping("/createpost")  //게시판 생성
    public String createForm(HttpSession session)
    {
        Long user_id = (Long)session.getAttribute("user_id");
        if (user_id != null){
            return "post/createpost";
        }else{
            return "user/login";
        }
    }

    @PostMapping("/createpost") //게시판 생성
    public String create(CreatePostDto createPostDto, Model model){
        CreatePostDto createPostDto1;
        createPostDto1 = createPostDto;
        createPostDto1.setUser_id(1L);
        createPostDto1.setComplete(false);
        model.addAttribute("post", createPostDto1);
        postService.createProject(createPostDto);

        return "redirect:/post/home";
    }

    @GetMapping("/home")    //전체 조회 게시판
    public String home(@RequestParam(defaultValue="1") int currentPage, HttpSession session, Model model){
        List<GetBoardResponseDto> getBoardResponseDto;
        Long id = (Long) session.getAttribute("user_id");
        System.out.println(id);
        getBoardResponseDto = postService.findAll("",currentPage);
        int total = postService.CountBoardList("");
        model.addAttribute("boardList", getBoardResponseDto);
        model.addAttribute("list", new ArticlePage(total, currentPage, 10, 5, getBoardResponseDto));
        model.addAttribute("total", total);
        return "post/home";}

    @GetMapping("/study")   //스터디 조회 게시판
    public String study(@RequestParam(defaultValue="1") int currentPage,HttpSession session,  Model model){
        List<GetBoardResponseDto> getBoardResponseDto;
        Long id = (Long) session.getAttribute("user_id");
        System.out.println(id);
        getBoardResponseDto = postService.findAll("study",currentPage);
        int total = postService.CountBoardList("study");
        model.addAttribute("boardList", getBoardResponseDto);
        model.addAttribute("list", new ArticlePage(total, currentPage, 10, 5, getBoardResponseDto));
        model.addAttribute("total", total);
        return "post/study";}

    @GetMapping("/project") //프로젝트 조회 게시판
    public String project(@RequestParam(defaultValue="1") int currentPage, Model model){
        List<GetBoardResponseDto> getBoardResponseDto;
        getBoardResponseDto = postService.findAll("project",currentPage);
        int total = postService.CountBoardList("project");
        model.addAttribute("boardList", getBoardResponseDto);
        model.addAttribute("list", new ArticlePage(total, currentPage, 10, 5, getBoardResponseDto));
        model.addAttribute("total", total);
        return "post/project";}

    @GetMapping("/res/{id}")
    public String test(@PathVariable Long id, Model model) {
        GetPostDetailResponseDto getPostDetailResponseDto;
        List<GetCommentDto> getCommentDto;
        getPostDetailResponseDto = postService.findById(id);
        getCommentDto = commentService.getComment(getPostDetailResponseDto.getPost_id());
        model.addAttribute("board", getPostDetailResponseDto);
        model.addAttribute("commentList", getCommentDto);
        return "post/postDetail";}

    @GetMapping("/paging")
    public String paging(@RequestParam(defaultValue="1") int currentPage, Model model){
        List<GetBoardResponseDto> getBoardResponseDto;
        getBoardResponseDto = postService.findAll("", currentPage);
        int total = postService.CountBoardList("");
        PageDto pageDto = new PageDto(currentPage, 10, total);
        model.addAttribute("boardList", getBoardResponseDto);
        model.addAttribute("list", new PageDto(currentPage, 10, total));
        model.addAttribute("total", total);
        return "post/paging";
    }

}
