package com.example.fighteam.apply.service;

import com.example.fighteam.apply.domain.dto.ApplyResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class ApplyService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void memberAccept(Long user_id, Long post_id) {
        String sql =  "update apply set status = 'confirm' where user_id = ? and post_id = ?";
        jdbcTemplate.update(sql, user_id,post_id);
    }

    public void memberDeny(Long user_id, Long post_id) {
        String sql =  "delete from apply where user_id = ? and post_id = ?";
        jdbcTemplate.update(sql, user_id,post_id);
    }

    public List<ApplyResponseDto> getApplyList(Long post_id) {
        String sql = "select u.name,u.user_id, u.email, u.score, a.status from users u, apply a where post_id = ? and u.user_id = a.user_id";
        List<ApplyResponseDto> apply_list = jdbcTemplate.query(sql, new Object[]{post_id}, new RowMapper<ApplyResponseDto>() {
            @Override
            public ApplyResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                ApplyResponseDto applyResponseDto = new ApplyResponseDto(rs.getString("name"),rs.getString("email"),rs.getInt("score"), rs.getLong("user_id"),rs.getString("status"));
                return applyResponseDto;
            }
        });
        return apply_list;
    }
    public List<ApplyResponseDto> getApplyListByTsid(Long teamspace_id) {
        String sql = "select u.name,u.user_id, u.email, u.score, a.status from users u, apply a where teamspace_id = ? and u.user_id = a.user_id";
        List<ApplyResponseDto> apply_list = jdbcTemplate.query(sql, new Object[]{teamspace_id}, new RowMapper<ApplyResponseDto>() {
            @Override
            public ApplyResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                ApplyResponseDto applyResponseDto = new ApplyResponseDto(rs.getString("name"),rs.getString("email"),rs.getInt("score"), rs.getLong("user_id"),rs.getString("status"));
                return applyResponseDto;
            }
        });
        return apply_list;
    }
    public void applyTeam(Long post_id, Long user_id){
        String sql = "insert into apply(apply_id, post_id, user_id, apply_userdeposit) values(nextval('seq_apply_id'), ?, ?, 0)";
        jdbcTemplate.update(sql, post_id,user_id);
    }
}
