package user;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import chat.ChatDAO;
import chat.ChatDTO;

@WebServlet("/AddProText")
public class AddProText extends HttpServlet {
	private static final long serialVersionUID = 1L;       

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		String userID = request.getParameter("userID");
		
		response.getWriter().write(makeLists(userID));
		
		
		
	}
	
	private String makeLists(String userID) {
		
		StringBuffer result = new StringBuffer("");
		result.append("{\"result\":[");
		
		UserDAO userDAO = new UserDAO();
	    ArrayList<UserDTO> lists = userDAO.addProText(userID);

		if(lists.size() == 0) return "";
		for(int i=0; i<lists.size(); i++) {
			result.append("[{\"value\": \""+ lists.get(i).getUserProfile() + "\"},");
			result.append("{\"value\": \""+ lists.get(i).getUserText() + "\"}]");
			if(i != lists.size()-1) {
				result.append(",");
			}
		}
		result.append("]}");
		System.out.println(result.toString());
		return result.toString();
	}

}







