<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.sql.*, javax.sql.*, java.io.*, javax.naming.InitialContext, javax.naming.Context" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%
		InitialContext initCtx = new InitialContext();
		Context envContext = (Context)initCtx.lookup("java:/comp/env");
		DataSource ds = (DataSource)envContext.lookup("jdbc/UserChat");
		Connection conn = ds.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rset = stmt.executeQuery("SELECT VERSION();");
		while(rset.next()){
			out.println("MySQL Version : "+rset.getString("version()"));
		}
		rset.close();
		stmt.close();
		conn.close();
		initCtx.close();
	%>
</body>
</html>