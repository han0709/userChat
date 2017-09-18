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
	
	//�α��� üũ
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
					return 1;//�α��� ����
				}
				return 2;//��й�ȣ�� Ʋ��
			}else {
				return 0;//�ش� ����ڰ� �������� ����
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
		return -1;//�����ͺ��̽� ����
	}
	
	
	
	//ȸ������ �ߺ�üũ
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
				return 0;//�̹� �����ϴ� ���̵�ų� ������ ���̵���.
			}else {
				return 1;//���� ������ ���̵���.
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
		return -1;//�����ͺ��̽� ����
	}
	
	//ȸ������ �����ͺ��̽��� �Է�
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
			return -1;//�����ͺ��̽� ����
		}
	
	//ȸ���� ���� ��
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
		
		//�����ʻ���, ���渻
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
