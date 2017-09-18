
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="java.util.*"%>
<%
    request.setCharacterEncoding("UTF-8");
	String userID = null;
	//사용자 접속유무 확인
	if(session.getAttribute("userID") != null){
		userID = (String)session.getAttribute("userID");
	}
	
	String userIDforMod = null;
	if(request.getParameter("userIDforMod") != null){
		userIDforMod = request.getParameter("userIDforMod");
	}
	
 %>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" href="css/bootstrap.css">
	<link rel="stylesheet" href="css/custom.css">
	<title>실시간 회원제 채팅 서비스</title>
	<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
	<script src="js/bootstrap.js"></script>
	<script>
	var userID = "${userID }";

	function getUnreadMessages(userID){
		
		$.ajax({
			type: "POST",
			url: './GetUnreadMessages',
			data: {userID:userID},
			success: function(result){
				$(".count_unread strong").html(result);
			}
			
		});
		
	}
	
	getUnreadMessages(userID);
	</script>
</head>
<body>
	<nav class="navbar navbar-default">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed" data-toggle="collapse"
			data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>			
			</button>
		
			<a class="navbar-brand" href="index.jsp">실시간 회원제 채팅 서비스</a>
		</div>
		<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
			<ul class="nav navbar-nav">
				<li class="active"><a href="index.jsp">메인</a></li>
				<li><a href="find.jsp">친구찾기</a></li>
				<c:if test="${not empty userID}">
				<li><a href="messageBox.jsp" class="count_unread">메시지함 <strong style="color:red;"></strong></a></li>
				
					<li><a href="#none">${userID } 님 어서오세요</a></li>
				</c:if>	
			</ul>
			<%
				if(userID == null){
			%>
			<ul class="nav navbar-nav navbar-right">
				<li class="dropdown">
					<a href="#none" class="dropdown-toggle"
						data-toggle="dropdown" role="button" aria-haspopup="true"
						aria-expanded="false">접속하기<span class="caret"></span>
					</a>
					<ul class="dropdown-menu">
						<li><a href="login.jsp">로그인</a></li>	
						<li><a href="join.jsp">회원가입</a></li>
					</ul>
					
				</li>
			</ul>
			<%		
				}else{
			%>		
			<ul class="nav navbar-nav navbar-right">
				<li class="dropdown">
					<a href="#none" class="dropdown-toggle"
						data-toggle="dropdown" role="button" aria-haspopup="true"
						aria-expanded="false">회원관리<span class="caret"></span>
					</a>
					<ul class="dropdown-menu">
						<li><a href="modifyAction?userIDforMod=${userID }">회원정보 수정</a></li>
						<li><a href="logoutAction.jsp">로그아웃</a></li>
					</ul>
				</li>
			</ul>
			<%	
				}
			%>	

		</div>
	</nav>
	<div class="container">
		<form method="post" action="./userRegister">
			<h4 class="sub_title_han">회원 정보</h4>
			<div class="container_wrap">
				<div class="container_wrap_left">
					<c:if test="${userDTO.userProfile != null}">
						<p><img alt="프로필사진" src="images/${userDTO.userProfile }"></p>
					</c:if>
					<c:if test="${userDTO.userProfile == null || userDTO.userProfile == ''}">
						<p><img alt="프로필사진" src="images/icon.png"></p>
					</c:if>
				</div>
				<table class="table table-bordered table-hover container_wrap_right" style="text-align:center;border:1px solid #ddd;">
					
					<tbody>
						<tr>
							<td style="width:110px;"><h5>아이디</h5></td>
							<td  colspan="2">
								<input class="form-control" type="text" name="userID" id="userID" maxlength="20" 
								value="${userDTO.userID }" readonly/>
							</td>
						</tr>
						<tr>
							<td style="width:110px;"><h5>이름</h5></td>
							<td colspan="2">
								<input class="form-control" type="text" name="userName" id="userName" 
								maxlength="20" value="${userDTO.userName }" readonly/>
							</td>
						</tr>
						<tr>
							<td style="width:110px;"><h5>나이</h5></td>
							<td colspan="2">
								<input class="form-control" type="number" name="userAge" 
								id="userAge" maxlength="20" value="${userDTO.userAge }" readonly/>
							</td>
						</tr>
						<tr>
							<td style="width:110px;"><h5>성별</h5></td>
							<td colspan="2">
								<div class="form-group" style="text-align:center;margin:0 auto">
									<div class="btn-group" data-toggle="buttons">
										<label class="btn btn-primary <c:if test="${userDTO.userGender == 'm'}">active</c:if>">
											<input type="radio" name="userGender" autocomplete="off" 
											value="m" <c:if test="${userDTO.userGender == 'm'}">checked="checked"</c:if>  readonly/>남자
										</label>
										<label class="btn btn-primary <c:if test="${userDTO.userGender == 'w'}">active</c:if>">
											<input type="radio" name="userGender" autocomplete="off" 
											value="w" <c:if test="${userDTO.userGender == 'w'}">checked="checked"</c:if>  readonly/>여자
										</label>
									</div>
								</div>
							</td>
						</tr>
						<tr>
							<td style="width:110px;"><h5>이메일</h5></td>
							<td colspan="2">
								<input class="form-control" type="email" name="userEmail" 
								id="userEmail" maxlength="20" value="${userDTO.userEmail }"  readonly/>
							</td>
						</tr>
						<tr>
							<td style="width:110px;"><h5>프로필사진</h5></td>
							<td colspan="2">
								<input class="form-control" type="text" name="userProfile" 
								id="userProfile" value="${userDTO.userProfile }" readonly/>
							</td>
						</tr>
						<tr>
							<td style="width:110px;"><h5>이메일</h5></td>
							<td colspan="2">
								<input class="form-control" type="text" name="userText" 
								id="userText" maxlength="20" value="${userDTO.userText }"  readonly/>
							</td>
						</tr>
						<tr>
							<td style="text-align:center;" colspan="3">
								<h5 style="color:red;" id="passwordCheckMessage"></h5>
							<% if(userID.equals(userIDforMod)){ %>
								<a href="modifyActionForm?userIDforMod=${param.userIDforMod }" class="btn btn-primary" >수정하기</a>
							<% } %>
								<a href="index.jsp" class="btn btn-primary" >닫기</a>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</form>
	</div>
	
	
	<!-- UserRegisterServlet 모달팝업 -->
	<%
		String messageContent = null;
		if(session.getAttribute("messageContent") != null){
			messageContent = (String) session.getAttribute("messageContent");
		}
		
		String messageType = null;
		if(session.getAttribute("messageType") != null){
			messageType = (String) session.getAttribute("messageType");
		}
		
		if(messageContent != null){
	%>
	<div class="modal fade" id="messageModal" tabindex="-1" role="dialog" aria-hidden="true">
		<div class="vertical-alignment-helper">
			<div class="modal-dialog vertical-align-center">
				<div class="modal-content <% if(messageType.equals("오류메세지")) out.println("panel-warning"); else out.println("panel-success"); %>">
					<div class="modal-header panel-heading">
						<button type="button" class="close" data-dismiss="modal">
							<span aria-hidden="true">&times</span>
							<span class="sr-only">Close</span>
						</button>
						<h4 class="modal-title">
							<%= messageType %>
						</h4>
					</div>
					<div class="modal-body">
						<%=messageContent  %>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-primary" data-dismiss="modal">확인</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script>
		$('#messageModal').modal("show");
	</script>
	<%			
		session.removeAttribute("messageContent");
		session.removeAttribute("messageType");
		}
	%>
	
	<!-- 중복아이디 체크 팝업창 -->
	<div class="modal fade" id="checkModal" tabindex="-1" role="dialog" aria-hidden="true">
		<div class="vertical-alignment-helper">
			<div class="modal-dialog vertical-align-center">
				<div id="checkType" class="modal-content panel-info">
					<div class="modal-header panel-heading">
						<button type="button" class="close" data-dismiss="modal">
							<span aria-hidden="true">&apm;times</span>
							<span class="sr-only">Close</span>
						</button>
						<h4 class="modal-title">
							확인 메시지
						</h4>
					</div>
					<div id="checkMessage" class="modal-body">
						
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-primary" data-dismiss="modal">확인</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>









