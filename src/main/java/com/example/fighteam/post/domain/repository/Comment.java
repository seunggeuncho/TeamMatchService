package com.example.fighteam.post.domain.repository;


import com.example.fighteam.post.domain.dto.*;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class Comment implements  CommentRepository{
    private final  DataSource dataSource;

    public Comment(DataSource dataSource){this.dataSource =dataSource;}

    private Connection conn;
    private PreparedStatement pstmt;
    private ResultSet rs;

    private Statement stmt;

    @Override
    public Long saveComment(CreateCommentDto createCommentDto) {
        String sql = "insert into comment(user_id, post_id, comment_content) values(?,?,?)";
        try {
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, createCommentDto.getUser_id());
            pstmt.setLong(2, createCommentDto.getPost_id());
            pstmt.setString(3, createCommentDto.getComment());
            int row = pstmt.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            close(conn, pstmt, rs);
        }
        return createCommentDto.getPost_id();
    }

    @Override
    public List<GetCommentDto> findbyPostid(Long post_id) {
        String sql = "select comment_id, user_id, post_id, comment_content, comment_date from comment " +
                "where post_id = ? order by comment_date desc";
        List<GetCommentDto> getCommentDto = new ArrayList<>();
        try{
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1,post_id);
            rs = pstmt.executeQuery();
            while (rs.next()){
                GetCommentDto g = new GetCommentDto();
                g.setComment_id(rs.getLong("comment_id"));
                g.setPost_id(rs.getLong("post_id"));
                g.setUser_id(rs.getLong("user_id"));
                g.setComment(rs.getString("comment_content"));
                g.setCommentCreatedTime(rs.getDate("comment_date"));
                getCommentDto.add(g);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            close(conn, pstmt, rs);
        }

        return getCommentDto;
    }

    private Connection getConnection(){
        return DataSourceUtils.getConnection(dataSource);
    }
    private void close(Connection conn, PreparedStatement pstmt, ResultSet rs){
        try {
            if (rs != null) {
                rs.close();
            }
        }
        catch(SQLException e) {e.printStackTrace();}

        try{
            if (pstmt != null){
                pstmt.close();
            }
        }catch(SQLException e) {e.printStackTrace();}

        try{
            if (conn != null){
                conn.close();
            }
        }catch(SQLException e) {e.printStackTrace();}
    }

    private void close(Connection conn) throws SQLException{
        DataSourceUtils.releaseConnection(conn, dataSource);
    }
}
