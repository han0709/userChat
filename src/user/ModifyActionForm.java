package user;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/modifyActionForm")
public class ModifyActionForm extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userIDforMod = request.getParameter("userIDforMod");
		UserDAO userDAO = new UserDAO();
		
		//결과값만 가져오기때문에 메모리상에 올리지 않아도 된다(new userDTO());
	    UserDTO userDTO = userDAO.modForm(userIDforMod);
		
		request.setAttribute("userDTO", userDTO);
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("modifyForm.jsp");
		dispatcher.forward(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

}
