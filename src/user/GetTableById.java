package user;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/GetTableById")
public class GetTableById extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");

		String userID = request.getParameter("userID");
		UserDAO userDAO = new UserDAO();
		UserDTO userDTO = userDAO.modForm(userID);
		String UserProfile= userDTO.getUserProfile();
		
		if(UserProfile == null || UserProfile == "") {
			UserProfile = "icon.png";
		}
		response.getWriter().write(UserProfile);
		
	}

}
