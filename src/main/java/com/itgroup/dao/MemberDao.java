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

    // 3~5단계 : PreparedStatement(sql) 입력하기, ResultSet 받아오기, close();
    public int getSize() {
        String sql = "select count(*) as cnt from members";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int cnt = 0;

        try {
            conn = this.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

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
//        Connection conn = null;          // try(괄호 안)에서 선언하는 편이 간결하며
//        PreparedStatement pstmt = null;  // finally 구문으로 따로 close();해줄 필요도 없음
//        ResultSet rs = null;             // 더불어 return도 finally 이후로 뺄 필요 없이 try 안에 넣을 수 있음

        List<Member> list = new ArrayList<>();

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery();){ // executeQuery() : SELECT 문 실행, 반환타입 ResultSet, rs.get@@로 return

            while (rs.next()){ // 결과가 여러 행이므로 if 대신 while 사용 // rs.next() = rs의 다음 행이 존재하면 (=값이 하나라도 존재하면) true
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
            return list;

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
    }


    public int addOne(Member member) {
        String sql = "insert into members (id, name, password, gender, birth, marriage, salary, address, manager)" +
                " values (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);){

            pstmt.setString(1, member.getId());
            pstmt.setString(2, member.getName());
            pstmt.setString(3, member.getPassword());
            pstmt.setString(4, member.getGender());
            pstmt.setString(5, member.getBirth());
            pstmt.setString(6, member.getMarriage());
            pstmt.setInt(7, member.getSalary());
            pstmt.setString(8, member.getAddress());
            pstmt.setString(9, member.getManager());

            int res = pstmt.executeUpdate(); // INSERT, UPDATE, DELETE 문 실행, 반환타입 int, 정수를 return
            // 입력값을 받은 뒤에 실행해야 하므로 setString 뒤에 배치
            // *** executeUpdate()의 경우 int로 반환하므로 따로 close할 필요가 없음

            return res; // executeUpdate() 값을 리턴하여 1이 나오면 등록 성공임을 알 수 있도록 설정

        } catch (SQLException e) {
            if ("23000".equals(e.getSQLState())) { // primary key(=id)가 중복되는 경우 SQLException 발생
                System.out.println("이미 존재하는 회원 ID입니다.");
            }
            throw new RuntimeException(e);
        }
    }


    public Member deleteOne(String searchId) {
        Member member = findById(searchId); // findById 메소드로 정보 조회
        if (member == null) { // findById가 null을 반환한 경우 (=해당 회원이 없는 경우)
            return null;
        }

        String sql = "delete from members where id = ?";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, searchId);
            pstmt.executeUpdate(); // 삭제 실행

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return member; // 조회된(삭제된) 회원 정보 반환
    }


    public Member findById(String searchId) {
        String sql = "select * from members where id = ?";
        Member member = null;
        ResultSet rs = null;

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);){

            pstmt.setString(1, searchId);
            rs = pstmt.executeQuery();

            if (rs.next()){
                String id = rs.getString("id");
                String name = rs.getString("name");
                String password = rs.getString("password");
                String gender = rs.getString("gender");
                String birth = rs.getString("birth");
                String marriage = rs.getString("marriage");
                int salary = rs.getInt("salary");
                String address = rs.getString("address");
                String manager = rs.getString("manager");

                member = new Member(id, name, password, gender, birth, marriage, salary, address, manager);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);

        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return member;
    }


    public Member updateSalary(String searchId, int newSalary) {
        Member member = findById(searchId);
        if (member == null) {
            return null;
        }

        String sql = "update members set salary = ? where id = ?";

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(2, searchId);
            pstmt.setInt(1, newSalary);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return findById(searchId); // member를 리턴하면 수정 전 대입했던 정보를 반환하게 되므로, 바뀐 회원정보를 findById로 다시 불러와서 리턴
    }


    public List<Member> findByGender(String gen) {
        String sql = "select * from members where gender = ?";

        List<Member> list = new ArrayList<>();
        ResultSet rs = null;

        try (Connection conn = this.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);){

            pstmt.setString(1, gen);
            rs = pstmt.executeQuery();

            while (rs.next()){
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
                list.add(member);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);

        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return list;
    }
}
