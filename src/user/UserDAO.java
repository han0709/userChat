package user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class UserDAO {
	DataSource dataSource;
	
	public UserDAO() {
		try {
			InitialContext initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			dataSource  = (DataSource) envContext.lookup("jdbc/UserChat");
		
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//로그인 체크
	public int login(String userID, String userPassword) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String SQL = "SELECT * FROM USER WHERE userID = ?";

		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			rs = pstmt.executeQuery();

			if(rs.next()) {
				if(rs.getString("userPassword").equals(userPassword)) {
					return 1;//로그인 성공
				}
				return 2;//비밀번호가 틀림
			}else {
				return 0;//해당 사용자가 존재하지 않음
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null)pstmt.close();
				if(conn != null) conn.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return -1;//데이터베이스 오류
	}
	
	
	
	//회원가입 중복체크
	public int registerCheck(String userID) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String SQL = "SELECT * FROM USER WHERE userID = ?";
		
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			rs = pstmt.executeQuery();
			if(rs.next() || userID.equals("")) {
				return 0;//이미 존재하는 아이디거나 공백인 아이디임.
			}else {
				return 1;//가입 가능한 아이디임.
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null)pstmt.close();
				if(conn != null) conn.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return -1;//데이터베이스 오류
	}
	
	//회원정보 데이터베이스에 입력
		public int register(String userID, String userPassword, String userName, String userAge, String userGender, String userEmail, String userProfile) {
			Connection conn = null;
			PreparedStatement pstmt = null;
			String SQL = "INSERT INTO USER values(?,?,?,?,?,?,?)";
			
			try {
				conn = dataSource.getConnection();
				System.out.println("conn =========== "+conn);
				pstmt = conn.prepareStatement(SQL);
				pstmt.setString(1, userID);
				pstmt.setString(2, userPassword);
				pstmt.setString(3, userName);
				pstmt.setInt(4, Integer.parseInt(userAge));
				pstmt.setString(5, userGender);
				pstmt.setString(6, userEmail);
				pstmt.setString(7, userProfile);
				return pstmt.executeUpdate();
				
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				try {
					if(pstmt != null)pstmt.close();
					if(conn != null) conn.close();
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
			return -1;//데이터베이스 오류
		}
	
	//회정보 수정 폼
		public UserDTO modForm(String userID) {
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			UserDTO userDTO =  new UserDTO();
			String SQL = "SELECT * FROM USER WHERE userID = ?";
			
			
			try {
				conn = dataSource.getConnection();
				pstmt = conn.prepareStatement(SQL);
				pstmt.setString(1, userID);
				rs = pstmt.executeQuery();
				if(rs.next()) {
					userDTO.setUserID(rs.getString("userID"));
					userDTO.setUserName(rs.getString("userName"));
					userDTO.setUserAge(rs.getInt("userAge"));
					userDTO.setUserGender(rs.getString("userGender"));
					userDTO.setUserEmail(rs.getString("userEmail"));
					userDTO.setUserProfile(rs.getString("userProfile"));
					userDTO.setUserText(rs.getString("userText"));
				}
				
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				try {
					if(rs != null) rs.close();
					if(pstmt != null)pstmt.close();
					if(conn != null) conn.close();
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
			return userDTO;
		}//modForm
		
		public void modUpdate(String userID,String userName,String userAge,String userGender,String userEmail,String userProfile, String userText) {
			Connection conn = null;
			PreparedStatement pstmt = null;
			
			String SQL = "UPDATE user SET userName = ?, userAge = ?, userGender = ?, userEmail = ? ,userProfile = ? ,userText = ? WHERE userID = ?";
			System.out.println("userID == "+userID);
			System.out.println("userName == "+userName);
			System.out.println("userAge == "+userAge);
			try{
				conn = dataSource.getConnection();
				pstmt = conn.prepareStatement(SQL);
				pstmt.setString(1, userName);
				pstmt.setInt(2, Integer.parseInt(userAge));
				pstmt.setString(3, userGender);
				pstmt.setString(4, userEmail);
				pstmt.setString(5, userProfile);
				pstmt.setString(6, userText);
				pstmt.setString(7, userID);
				
				pstmt.executeUpdate();
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				try {
					if(pstmt != null)pstmt.close();
					if(conn != null) conn.close();
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		//프로필사진, 남길말
		public ArrayList<UserDTO> addProText(String userID) {
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			ArrayList<UserDTO> lists = new ArrayList<>();
			
			String SQL = "SELECT * FROM USER WHERE userID = ?";
			
			
			try {
				conn = dataSource.getConnection();
				pstmt = conn.prepareStatement(SQL);
				pstmt.setString(1, userID);
				rs = pstmt.executeQuery();
				if(rs.next()) {
					UserDTO userDTO =  new UserDTO();
					
					userDTO.setUserID(rs.getString("userID"));
					userDTO.setUserName(rs.getString("userName"));
					userDTO.setUserAge(rs.getInt("userAge"));
					userDTO.setUserGender(rs.getString("userGender"));
					userDTO.setUserEmail(rs.getString("userEmail"));
					userDTO.setUserProfile(rs.getString("userProfile"));
					userDTO.setUserText(rs.getString("userText"));
					
					lists.add(userDTO);
				}
				
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				try {
					if(rs != null) rs.close();
					if(pstmt != null)pstmt.close();
					if(conn != null) conn.close();
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
			return lists;
		}//modForm
	
}
