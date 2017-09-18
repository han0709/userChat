package chat;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class GetUnreadById
 */
@WebServlet("/GetUnreadById")
public class GetUnreadById extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		String userID = request.getParameter("userID");
		
		ChatDAO chatDAO = new ChatDAO();
		
		
		StringBuffer result = new StringBuffer("");
		result.append("{\"result\":[");
		
		ArrayList<ChatDTO> chatList = chatDAO.getUnreadById(userID);
		
		if(chatList.size() == 0) response.getWriter().write("");
		for(int i=0; i<chatList.size(); i++) {
			//ArrayList<ChatDTO> chatList �� 0��° �迭���� ���� �������� ������.
			result.append("[{\"value\": \""+ chatList.get(i).getFromID() + "\"},");
			result.append("{\"value\": \""+ chatList.get(i).getToID() + "\"},");
			result.append("{\"value\": \""+ chatList.get(i).getChatContent() + "\"},");
			result.append("{\"value\": \""+ chatList.get(i).getChatTime() + "\"}]");
			
			//get(i)�� �迭�̰� chatList.size()�� 1���� �����ϱ� ������ -1�� �� �־����
			if(i != chatList.size()-1) {
				result.append(",");//���������� ,�� ���� ����
			}

		}
		result.append("],\"last\":\""+ chatList.get(chatList.size()-1).getChatID()+"\"}");
			System.out.println("result ===== "+result.toString());

		response.getWriter().write(result.toString()+"");
	}

}
