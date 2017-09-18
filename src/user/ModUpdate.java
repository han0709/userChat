package user;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.SendResult;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

@WebServlet("/ModUpdate")
public class ModUpdate extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		String userID = null;
		String userName = null;
		String userAge = null;
		String userGender = null;
		String userEmail = null;
		String userProfile = null;
		String userText = null;
			
		// 여기를 바꿔주면 다운받는 경로가 바뀜
		String savePath = "images";
		
		// 최대 업로드 파일 크기 5MB로 제한
		int uploadFileSizeLimit = 5 * 1024 * 1024;
		// 인코딩 방법
		String encType = "UTF-8";
		
		ServletContext context = getServletContext();
		String uploadFilePath = context.getRealPath(savePath);
		System.out.println("서버상의 실제 디렉토리 :");
		System.out.println(uploadFilePath);
		String fileName = null;
		
		try {
			//파일 업로드되는 시점
			MultipartRequest multi = new MultipartRequest(request, // request 객체
					uploadFilePath, // 서버상의 실제 디렉토리
					uploadFileSizeLimit, // 최대 업로드 파일 크기
					encType, // 인코딩 방법					
					new DefaultFileRenamePolicy()); // 동일한 이름이 존재하면 새로운 이름이 부여됨
			
			// 업로드된 파일의 이름 얻기
			
			fileName = multi.getFilesystemName("userProfile");
			
			 userID = multi.getParameter("userID");
			 userName = multi.getParameter("userName");
			 userAge = multi.getParameter("userAge");
			 userGender = multi.getParameter("userGender");
			 userEmail = multi.getParameter("userEmail");
			 userText = multi.getParameter("userText");
			 
			 if(fileName == null || fileName == "") {
					userProfile =  multi.getParameter("oriImg");
				}else {
					userProfile = fileName;
				}
			
			if (fileName == null) { // 파일이 업로드 되지 않았을때
				System.out.print("파일 업로드 되지 않았음");
				
			} else { // 파일이 업로드 되었을때
				System.out.println("파일 업로드 성공");
			}// else
		} catch (Exception e) {
			System.out.print("예외 발생 : " + e);
		}// catch	
		
		
		System.out.println("userAge ModUpdate == "+userAge);
		
		UserDAO userDAO = new UserDAO();
		userDAO.modUpdate(userID, userName, userAge, userGender, userEmail, userProfile, userText);
		
		response.sendRedirect("index.jsp");
		
	}

}
