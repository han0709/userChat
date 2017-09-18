package chat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import user.UserDTO;

public class ChatDAO {
	DataSource dataSource;
	
	public ChatDAO() {
		try {
			InitialContext initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			dataSource  = (DataSource) envContext.lookup("jdbc/UserChat");
		
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//채팅내용 가져오기
	public ArrayList<ChatDTO> getChatListById(String fromID,String toID,String chatID){
		ArrayList<ChatDTO> chatList =null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		//채팅을 보낸사람이든 받은사람이든 두 경우중 하나에 해당될때 모든 챗 데이터베이스를 가져옴.
		String SQL = "SELECT * FROM CHAT WHERE ((fromID = ? AND toID = ?) OR (fromID = ? AND toID = ?)) AND chatID > ? ORDER BY chatTime";
		
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, fromID);
			pstmt.setString(2, toID);
			pstmt.setString(3, toID);
			pstmt.setString(4, fromID);
			pstmt.setInt(5, Integer.parseInt(chatID));
			rs = pstmt.executeQuery();
			chatList = new ArrayList<ChatDTO>();
			while(rs.next()) {
				ChatDTO chat = new ChatDTO();
				chat.setChatID(rs.getInt("chatID"));
				chat.setFromID(rs.getString("fromID").replaceAll("<", "&lt;").replaceAll(">", "&gt").replaceAll("\n", "<br>"));
				chat.setToID(rs.getString("toID").replaceAll("<", "&lt;").replaceAll(">", "&gt").replaceAll("\n", "<br>"));
				chat.setChatContent(rs.getString("chatContent").replaceAll("<", "&lt;").replaceAll(">", "&gt").replaceAll("\n", "<br>"));
				int chatTime = Integer.parseInt(rs.getString("chatTime").substring(11,13));
				
				System.out.println("rs.getString(\"chatTime\") ==== "+rs.getString("chatTime"));
				System.out.println("chatTime ==== "+chatTime);
				
				String timeType = "오전";
				if(chatTime >= 12) {
					timeType = "오후";
					chatTime -= 12;//12를 빼준다
				}
				chat.setChatTime(rs.getString("chatTime").substring(0,11)+" "+chatTime+":"+rs.getString("chatTime").substring(14,16)+"");
				chatList.add(chat);
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
		return chatList;
		
	}//getChatListById
	
	//대화내용중 최근의 몇개만 뽑아서 가져옴.
	public ArrayList<ChatDTO> getChatListByRecent(String fromID,String toID, int number){
		ArrayList<ChatDTO> chatList =null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		//채팅을 보낸사람이든 받은사람이든 두 경우중 하나에 해당될때 모든 챗 데이터베이스를 가져옴.
		String SQL = "SELECT * FROM CHAT WHERE ((fromID = ? AND toID = ?) OR (fromID = ? AND toID = ?)) AND chatID > "
				+ "(SELECT MAX(chatID) - ? FROM CHAT WHERE (fromID = ? AND toID = ?) OR (fromID = ? AND toID = ?)) ORDER BY chatTime";
		
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, fromID);
			pstmt.setString(2, toID);
			pstmt.setString(3, toID);
			pstmt.setString(4, fromID);
			pstmt.setInt(5, number);
			pstmt.setString(6, fromID);
			pstmt.setString(7, toID);
			pstmt.setString(8, toID);
			pstmt.setString(9, fromID);
			rs = pstmt.executeQuery();
			chatList = new ArrayList<ChatDTO>();
			while(rs.next()) {
				ChatDTO chat = new ChatDTO();
				chat.setChatID(rs.getInt("chatID"));
				chat.setFromID(rs.getString("fromID").replaceAll("<", "&lt;").replaceAll(">", "&gt").replaceAll("\n", "<br>"));
				
				chat.setToID(rs.getString("toID").replaceAll("<", "&lt;").replaceAll(">", "&gt").replaceAll("\n", "<br>"));
				
				chat.setChatContent(rs.getString("chatContent").replaceAll("<", "&lt;").replaceAll(">", "&gt").replaceAll("\n", "<br>"));
				
				int chatTime = Integer.parseInt(rs.getString("chatTime").substring(11,13));
				String timeType = "오전";
				if(chatTime >= 12) {
					timeType = "오후";
					chatTime -= 12;//12를 빼준다
				}
				chat.setChatTime(rs.getString("chatTime").substring(0,11)+" "+chatTime+":"+rs.getString("chatTime").substring(14,16)+"");
				chatList.add(chat);
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
		return chatList;
		
	}//getChatListByRecent
	
	//대화를 보내는 기능.
		public int submit(String fromID,String toID, String chatContent){

			Connection conn = null;
			PreparedStatement pstmt = null;
			
			String SQL = "INSERT INTO CHAT VALUES (NULL, ?, ?, ?, NOW(), 0)";//맨뒤의 0은 안읽음을 의미한다.
			
			try {
				conn = dataSource.getConnection();
				pstmt = conn.prepareStatement(SQL);
				pstmt.setString(1, fromID);
				pstmt.setString(2, toID);
				pstmt.setString(3, chatContent);
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
		}//submit
		
		
		//채팅창을 읽었을 경우
		public int readChat(String fromID, String toID) {
			Connection conn = null;
			PreparedStatement pstmt = null;
			
			String SQL = "UPDATE CHAT SET chatRead = 1 WHERE (fromID = ? AND toID = ?)";
			
			try{
				conn = dataSource.getConnection();
				pstmt = conn.prepareStatement(SQL);
				pstmt.setString(1, toID);
				pstmt.setString(2, fromID);
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
			return -1;
		}
		
		//읽지않은 총 갯수를 가져옴
		public int getAllUnreadChat(String userID) {
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			String SQL = "SELECT COUNT(chatID) FROM CHAT WHERE toID = ? AND chatRead = 0";
			
			try{
				conn = dataSource.getConnection();
				pstmt = conn.prepareStatement(SQL);
				pstmt.setString(1, userID);
				rs =pstmt.executeQuery();
				if(rs.next()) {
					return rs.getInt("COUNT(chatID)");
				}
				return 0;
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
			return -1;
		}
		
		//읽지않은 각각 아이디의 갯수를 가져옴
		public int getUnreadByIdEach(String userID,String toID) {
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			System.out.println("userID ========$ "+userID);
			System.out.println("toID ========$ "+toID);
			
			String SQL = "SELECT COUNT(chatID) FROM CHAT WHERE (toID = ? and fromID = ?) AND chatRead = 0";
			
			try{
				conn = dataSource.getConnection();
				pstmt = conn.prepareStatement(SQL);
				pstmt.setString(1, userID);
				pstmt.setString(2, toID);
				rs =pstmt.executeQuery();
				if(rs.next()) {
					return rs.getInt("COUNT(chatID)");
					
				}
				return 0;
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
			return -1;
		}
		
	//나와 대화를 나눈 사람들을 가져옴.(로그인사람이 메세지를 받는 입장이므로 toID에 와야함)
		public ArrayList<ChatDTO> getUnreadById(String userID){
			ArrayList<ChatDTO> chatList =null;
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			System.out.println("getUnreadById + userID ==== "+userID);
			//보낸사람이 userID이거나 받는사람이 userID(로그인한자)일경우 다 가져옴
			String SQL = "SELECT * FROM CHAT WHERE (fromID = ? or toID = ?)  ORDER BY chatTime desc";
			
			try {
				conn = dataSource.getConnection();
				pstmt = conn.prepareStatement(SQL);
				pstmt.setString(1, userID);
				pstmt.setString(2, userID);
				rs = pstmt.executeQuery();
				chatList = new ArrayList<ChatDTO>();
				while(rs.next()) {
					ChatDTO chat = new ChatDTO();
					chat.setChatID(rs.getInt("chatID"));
					chat.setFromID(rs.getString("fromID").replaceAll("<", "&lt;").replaceAll(">", "&gt").replaceAll("\n", "<br>"));
					chat.setToID(rs.getString("toID").replaceAll("<", "&lt;").replaceAll(">", "&gt").replaceAll("\n", "<br>"));
					chat.setChatContent(rs.getString("chatContent").replaceAll("<", "&lt;").replaceAll(">", "&gt").replaceAll("\n", "<br>"));
					int chatTime = Integer.parseInt(rs.getString("chatTime").substring(11,13));
					
					System.out.println("rs.getString(\"chatTime\") ==== "+rs.getString("chatTime"));
					System.out.println("chatTime ==== "+chatTime);
					
					String timeType = "오전";
					if(chatTime >= 12) {
						timeType = "오후";
						chatTime -= 12;//12를 빼준다
					}
					chat.setChatTime(rs.getString("chatTime").substring(0,11)+" "+chatTime+":"+rs.getString("chatTime").substring(14,16)+"");
					chatList.add(chat);
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
			return chatList;
			
		}//getChatListById 
	
	//친구 검색
		public int confirmFriends(String userID, String toID){
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			int retrun_num = -1;

			String SQL = "SELECT * FROM friends where userID = ? and friend = ? limit 1";
			
			try {
				conn = dataSource.getConnection();
				pstmt = conn.prepareStatement(SQL);
				pstmt.setString(1, userID);
				pstmt.setString(2, toID);
				System.out.println("userID === "+userID);
				System.out.println("toID === "+toID);
				
				rs = pstmt.executeQuery();
				if(rs.next()) {
					retrun_num = 0;
				}else {
					retrun_num = 1;
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
			return retrun_num;
			
		}//confirmFriends 
		
    //친구추가
		public int addFriends(String userID,String toID){

			Connection conn = null;
			PreparedStatement pstmt = null;
			
			String SQL = "INSERT INTO friends VALUES (?, ?)";

			try {
				conn = dataSource.getConnection();
				pstmt = conn.prepareStatement(SQL);
				pstmt.setString(1, userID);
				pstmt.setString(2, toID);
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
		}//addFriends
		
	//친구 리스트
		public ArrayList<String> listFriends(String userID){
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			ArrayList<String> lists = new ArrayList<>();

			String SQL = "SELECT * FROM friends where userID = ?";
			
			try {
				conn = dataSource.getConnection();
				pstmt = conn.prepareStatement(SQL);
				pstmt.setString(1, userID);
				System.out.println("userID === "+userID);
				
				rs = pstmt.executeQuery();
				
				while(rs.next()) {
					String friendsL = rs.getString("friend");
					lists.add(friendsL);
				}
				System.out.println(lists);
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
			
		}//listFriends 
	
	//회원 수정 form
		public UserDTO userFormList(String userID){
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			UserDTO userDTO = new UserDTO();

			String SQL = "SELECT * FROM user where userID = ?";
			
			try {
				conn = dataSource.getConnection();
				pstmt = conn.prepareStatement(SQL);
				pstmt.setString(1, userID);
				System.out.println("userID === "+userID);
				
				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					
					userDTO.setUserID(rs.getString("userID"));
					userDTO.setUserName(rs.getString("userName"));
					userDTO.setUserAge(rs.getInt("userAge"));
					userDTO.setUserGender(rs.getString("userGender"));
					userDTO.setUserEmail(rs.getString("userEmail"));
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
					
		}
	
}







