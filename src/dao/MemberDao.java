package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MemberDao {

	Connection con;
	PreparedStatement pstmt;
	ResultSet rs;
	
	public MemberDao() {
		con = JdbcUtil.getConnection();
	}
	
	public void close() {
		JdbcUtil.close(rs, pstmt, con);		
	}
	
	public int access(String id, String pw) {
		String sql = "SELECT * FROM MEMBER2 WHERE ID=?";
		int result = -1;	//id 부재
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setNString(1, id);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {	//아이디 존재
				if(rs.getNString("PW").equals(pw)) {//비번 일치
					result = 1;
				} else {	//비번 불일치
					result = 0;
				}
			}
		} catch (SQLException e) {
			System.out.println("로그인 예외 발생");
			e.printStackTrace();
		}		
		return result;
	}

}
