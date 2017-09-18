package chat;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/GetUnreadMessages")
public class GetUnreadMessages extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userID = request.getParameter("userID");
		
		ChatDAO chatDAO = new ChatDAO();
		int count = chatDAO.getAllUnreadChat(userID);

		response.getWriter().write(count+"");
	}

}
