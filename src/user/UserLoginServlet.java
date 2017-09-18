package user;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/UserLoginServlet")
public class UserLoginServlet extends HttpServlet {
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		String userID = request.getParameter("userID");
		String userPassword = request.getParameter("userPassword");

		
		if(userID==null || userID=="" || userPassword==null || userPassword=="") {
			request.getSession().setAttribute("messageType", "오류메세지");
			request.getSession().setAttribute("messageContent", "모든 내용을 입력해 주세요");
			response.sendRedirect("login.jsp");
			return;
		}
		
		int result = new UserDAO().login(userID, userPassword);
		System.out.println("result =="+result);
		
		if(result == 1) {
			request.getSession().setAttribute("userID", userID);
			request.getSession().setAttribute("messageType", "성공메세지");
			request.getSession().setAttribute("messageContent", "로그인에 성공 하였습니다.");
			response.sendRedirect("index.jsp");
			return;
		}else if(result == 2) {
			request.getSession().setAttribute("messageType", "오류메세지");
			request.getSession().setAttribute("messageContent", "비밀번호를 다시 입력 하세요.");
			response.sendRedirect("login.jsp");
			return;
		}else if(result == 0) {
			request.getSession().setAttribute("messageType", "오류메세지");
			request.getSession().setAttribute("messageContent", "아이디가 존재하지 않습니다.");
			response.sendRedirect("login.jsp");
			return;
		}else if(result == -1) {
			request.getSession().setAttribute("messageType", "오류메세지");
			request.getSession().setAttribute("messageContent", "데이터베이스 오류가 발생 하였습니다.");
			response.sendRedirect("login.jsp");
			return;
		}
		
	}

}
