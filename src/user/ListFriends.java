package user;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import chat.ChatDAO;


@WebServlet("/ListFriends")
public class ListFriends extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		String userID = request.getParameter("userID");
		
		response.getWriter().write(getIDs(userID));	
		
	}
	
	public String getIDs(String userID) {
		ChatDAO chatDAO = new ChatDAO();
		ArrayList<String> lists = new ArrayList<>();
		lists = chatDAO.listFriends(userID);
		
		StringBuffer result = new StringBuffer("");
		result.append("{\"result\":[");
		
		if(lists.size() == 0) return "";
		for(int i=0; i<lists.size(); i++) {
			result.append("[{\"value\": \""+ lists.get(i) + "\"}]");

			
			//get(i)�� �迭�̰� chatList.size()�� 1���� �����ϱ� ������ -1�� �� �־����
			if(i != lists.size()-1) {
				result.append(",");//���������� ,�� ���� ����
			}
			//System.out.println("chatList.get(i).getFromID() == "+lists.get(i).getFromID());
			//System.out.println("chatList.size() == "+lists.size());
		}
		result.append("]}");
			System.out.println("result ===== "+result);

		return result.toString();		
		
	}

}
