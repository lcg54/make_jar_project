package com.itgroup.dao;

import com.itgroup.bean.Board;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BoardDao extends SuperDao{
    public BoardDao() {
    }

    private Board setBoard(ResultSet rs){
        Board board = null;
        try {
            board = new Board();
            board.setNo(rs.getInt("no"));
            board.setWriter(rs.getString("writer"));
            board.setPassword(rs.getString("password"));
            board.setSubject(rs.getString("subject"));
            board.setContent(rs.getString("content"));
            board.setReadhit(rs.getInt("readhit"));
            board.setRegdate(rs.getString("regdate"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return board;
    }

    public List<Board> selectAll() {
        List<Board> boardList = new ArrayList<>();

        String sql = "select * from boards order by no desc";

        try (Connection conn = super.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery();){
            while (rs.next()) {
                Board board = this.setBoard(rs);
                boardList.add(board);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return boardList;
    }

    public List<Board> selectEven() {
        List<Board> boardList = new ArrayList<>();

        String sql = "select * from boards where mod(no, 2) = 0 order by no desc";

        try (Connection conn = super.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery();){

            while (rs.next()) {
                Board board = this.setBoard(rs);
                boardList.add(board);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return boardList;
    }

    public Board selectOne(String writer) {
        Board board = null;

        String sql = "select * from boards where writer = ?";

        try (Connection conn = super.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);){

            pstmt.setString(1, writer); // 문장의 ? 에 writer를 대입하고

            try(ResultSet rs = pstmt.executeQuery();) { // 문장을 db에서 실행. 만약 먼저 실행까지 처리해버리면 ?인 상태로 대입되니 에러가 뜸
                if (rs.next()) {
                    board = this.setBoard(rs);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return board;
    }

    public int getSize() {
        int res = 0;

        String sql = "select count(*) from boards";

        try (Connection conn = super.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery();){
            if (rs.next()){
                res = rs.getInt("1"); // 컬럼숫자 or 컬럼명 둘다 가능. // 여기선 count(*)이므로 primary key를 지정하는게 좋음.
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    public int deleteOne(String con) {
        int cnt = 0;
        int res = 0;

        String sql1 = "select count(*) from boards where content = ?";
        String sql2 = "delete from boards where content = ?";

        try (Connection conn = super.getConnection();
             PreparedStatement pstmt1 = conn.prepareStatement(sql1);
             ResultSet rs = pstmt1.executeQuery();){
            if (rs.next()) {
                cnt = rs.getInt("1");
            }

            try (PreparedStatement pstmt2 = conn.prepareStatement(sql2)){
                if (cnt == 1) {
                    pstmt2.setString(1, con);
                    res = pstmt2.executeUpdate();
                } else if (cnt > 1){
                    res = cnt;
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return res;
    }
}
