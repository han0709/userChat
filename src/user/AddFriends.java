package user;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import chat.ChatDAO;

@WebServlet("/AddFriends")
public class AddFriends extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		String userID = request.getParameter("userID");
		String toID= request.getParameter("toID");
		ChatDAO chatDAO = new ChatDAO();
		
		int confirmFriends = chatDAO.confirmFriends(userID, toID);
		//System.out.println("confirmFriends === "+confirmFriends);
		
		if(confirmFriends == 1) {
			response.getWriter().write(chatDAO.addFriends(userID, toID)+"");
			return;
			
		}else {
			response.getWriter().write(0+"");
			return;
		}
		
		
	}

}
