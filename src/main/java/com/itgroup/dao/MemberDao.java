package com.itgroup.dao;

import com.itgroup.bean.Member;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// 데이터베이스와 직접 연동하여 CRUD 작업을 수행하는 DAO 클래스
public class MemberDao {

    // 1단계 : 드라이버 로딩
    // 변수 drive는 JDBC를 위한 드라이버의 이름으로, ojdbc숫자.jar라는 파일 안에 포함되어 있는 자바 클래스 이름
    // 일반적으로 생성자 내에 구현 (자동 실행)
    public MemberDao() {
        String driver = "oracle.jdbc.driver.OracleDriver";
        try {
            Class.forName(driver); // driver라는 문자열을 클래스화시켜서 메모리에 로딩
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e); // 해당 클래스가 존재하지 않을 경우 예외처리
        }
    }

    // 2단계 : 커넥션 객체 생성
    public Connection getConnection() {
        String url = "jdbc:oracle:thin:@localhost:1521:xe";
        String id = "oraman";
        String password = "oracle";

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, id, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return conn;
    }

    public int getSize() {
        String sql = "select count(*) as cnt from members";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int cnt = 0;

        try {
            conn = this.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery(); // executeQuery() : SELECT 문 실행 // executeUpdate() : 나머지 (INSERT 등) 실행
            if (rs.next()){
                cnt = rs.getInt("cnt"); // 실행 결과를 int값으로 반환
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);

        } finally {
            try {
                if(rs != null){rs.close();}
                if(pstmt != null){pstmt.close();}
                if(conn != null){conn.close();}
            } catch(Exception ex) {
            }
        }

        return cnt;
    }

    public List<Member> selectAll() {
        String sql = "select * from members";
//        Connection conn = null;  // try 내부에서 선언하는 편이 간결하며, finally 구문으로 따로 close();해줄 필요도 없음
//        PreparedStatement pstmt = null;
//        ResultSet rs = null;
        List<Member> list = new ArrayList<>();

        try {
            Connection conn = this.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){ // 결과가 여러 행이므로 if 대신 while 사용
                String id = rs.getString("id");
                String name = rs.getString("name");
                String password = rs.getString("password");
                String gender = rs.getString("gender");
                String birth = rs.getString("birth");
                String marriage = rs.getString("marriage");
                int salary = rs.getInt("salary");
                String address = rs.getString("address");
                String manager = rs.getString("manager");

                Member member = new Member(id, name, password, gender, birth, marriage, salary, address, manager);
                list.add(member); // 새로 리스트를 만들어서 받아온 값을 하나하나 저장하는 개념
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);

//        } finally {
//            try {
//                if(rs != null){rs.close();}
//                if(pstmt != null){pstmt.close();}
//                if(conn != null){conn.close();}
//            } catch(Exception ex) {
//            }
        }

        return list;
    }
}
