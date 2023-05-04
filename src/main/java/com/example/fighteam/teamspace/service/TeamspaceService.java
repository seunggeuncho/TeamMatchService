package com.example.fighteam.teamspace.service;

import com.example.fighteam.teamspace.domain.dto.AttendanceCheckRequestDto;
import com.example.fighteam.teamspace.domain.dto.AttendanceResponseDto;
import com.example.fighteam.teamspace.domain.dto.HistoryDto;
import com.example.fighteam.teamspace.domain.repository.Attendance;
import com.example.fighteam.teamspace.domain.repository.AttendanceRepository;
import com.example.fighteam.teamspace.domain.repository.TeamspaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class TeamspaceService {
    @Autowired
    private AttendanceRepository attendanceRepository;
    @Autowired
    private TeamspaceRepository teamspaceRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    public void createAttendance(AttendanceCheckRequestDto attendanceCheckRequestDto) {

    }
    public int getUserDeposit(Long user_id, Long teamspace_id){
        String sql = "select apply_userdeposit from apply where user_id = ? and teamspace_id = ?";
        int UserDeposit = jdbcTemplate.queryForObject(sql, new Object[]{user_id, teamspace_id}, Integer.class);
        return UserDeposit;
    }

    public List<HistoryDto> getHistory(Long user_id, Long teamspace_id) {
        String sql = "select ch.user_id , history_date, type, cost, apply_userdeposit from charge_history ch, apply app " +
                "where ch.user_id = app.user_id and ch.teamspace_id = ? and ch.user_id = ? order by history_date desc";
        List<HistoryDto> history = jdbcTemplate.query(sql, new Object[]{teamspace_id, user_id}, new RowMapper<HistoryDto>() {
            @Override
            public HistoryDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                HistoryDto historyDto = new HistoryDto();
                historyDto.setHistory_date(rs.getTimestamp("history_date").toLocalDateTime());
                historyDto.setCost(rs.getInt("cost"));
                historyDto.setType(rs.getString("type"));
                historyDto.setUser_id(rs.getLong("user_id"));

                return historyDto;
            }
        });
        return history;
    }


    public void writeAttendance(Attendance attendance){

        attendanceRepository.save(attendance);
    }
    public List<AttendanceResponseDto> getMember(long teamspace_id){
        String sql = "select user_id from apply where teamspace_id = ?";
        List<AttendanceResponseDto> members= jdbcTemplate.query(sql, new Object[]{teamspace_id}, new RowMapper<AttendanceResponseDto>() {
            @Override
            public AttendanceResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                AttendanceResponseDto attendanceResponseDto = new AttendanceResponseDto();
                attendanceResponseDto.setUser_id(rs.getLong("user_id"));
                return attendanceResponseDto;
            }
        });
        return  members;
    }


}
