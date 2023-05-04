package com.example.fighteam.teamspace.controller;

import com.example.fighteam.teamspace.domain.dto.AttendanceResponseDto;
import com.example.fighteam.teamspace.domain.dto.AttendanceRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
public class TeamspaceRestController {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @PostMapping("/AttendanceEvents")
    @ResponseBody
    public ResponseEntity<List<AttendanceResponseDto>> attendanceEvents(@RequestBody Map<String, Object> param){
        Long teamspace_id = Long.valueOf(((Integer)param.get("teamspace_id")).longValue());
        String sql = "SELECT * FROM attendance WHERE teamspace_id = ?";
        List<AttendanceResponseDto> events = jdbcTemplate.query(sql, new Object[]{teamspace_id}, new AttendanceRowMapper());
        return  ResponseEntity.ok(events);
    }

    @PostMapping("/AttendanceEventsClick")
    @ResponseBody
    public ResponseEntity<List<AttendanceResponseDto>> attendanceEventsClick(@RequestBody Map<String, Object> param){
        Long teamspace_id = Long.valueOf(((Integer)param.get("teamspace_id")).longValue());
        String date = param.get("date").toString();
        String sql = "select * from attendance where teamspace_id = ? and calendar_date = ?";
        List<AttendanceResponseDto> clickEvents = jdbcTemplate.query(sql, new Object[]{teamspace_id,date}, new AttendanceRowMapper());
        String sql2 = "select count(*) from apply where teamspace_id = ? group by teamspace_id";
        AttendanceResponseDto attendanceResponseDto = new AttendanceResponseDto();
        attendanceResponseDto.setMemberCnt(jdbcTemplate.queryForObject(sql2, new Object[]{teamspace_id}, Integer.class));
        clickEvents.add(attendanceResponseDto);
        return ResponseEntity.ok(clickEvents);
    }

}
