package com.example.fighteam.teamspace.service;

import com.example.fighteam.teamspace.domain.dto.AttendanceCheckRequestDto;
import com.example.fighteam.teamspace.domain.dto.AttendanceResponseDto;
import com.example.fighteam.teamspace.domain.dto.HistoryDto;
import com.example.fighteam.teamspace.domain.dto.TeamspaceMyPageResponseDto;
import com.example.fighteam.teamspace.domain.repository.AttendanceRepository;
import com.example.fighteam.teamspace.domain.repository.TeamspaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
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
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                HistoryDto historyDto = new HistoryDto();
                historyDto.setHistory_date(sdf.format(rs.getTimestamp("history_date")));
                historyDto.setCost(rs.getInt("cost"));
                historyDto.setType(rs.getString("type"));
                historyDto.setUser_id(rs.getLong("user_id"));

                return historyDto;
            }
        });
        return history;
    }

    public String getTeamspaceName(Long teamspace_id, String by){
        String sql = "select teamspace_name from teamspace where teamspace_id=?";
        return jdbcTemplate.queryForObject(sql, new Object[]{teamspace_id}, String.class);
    }
    public int writeAttendance(AttendanceCheckRequestDto attendanceCheckRequestDto,String user_id){
        String sql = "insert into attendance(calendar_id, teamspace_id, user_id, calendar_date, attendance_check, etc, status)" +
                "values(nextval('SEQ_ATTENDANCE'), ?, ?, ?, ?, ?, ?)";
        Long teamspace_id = Long.parseLong(attendanceCheckRequestDto.getTeamspace_id());
        String[] users_id = attendanceCheckRequestDto.getUser_id();

        int rowCnt = 0;
        for(int i = 0; i < users_id.length; i++){
            Long member_id = Long.parseLong(users_id[i]);
            rowCnt += jdbcTemplate.update(sql,teamspace_id, member_id,attendanceCheckRequestDto.getCalendar_date(),
                    attendanceCheckRequestDto.getAtt_checks()[i],attendanceCheckRequestDto.getEtc(),"unconfirm");
        }
        String vote_sql = "insert into att_vote(vote_id, teamspace_id, user_id, calendar_date, vote) " +
                "values(nextval('seq_vote_id'), ?,?,?,'agree')";
        jdbcTemplate.update(vote_sql, attendanceCheckRequestDto.getTeamspace_id(),user_id,attendanceCheckRequestDto.getCalendar_date());
        return rowCnt;
    }
    public int updateAttendance(AttendanceCheckRequestDto attendanceCheckRequestDto){
        String sql = "select count(user_id) from attendance where user_id = ? and teamspace_id = ? and calendar_date = ?";
        String[] users_id = attendanceCheckRequestDto.getUser_id();
        String[] att_check = attendanceCheckRequestDto.getAtt_checks();
        Long teamspace_id = Long.parseLong(attendanceCheckRequestDto.getTeamspace_id());
        int rowCnt = 0;
        for(int i = 0; i < users_id.length; i++){
            Long member_id = Long.parseLong(users_id[i]);
            int result = jdbcTemplate.queryForObject(sql, new Object[]{member_id, teamspace_id,
                    attendanceCheckRequestDto.getCalendar_date()}, Integer.class);
            if(result == 1){
                String u_sql = "update attendance set attendance_check = ?, etc = ? where user_id = ? and teamspace_id = ? and calendar_date = ?";
                rowCnt += jdbcTemplate.update(u_sql,att_check[i],attendanceCheckRequestDto.getEtc(),
                        member_id,teamspace_id, attendanceCheckRequestDto.getCalendar_date());
            }else{
                String i_sql = "insert into attendance(calendar_id, teamspace_id, user_id, calendar_date, attendance_check, etc, status)" +
                        "values(nextval(seq_attendance), ?, ?, ? , ?, ?, ?);";
                rowCnt += jdbcTemplate.update(sql,teamspace_id, member_id,attendanceCheckRequestDto.getCalendar_date(),
                        att_check[i],attendanceCheckRequestDto.getEtc(),'N');
            }
        }
        return rowCnt;
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
    public Long CreateTeamspace(Long post_id, Long master, String teamspace_name){
        String delete_sql = "delete from apply where post_id = ? and apply_status is null";
        jdbcTemplate.update(delete_sql,post_id);
        String insert_sql = "insert into teamspace(teamspace_id, post_id, teamspace_name, master) values(seq_teamspace_id.nextval, ?, ?, ?)";
        jdbcTemplate.update(insert_sql, post_id, teamspace_name, master);
        String select_sql = "select distinct teamspace_id from apply where post_id = ?";
        Long teamspace_id = jdbcTemplate.queryForObject(select_sql, new Object[]{post_id}, Long.class);
        return teamspace_id;
    }

    public boolean isMaster(Long user_id, Long post_id){
        String sql = "select apply_status from apply where user_id = ? and post_id = ?";
        String status = jdbcTemplate.queryForObject(sql, new Object[]{user_id, post_id}, String.class);
        if(status.equals("master")){
            return true;
        }else{
            return false;
        }
    }
    public boolean isMember(Long user_id, Long post_id){
        String sql = "select apply_status from apply where user_id = ? and post_id = ?";
        String status= jdbcTemplate.queryForObject(sql, new Object[]{user_id, post_id}, String.class);
        if(status.equals(null)){
            return false;
        }else{
            return true;
        }
    }
    public boolean isMemberByTsid(Long user_id, Long teamspace_id){
        String sql = "select apply_status from apply where user_id = ? and teamspace_id = ?";
        String status= jdbcTemplate.queryForObject(sql, new Object[]{user_id, teamspace_id}, String.class);
        if(status.equals(null)){
            return false;
        }else{
            return true;
        }
    }
    public List<Long> getReviewList(Long teamspace_id, Long user_id){
        String sql = "select user_rvd from review where post_id = ? and user_rvr = ?";
        List<Long> list = jdbcTemplate.query(sql, new Object[]{teamspace_id, user_id}, new RowMapper<Long>() {
            @Override
            public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getLong("user_rvd");
            }
        });
        return list;
    }
    public int writeReview(Long teamspace_id, Long user_rvr, Long user_rvd, int q1){
        String sql = "insert into review(review_id,user_rvr, user_rvd, teamspace_id, q1) " +
                "values(seq_review_id, ?,?,?,?)";
        return jdbcTemplate.update(sql,user_rvr,user_rvd,teamspace_id,q1);
    }

    public int attVote(AttendanceCheckRequestDto attendanceCheckRequestDto){
        String sql = "insert into att_vote(vote_id, teampsace_id, user_id, calendar_date, vote) values(nextval('seq_vote_id', ?,?,?,'agree')";
        return jdbcTemplate.update(sql,attendanceCheckRequestDto.getTeamspace_id(),attendanceCheckRequestDto.getWriter(),attendanceCheckRequestDto.getCalendar_date());
    }

    public void voteConfirmCheck(AttendanceCheckRequestDto attendanceCheckRequestDto){
        String sql = "update attendance set status = 'confirm' where teamspace_id = ? and calendar_date = ? and (select count(user_id) from att_vote where teamspace_id = ? and  calendar_date = ? and vote = 'agree') > (select count(user_id) from apply where teamspace_id = ?)";
        String teamspace_id = attendanceCheckRequestDto.getTeamspace_id();
        String calendar_date = attendanceCheckRequestDto.getCalendar_date();
        jdbcTemplate.update(sql, teamspace_id, calendar_date, teamspace_id,calendar_date,teamspace_id);
    }
    public List<TeamspaceMyPageResponseDto> myPageTeamspaceList(Long user_id){
        String sql1 = "select apply_status, post_id, teamspace_id from apply where user_id = ?";
        List<TeamspaceMyPageResponseDto> team_list = jdbcTemplate.query(sql1, new Object[]{user_id}, new RowMapper<TeamspaceMyPageResponseDto>() {
            @Override
            public TeamspaceMyPageResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                TeamspaceMyPageResponseDto teamspaceMyPageResponseDto = new TeamspaceMyPageResponseDto();
                teamspaceMyPageResponseDto.setApply_status(rs.getString("apply_status"));
                teamspaceMyPageResponseDto.setPost_id(rs.getLong("post_id"));
                if(String.valueOf(rs.getLong("teamspace_id")).equals("0")){
                    teamspaceMyPageResponseDto.setTeamspace_status("시작 준비");
                    teamspaceMyPageResponseDto.setTeamspace_id(String.valueOf(rs.getLong("teamspace_id")));
                }else{
                    teamspaceMyPageResponseDto.setTeamspace_id(String.valueOf(rs.getLong("teamspace_id")));
                    teamspaceMyPageResponseDto.setTeamspace_status("진행중");
                }
                return teamspaceMyPageResponseDto;
            }
        });
        return team_list;
    }
    public String getTeamspaceStatus(Long teamspace_id){
        String sql = "select teamspace_status from teamspace where teamspace_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{teamspace_id}, String.class);
    }
    public Long getMasterFromApply(Long post_id){
        String sql = "select user_id from apply where post_id = ?";
        return jdbcTemplate.queryForObject(sql,new Object[]{post_id}, Long.class);
    }
}
