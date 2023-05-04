package com.example.fighteam.post.domain.repository;

import com.example.fighteam.post.domain.dto.*;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;



public class Post implements PostRepository {

    private final DataSource dataSource;

    public Post(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private Connection conn;
    private PreparedStatement pstmt;
    private ResultSet rs;

    private Statement stmt;

    @Override
    public Long insertproject(CreatePostDto createPostDto) {
        String sql = "insert into post_table(user_id, title, content, startdate, enddate, " +
                "recruitdate, deposit, count, complete,subject) values (?,?,?,?,?,?,?,?,?,?)";
        Long result = null;
        try {
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setLong(1, createPostDto.getUser_id());
            pstmt.setString(2, createPostDto.getTitle());
            pstmt.setString(3, createPostDto.getContent());
            pstmt.setDate(4, new java.sql.Date(createPostDto.getStartdate().getTime()));
            pstmt.setDate(5, new java.sql.Date(createPostDto.getEnddate().getTime()));
            pstmt.setInt(6, createPostDto.getRecruitdate());
            pstmt.setInt(7, createPostDto.getDeposit());
            pstmt.setInt(8, createPostDto.getCount());
            pstmt.setBoolean(9, createPostDto.getComplete());
            pstmt.setString(10, createPostDto.getSubject());
            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if(rs.next()){
                result = rs.getLong(1);
            }else{
                throw new SQLException("프로젝트 생성 실패");
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            close(conn, pstmt, rs);
        }
        return result;
    }

    @Override
    public Boolean removepost(DeleteProjectRequestDto deleteProjectRequestDto) {
        return null;
    }

    @Override
    public CreatePostDto chooseproject(Long post_id) {
        return null;
    }

    @Override
    public CreatePostDto changeproject(CreatePostDto createPostDto) {
        return null;
    }

    @Override
    public Boolean checkUser(Long user_id, Long post_id) {
        return null;
    }

    @Override
    public Boolean insertlanguage(Long post_id, String language) {
        String sql = "insert into post_language(post_id, language_id) values(?, " +
                "select language_id from language where language_content = ?)";
        Boolean result = null;
        int row = 0;
        try {
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, post_id);
            pstmt.setString(2, language);
            row = pstmt.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            close(conn, pstmt, rs);
        }
        return result;
    }

    @Override
    public Boolean inserttype(Long post_id, String type) {
        String sql = "insert into post_type(post_id, type_id) values(?, " +
                "select type_id from type where type_content = ?)";
        Boolean result = null;
        int row;
        try {
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, post_id);
            pstmt.setString(2, type);
            row = pstmt.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            close(conn, pstmt, rs);
        }
        return result;
    }

    @Override
    public List<GetBoardResponseDto> findAllBoard(String topic, int page, int count) {
        int start = page * count -(count -1);
        int end = page * count;

        String sql = "";
        if (topic.equals("study")){
            sql = "SELECT title, user_id, post_id, enddate, count, subject, deposit FROM ("
                    +" SELECT ROWNUM NUM, N.* FROM ("+
                    "SELECT *FROM post_table where subject = 'study' ORDER BY date  DESC) N) "
                    +"WHERE NUM BETWEEN ? AND ?";
        }else if(topic.equals("project")){
            sql = "SELECT title, user_id, post_id, enddate, count, subject, deposit FROM ("
                    +" SELECT ROWNUM NUM, N.* FROM ("+
                    "SELECT *FROM post_table where subject = 'project' ORDER BY date  DESC) N) "
                    +"WHERE NUM BETWEEN ? AND ?";
        } else{
            sql = "SELECT title, user_id, post_id, enddate, count, subject, deposit FROM ("
                   +" SELECT ROWNUM NUM, N.* FROM ("+
                            "SELECT *FROM post_table ORDER BY date  DESC) N) "
            +"WHERE NUM BETWEEN ? AND ?";
        }
        List<GetBoardResponseDto> getBoardResponseDto = new ArrayList<>();
        try{
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, start);
            pstmt.setInt(2,end);
            rs = pstmt.executeQuery();
            while(rs.next()){
                GetBoardResponseDto g = new GetBoardResponseDto();
                g.setTitle(rs.getString("title"));
                g.setUser_id(rs.getLong("user_id"));
                g.setPost_id(rs.getLong("post_id"));
                g.setEnddate(rs.getDate("enddate"));
                g.setCount(rs.getInt("count"));
                g.setSubject(rs.getString("subject"));
                g.setDeposit(rs.getInt("deposit"));
                g.setLanguageContent("");
                g.setTypeContent("");
                getBoardResponseDto.add(g);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            close(conn, pstmt, rs);
        }
        return getBoardResponseDto;
    }

    @Override
    public List<String> findlanguage(Long post_id) {
        String sql = "select language_content from language where language_id in (select language_id from post_table p inner " +
                "join post_language l on p.post_id = l.post_id where p.post_id = ?)";
        List<String> language_lst= new ArrayList<>();
        try{
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, post_id);
            rs = pstmt.executeQuery();
            while (rs.next()){
                language_lst.add(rs.getString("language_content"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            close(conn, pstmt, rs);
        }

        return language_lst;
    }

    @Override
    public List<String> findcontent(Long post_id) {
        String sql = "select type_content from type where type_id in (select type_id from post_table " +
                "p inner join post_type t on p.post_id = t.post_id where p.post_id = ?)";
        List<String>content_lst = new ArrayList<>();
        try{
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1,post_id);
            rs = pstmt.executeQuery();
            while(rs.next()){
                content_lst.add(rs.getString("type_content"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            close(conn, pstmt, rs);
        }
        return content_lst;
    }

    @Override
    public GetPostDetailResponseDto findById(Long post_id) {
        String sql = "select * from post_table where post_id = ?";
       GetPostDetailResponseDto getPostDetailResponseDto = new GetPostDetailResponseDto();
        try{
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1,post_id);
            rs = pstmt.executeQuery();
            if (rs.next()){
                getPostDetailResponseDto.setPost_id(rs.getLong("post_id"));
                getPostDetailResponseDto.setUser_id(rs.getLong("user_id"));
                getPostDetailResponseDto.setTitle(rs.getString("title"));
                getPostDetailResponseDto.setContent(rs.getString("content"));
                getPostDetailResponseDto.setStartdate(rs.getDate("startdate"));
                getPostDetailResponseDto.setEnddate(rs.getDate("enddate"));
                getPostDetailResponseDto.setRecruitdate(rs.getInt("recruitdate"));
                getPostDetailResponseDto.setDeposit(rs.getInt("deposit"));
                getPostDetailResponseDto.setDate(rs.getDate("date"));
                getPostDetailResponseDto.setCount(rs.getInt("count"));
                getPostDetailResponseDto.setSubject(rs.getString("subject"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            close(conn, pstmt, rs);
        }
        return getPostDetailResponseDto;
    }

    @Override
    public int countboard(String topic) {
        int num = 0;
        String sql = "";
        if (topic.equals("study")){
            sql = "select Count(*) cnt from post_table where subject= 'study'";
        }else if(topic.equals("project")){
            sql = "select Count(*) cnt from post_table where subject= 'project'";
        } else{
            sql = "select Count(*) cnt from post_table";
        }
        try{
            conn = dataSource.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            if(rs.next()){
                num = rs.getInt("cnt");
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            close(conn, pstmt, rs);
        }
        return num;
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
