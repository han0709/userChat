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
			
		// ���⸦ �ٲ��ָ� �ٿ�޴� ��ΰ� �ٲ�
		String savePath = "images";
		
		// �ִ� ���ε� ���� ũ�� 5MB�� ����
		int uploadFileSizeLimit = 5 * 1024 * 1024;
		// ���ڵ� ���
		String encType = "UTF-8";
		
		ServletContext context = getServletContext();
		String uploadFilePath = context.getRealPath(savePath);
		System.out.println("�������� ���� ���丮 :");
		System.out.println(uploadFilePath);
		String fileName = null;
		
		try {
			//���� ���ε�Ǵ� ����
			MultipartRequest multi = new MultipartRequest(request, // request ��ü
					uploadFilePath, // �������� ���� ���丮
					uploadFileSizeLimit, // �ִ� ���ε� ���� ũ��
					encType, // ���ڵ� ���					
					new DefaultFileRenamePolicy()); // ������ �̸��� �����ϸ� ���ο� �̸��� �ο���
			
			// ���ε�� ������ �̸� ���
			
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
			
			if (fileName == null) { // ������ ���ε� ���� �ʾ�����
				System.out.print("���� ���ε� ���� �ʾ���");
				
			} else { // ������ ���ε� �Ǿ�����
				System.out.println("���� ���ε� ����");
			}// else
		} catch (Exception e) {
			System.out.print("���� �߻� : " + e);
		}// catch	
		
		
		System.out.println("userAge ModUpdate == "+userAge);
		
		UserDAO userDAO = new UserDAO();
		userDAO.modUpdate(userID, userName, userAge, userGender, userEmail, userProfile, userText);
		
		response.sendRedirect("index.jsp");
		
	}

}
