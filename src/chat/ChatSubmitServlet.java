package chat;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/ChatSubmitServlet")
public class ChatSubmitServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		//System.out.println("ChatSubmitServlet======================");
		
		String fromID = request.getParameter("fromID");
		String toID = request.getParameter("toID");
		String chatContent = request.getParameter("chatContent");
		if(fromID == null || fromID == "" || toID == null || toID == "" || chatContent == null || chatContent == "") {
			response.getWriter().write("0");
			
		}else {
			fromID = URLDecoder.decode(fromID, "UTF-8");
			toID = URLDecoder.decode(toID, "UTF-8");
			chatContent = URLDecoder.decode(chatContent, "UTF-8");
			//데이터베이스에 채팅 내용을 저장해줌
			response.getWriter().write(new ChatDAO().submit(fromID, toID, chatContent)+"");//1을 반환받음 (데이터베이스 오류시 -1)
			
		}
		
	}

}
