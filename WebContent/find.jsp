<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<%
		String userID = null;
		//사용자 접속유무 확인
		if(session.getAttribute("userID") != null){
			userID = (String)session.getAttribute("userID");
		}
		
/* 		String toID = null;
		if(request.getParameter("toID") != null){
			toID = (String)request.getParameter("toID");
			//out.println("toID === - -"+toID);
		} */
		
		if(userID == null){
			session.setAttribute("messageType", "오류메세지");
			session.setAttribute("messageContent", "현재 로그인이 되어있지 않습니다.");
			response.sendRedirect("index.jsp");
			return;
		}
		
	%>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" href="css/bootstrap.css">
	<link rel="stylesheet" href="css/custom.css">
	<title>실시간 회원제 채팅 서비스</title>
	<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
	<script src="js/bootstrap.js"></script>
	<script>
	var userID = "<%= userID %>";
	
	//친구추가fgq
<%-- 	var toID = '<%= toID %>';

	if(toID == null || toID == "null"){
	}else{
		addFriends(toID);
	} --%>
	
	
	
	//총 안읽은 메세지 숫자
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
	getUnreadMessages(userID)
		function findFunction(){
			var userID = $('#findID').val();
			$.ajax({
				type: "POST",
				url: './UserRegisterCheckServlet',
				data: {userID:userID},
				success: function(result){
					if(result == 0){
						$('#checkMessage').html('친구찾기에 성공 했습니다.');
						$('#checkType').attr('class','modal-content panel-success');
						getFriend(userID);
					}else{
						$('#checkMessage').html('친구찾기에 실패 했습니다.');
						$('#checkType').attr('class','modal-content panel-warning');
						failFriend();
					}
					$('#checkModal').modal("show");
				}
				
			})
		}
		
		function getFriend(findID){
			$('#friendResult').html(
				'<thead>'+
				'<tr>'+
				'<th><h4>검색 결과</h4></th>'+
				'</tr>'+
				'</thead>'+
				'<tbody>'+
				'<tr>'+
				'<td style="text-align:right;position:relative;"><h3 style="position:absolute;left:50%;top:0;transform:translate(-50%,-50%);font-size:18px;"><a href="modifyAction?userIDforMod='+findID+'" >'+findID+'</a></h3>'+
					'<a href="#none" data-info="'+findID+'" class="btn btn-primary addF">'+'친구추가</a>&nbsp&nbsp'+
				     '<a href="chat.jsp?toID='+encodeURIComponent(findID)+'" class="btn btn-primary">'+'메세지 보내기</a></td>'+
				'</tr>'+
				'</tbody>'
			);
		}
		
		function failFriend(){
			$('#friendResult').html("");
		}
		
		function addFriends(toID){
			$.ajax({
				type: "POST",
				url: './AddFriends',
				data: {toID: toID, userID: userID},
				success: function(result){
					if(result == 1){
						$('#checkMessage').html('친구추가에 성공 했습니다.');
						$('#checkType').attr('class','modal-content panel-success');
					}else if(result == 0){
						$('#checkMessage').html('이미 등록된 친구 입니다.');
						$('#checkType').attr('class','modal-content panel-warning');
					}else{
						$('#checkMessage').html('친구추가에 실패 했습니다.');
						$('#checkType').attr('class','modal-content panel-warning');
					}
					$('#checkModal').modal("show");
				}
				
			});
		}
		
		$(function(){
			$("#friendResult").on("click", ".addF" , function(){
				var toID_in = $(this).attr("data-info");
				addFriends(toID_in);
				
			})
			
		})
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
				<li><a href="index.jsp">메인</a></li>
				<li class="active"><a href="find.jsp">친구찾기</a></li>
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
	<!-- 친구찾기 폼 -->
	<div class="container">
		<table class="table table-bordered table-hover" style="text-align:center;border:1px solid #ddd">
			<thead>
				<tr>
					<th colspan="2"><h4>검색으로 친구찾기</h4></th>
				</tr>
			</thead>
			<tbody>
				<tr class="active">
					<td style="width:110px;"><h5>친구 아이디</h5></td>
					<td><input class="form-control" type="text" id="findID" maxlength="20" placeholder="찾으실 아이디를 입력 해 주세요"/></td>
				</tr>
				<tr>
					<td colspan="2"><button class="btn btn-primary pull-right" onclick="findFunction();">검색</button></td>
				</tr>
			</tbody>
		</div>
	</div>
	<div class="container">
		<table id="friendResult" class="table table-bordered table-hover" style="text-align:center;border:1px solid #ddd">
		 
		</table>
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
							<span aria-hidden="true">&apm;times</span>
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
	<!-- 모달팝업 -->
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