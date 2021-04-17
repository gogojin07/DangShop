package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcUtil {
//jdbc, dao에서 사용할 공통 메소드 정의
	static { // 클래스로딩시 1번만 초기화
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("드라이버 로딩 성공");
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 예외 발생");
			System.out.println(e.getMessage()); // 자세하지 않음
			e.printStackTrace();
		}
	}// static End

	public static Connection getConnection() {
		Connection con=null;
		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:xe", "icia", "1111");
			//수동 커밋으로 설정
			con.setAutoCommit(false);
			System.out.println("connect OK");
		} catch (SQLException e) {
			System.out.println("DB 접속 실패");
			e.printStackTrace();
		}
		return con;  //Connection객체 반환
	}// connect end

	public static void close(ResultSet rs, PreparedStatement pstmt, Connection con) {
		try {
			if(rs!=null) rs.close();
			if(pstmt!=null) pstmt.close();
			if(con!=null) con.close();
		} catch (SQLException e) {
			System.out.println("접속 해제 실패");
			e.printStackTrace();
		}
	}

	public static void commit(Connection con) {
		try {
			con.commit();
		} catch (SQLException e) {
			System.out.println("commit 예외");
			e.printStackTrace();
		}
	}

	public static void rollback(Connection con) {
		try {
			con.rollback();
		} catch (SQLException e) {
			System.out.println("rollback 예외");
			e.printStackTrace();
		}
	}
	
}







