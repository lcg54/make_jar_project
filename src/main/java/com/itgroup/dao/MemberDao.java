package com.itgroup.dao;

import com.itgroup.bean.Member;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// 데이터베이스와 직접 연동하여 CRUD 작업을 수행하는 DAO 클래스
public class MemberDao extends SuperDao{
    public MemberDao() {
        super(); // 안써도 암시적으로 호출됨
    }
    
    // 3~5단계 : PreparedStatement(sql) 입력하기, ResultSet 받아오기, close();
    public int getSize() {
        String sql = "select count(*) as cnt from members";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int cnt = 0;

        try {
            conn = super.getConnection(); // 어차피 해당 메소드를 상속받고 있으므로 this로 써도 상관없으나, 해당 클래스에 보이지 않으니 타인에게 혼선을 줄 수 있으므로 super로 기입하는 편
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                cnt = rs.getInt("cnt"); // 실행 결과를 int값으로 반환
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);

        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
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

        try (Connection conn = super.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery();) { // executeQuery() : SELECT 문 실행, 반환타입 ResultSet(객체)

            while (rs.next()) { // 결과가 여러 행이므로 if 대신 while 사용 // rs.next() = rs의 다음 행이 존재하면 (=값이 하나라도 존재하면) true
                String id = rs.getString("id");
                String name = rs.getString("name");
                String password = rs.getString("password"); // rs.get반환타입("컬럼명")
                String gender = rs.getString("gender");     // set처럼 컬럼순서(숫자)를 입력해도 되긴 함
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
        int cnt = 0;

        try (Connection conn = super.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {

            pstmt.setString(1, member.getId()); //  준비된문장.set반환타입(물음표 순서, 대입할거)
            pstmt.setString(2, member.getName());
            pstmt.setString(3, member.getPassword());
            pstmt.setString(4, member.getGender());
            pstmt.setString(5, member.getBirth());
            pstmt.setString(6, member.getMarriage());
            pstmt.setInt(7, member.getSalary());
            pstmt.setString(8, member.getAddress());
            pstmt.setString(9, member.getManager());

            cnt = pstmt.executeUpdate(); // INSERT, UPDATE, DELETE 문 실행, 반환타입 int
            // 입력값을 받은 뒤에 실행해야 하므로 setString 뒤에 배치
            // *** executeUpdate()의 경우 int로 반환하므로 따로 close할 필요가 없음

            conn.commit(); // 커밋

        } catch (SQLException e) {
            if ("23000".equals(e.getSQLState())) { // primary key(=id)가 중복되는 경우 SQLException 발생
                System.out.println("이미 존재하는 회원 ID입니다.");
            }
            throw new RuntimeException(e);
        }
        return cnt; // executeUpdate() 값을 리턴하여 1이 나오면 등록 성공임을 알 수 있도록 설정
    }


    public Member deleteOne(String searchId) {
        Member member = findById(searchId); // findById 메소드로 정보 조회
        if (member == null) { // findById가 null을 반환한 경우 (=해당 회원이 없는 경우)
            return null;
        }

        String sql = "delete from members where id = ?";

        try (Connection conn = super.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, searchId);
            pstmt.executeUpdate(); // 삭제 실행

            conn.commit();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return member; // 조회된(삭제된) 회원 정보 반환
    }


    public Member findById(String searchId) {
        String sql = "select * from members where id = ?";
        Member member = null;
        ResultSet rs = null;

        try (Connection conn = super.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {

            pstmt.setString(1, searchId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                String id = rs.getString("id"); //
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
            }
        }
        return member;
    }


    public List<Member> findByGender(String gen) {
        String sql = "select * from members where gender = ?";

        List<Member> list = new ArrayList<>();
        ResultSet rs = null;

        try (Connection conn = super.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {

            pstmt.setString(1, gen);
            rs = pstmt.executeQuery();

            while (rs.next()) {
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
            }
        }
        return list;
    }


    public Member updateOne(String searchId, Member updateMember) {
        Member member = findById(searchId);
        if (member == null) {
            return null;
        }
        
        String sql = "update members set name = ?, password = ?, gender = ?, birth = ?, marriage = ?, salary = ?, address = ?, manager = ? where id = ?";
        
        try (Connection conn = super.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {

            String name = updateMember.getName();
            if (name == null || name.isBlank()) { // 값이 null이거나 공백인(그냥 엔터친) 경우 기존의 값을 할당.
                name = member.getName();
            }

            String password = updateMember.getPassword();
            if (password == null || password.isBlank()) {
                password = member.getPassword();
            }

            String gender = updateMember.getGender();
            if (gender == null || gender.isBlank()) {
                gender = member.getGender();
            }

            String birth = updateMember.getBirth();
            if (birth == null || birth.isBlank()) {
                birth = member.getBirth();
            }

            String marriage = updateMember.getMarriage();
            if (marriage == null || marriage.isBlank()) {
                marriage = member.getMarriage();
            }

            int salary = updateMember.getSalary();
            if (salary == 0) {
                salary = member.getSalary();
            }

            String address = updateMember.getAddress();
            if (address == null || address.isBlank()) {
                address = member.getAddress();
            }

            String manager = updateMember.getManager();
            if (manager == null || manager.isBlank()) {
                manager = member.getManager();
            }

            pstmt.setString(1, name);
            pstmt.setString(2, password);
            pstmt.setString(3, gender);
            pstmt.setString(4, birth);
            pstmt.setString(5, marriage);
            pstmt.setInt(6, salary);
            pstmt.setString(7, address);
            pstmt.setString(8, manager);
            pstmt.setString(9, searchId);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return findById(searchId); // 수정된 정보 다시 조회해서 반환
    }
}
