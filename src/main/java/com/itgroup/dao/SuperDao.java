package com.itgroup.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SuperDao {
    // 1단계 : 드라이버 로딩
    // 변수 drive는 JDBC를 위한 드라이버의 이름으로, ojdbc숫자.jar라는 파일 안에 포함되어 있는 자바 클래스 이름
    // 일반적으로 생성자 내에 구현 (자동 실행)
    public SuperDao() {
        String driver = "oracle.jdbc.driver.OracleDriver";
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
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

}
