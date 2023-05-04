package com.example.fighteam.teamspace.controller;

import com.example.fighteam.teamspace.domain.dto.*;
import com.example.fighteam.teamspace.domain.repository.Attendance;
import com.example.fighteam.teamspace.service.TeamspaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jdbc.support.JdbcUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

@Controller
public class TeamspaceController {
    @Autowired
    private TeamspaceService teamspaceService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/attendanceMain")
    public String attendanceMain(@RequestParam("teamspace_id") Long teamspace_id,@RequestParam("user_id") Long user_id, Model model){
        List<AttendanceResponseDto> members = teamspaceService.getMember(teamspace_id);
        List<HistoryDto> history = teamspaceService.getHistory(user_id, teamspace_id);
        AttendanceCheckRequestDto attendanceCheckRequestDto = new AttendanceCheckRequestDto();
        int UserDeposit = teamspaceService.getUserDeposit(user_id, teamspace_id);
        model.addAttribute("teamspace_id", teamspace_id);
        model.addAttribute("radioValues",new String[members.size()]);
        model.addAttribute("userDeposit", UserDeposit);
        model.addAttribute("depositTitle", "현재 보증금 : "+UserDeposit);
        model.addAttribute("history", history);
        model.addAttribute("members",members);
        model.addAttribute("memCnt",members.size());
        model.addAttribute("attendanceCheck",attendanceCheckRequestDto);
        //List<Deposit> list2 = depositService.getDeposit(user_id,Teamspcae_id);
        return "AttendanceMain";
    }

    @PostMapping("/attendanceWrite")
    public String attendanceWritePro(@ModelAttribute("attendanceCheck") AttendanceCheckRequestDto attendanceCheckRequestDto,
                                     @RequestBody Map<String, Object> param){
        String form_type = param.get("form_type").toString();

        return "";
    }
}
