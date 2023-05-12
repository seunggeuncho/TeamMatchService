package com.example.fighteam.teamspace.controller;

import com.example.fighteam.apply.domain.dto.ApplyResponseDto;
import com.example.fighteam.apply.service.ApplyService;
import com.example.fighteam.teamspace.domain.dto.AttendanceCheckRequestDto;
import com.example.fighteam.teamspace.domain.dto.AttendanceResponseDto;
import com.example.fighteam.teamspace.domain.dto.HistoryDto;
import com.example.fighteam.teamspace.service.TeamspaceService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class TeamspaceController {
    @Autowired
    private ApplyService applyService;
    @Autowired
    private TeamspaceService teamspaceService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/attendanceMain")
    public String attendanceMain(@RequestParam("teamspace_id") Long teamspace_id,HttpSession session, Model model){
        String url;

        if(session.getAttribute("user_id") == null){
            url = "redirect:/TeamspaceErrorManager?error_code=nl"; //need login
        }else {
            Long session_user_id = Long.valueOf(session.getAttribute("user_id").toString());
            if(teamspaceService.isMemberByTsid(session_user_id,teamspace_id)){
                Long user_id = Long.valueOf(session.getAttribute("user_id").toString());
                List<AttendanceResponseDto> members = teamspaceService.getMember(teamspace_id);
                List<HistoryDto> history = teamspaceService.getHistory(user_id, teamspace_id);
                AttendanceCheckRequestDto attendanceCheckRequestDto = new AttendanceCheckRequestDto();
                int UserDeposit = teamspaceService.getUserDeposit(user_id, teamspace_id);
                String teamspace_name = teamspaceService.getTeamspaceName(teamspace_id,"teamspace_id");
                model.addAttribute("teamspace_name",teamspace_name+" - 팀 스페이스");
                model.addAttribute("teamspace_id", teamspace_id);
                //model.addAttribute("radioValues",new String[members.size()]);
                model.addAttribute("userDeposit", UserDeposit);
                model.addAttribute("depositTitle", "현재 보증금 : "+UserDeposit);
                model.addAttribute("history", history);
                model.addAttribute("members",members);
                model.addAttribute("memCnt",members.size());
                model.addAttribute("attendanceCheck",attendanceCheckRequestDto);
                //List<Deposit> list2 = depositService.getDeposit(user_id,Teamspcae_id);
                url = "/AttendanceMain";
            }else{
                url = "redirect:/TeamspaceErrorManager?error_code=nm"; //not member
            }

        }

        return url;
    }

    @PostMapping("/attendanceWrite")
    public String attendanceWritePro(@ModelAttribute("attendanceCheck") AttendanceCheckRequestDto attendanceCheckRequestDto){

        if(attendanceCheckRequestDto.getForm_type().equals("c")){
            teamspaceService.writeAttendance(attendanceCheckRequestDto, attendanceCheckRequestDto.getWriter());
        }else if(attendanceCheckRequestDto.getForm_type().equals("u")) {
            teamspaceService.updateAttendance(attendanceCheckRequestDto);
        }else if(attendanceCheckRequestDto.getForm_type().equals("v")){
            teamspaceService.attVote(attendanceCheckRequestDto);
            teamspaceService.voteConfirmCheck(attendanceCheckRequestDto);
        }
        return "redirect:/attendanceMain?teamspace_id="+attendanceCheckRequestDto.getTeamspace_id();
    }

    @PostMapping(value = "/CreateTeamspacePro",consumes = "application/x-www-form-urlencoded")
    public String CreateTeamspacePro(@RequestParam("post_id") String post_id,@RequestParam("teamspace_name") String teamspace_name,Model model,HttpSession session){
        String url = "";
        System.out.println("post_id:"+post_id);
        Long long_post_id = Long.valueOf(post_id);
        if(session.getAttribute("user_id") == null){
            url = "redirect:/TeamspaceErrorManager?error_code=nl"; //need login
        }else{
            Long session_user_id = Long.valueOf(session.getAttribute("user_id").toString());
            if(teamspaceService.isMaster(session_user_id,long_post_id)){
                Long master = teamspaceService.getMasterFromApply(long_post_id);
                Long teamspace_id = teamspaceService.CreateTeamspace(long_post_id,master,teamspace_name);
                url = "/attendanceMain?teamspace_id="+teamspace_id;
            }else{
                url = "redirect:/TeamspaceErrorManager?error_code=nmst"; //not master
            }
        }



        return url;
    }

    @GetMapping("/CreateTeamspace")
    public String CreateTeamspace(@RequestParam("post_id") Long post_id,Model model, HttpSession session){
        String url = "";
        if(session.getAttribute("user_id") == null){
            url = "redirect:/TeamspaceErrorManager?error_code=nl"; //need login
        }else{
            Long session_user_id = Long.valueOf(session.getAttribute("user_id").toString());
            if(teamspaceService.isMaster(session_user_id,post_id)){
                List<ApplyResponseDto> apply_List = applyService.getApplyList(post_id);
                model.addAttribute("applyList",apply_List);
                model.addAttribute("post_id", post_id);
                url = "/CreateTeamspace";
            }else{
                url = "redirect:/TeamspaceErrorManager?error_code=nmst"; //not master
            }
        }
        return url;
    }
    @PostMapping(value="/AcceptApply",consumes = "application/x-www-form-urlencoded")
    public String AcceptApply(@RequestParam("user_id") Long user_id,@RequestParam("post_id") Long post_id, HttpSession session){
        String url = "";
        if(session.getAttribute("user_id") == null){
            url = "redirect:/TeamspaceErrorManager?error_code=nl"; //need login
        }else{
            Long session_user_id = Long.valueOf(session.getAttribute("user_id").toString());
            if(teamspaceService.isMaster(session_user_id,post_id)){
                url = "redirect:/CreateTeamspace?post_id="+post_id;
                applyService.memberAccept(user_id,post_id);
            }else{
                url = "redirect:/TeamspaceErrorManager?error_code=nmst"; //not master
            }
        }
        return url;
    }
//여기까지
    @PostMapping(value = "/DenyApply",consumes = "application/x-www-form-urlencoded")
    public String DenyApply(@RequestParam("user_id") Long user_id,@RequestParam("post_id") Long post_id, HttpSession session){
        String url = "";
        if(session.getAttribute("user_id") == null){
            url = "redirect:/TeamspaceErrorManager?error_code=nl"; //need login
        }else{
            Long session_user_id = Long.valueOf(session.getAttribute("user_id").toString());
            if(teamspaceService.isMaster(session_user_id,post_id)){
                url = "redirect:/CreateTeamspace?post_id="+post_id;
                applyService.memberDeny(user_id,post_id);
            }else{
                url = "redirect:/TeamspaceErrorManager?error_code=nmst"; //not master
            }
        }
        return url;
    }

    @GetMapping("/ReviewTeammates")
    public String ReviewTeammates(@RequestParam("teamspace_id") Long teamspace_id,Model model, HttpSession session){
        String url = "";
        if(session.getAttribute("user_id") == null){
            url = "redirect:/TeamspaceErrorManager?error_code=nl"; //need login
        }else{
            Long user_id = Long.valueOf(session.getAttribute("user_id").toString());
            if(teamspaceService.isMemberByTsid(user_id,teamspace_id)){
                List<ApplyResponseDto> apply_List = applyService.getApplyList(teamspace_id);
                List<Long> rvd_list = teamspaceService.getReviewList(teamspace_id,user_id);
                model.addAttribute("applyList", apply_List);
                model.addAttribute("rvd_list",rvd_list);
                model.addAttribute("post_id",teamspace_id);
                url = "/ReviewTeammates";
            }else{
                url = "redirect:/TeamspaceErrorManager?error_code=nm"; //not member
            }
        }
        return url;
    }
    @PostMapping("/ReviewWrite")
    public String ReviewWrite(@RequestBody Map<String, Object> param, HttpSession session){
        String url = "";
        if(session.getAttribute("user_id") == null){
            url = "redirect:/TeamspaceErrorManager?error_code=nl"; //need login
        }else{
            Long session_user_id = Long.valueOf(session.getAttribute("user_id").toString());
            Long user_id = (Long)param.get("user_id");
            Long teamspace_id = (Long)param.get("teamspace_id");
            if(teamspaceService.isMemberByTsid(session_user_id,teamspace_id)){
                int q1 = (int)param.get("review_q1");
                int row = teamspaceService.writeReview(teamspace_id, Long.valueOf(session.getAttribute("user_id").toString()),user_id,q1);
                if(row == 1 ){
                    //review가 잘 등록되었는지 확인하는 코드 추가할것
                }
                url = "redirect:/ReviewTeammates?teamspace_id="+teamspace_id;
            }else{
                url = "redirect:/TeamspaceErrorManager?error_code=nm"; //not member
            }
        }
        return url;
    }
    @GetMapping("/myPageTeamspace")
    public String myPageTeamspace(HttpSession session,Model model){
        Long user_id = Long.valueOf(session.getAttribute("user_id").toString());
        List<TeamspaceMyPageResponseDto> team_list = teamspaceService.myPageTeamspaceList(user_id);
        if(team_list.isEmpty()){
            System.out.println("isEmpty");
        }else{
            System.out.println("notEmpty");
            System.out.println(team_list.get(0).getPost_id());
        }
        model.addAttribute("team_list",team_list);
        return "/myPageTeamspace";
    }
}
