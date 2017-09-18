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
		
		var userID = "${userID }";
		
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
		
		getUnreadMessages(userID);
		
		var arrayFriends = [];
		//각 아이디마다 안읽은 숫자
		function getUnreadById(userID){
			
			$.ajax({
				type: "POST",
				url: './GetUnreadById',
				data: {userID:userID},
				success: function(data){
					console.log("data == "+data);
					if(data == "") return;
					var parsed = JSON.parse(data);
					var result = parsed.result;
					
					for(var i=0; i< result.length; i++){
						
						//받는 사람이 로그인한자와 같다면 from아이디를 toID에 넣어줌
						if(result[i][0].value == userID){
							result[i][0].value = result[i][1].value
						}
	
						console.log(result[i][0].value);
						if(arrayFriends.indexOf(result[i][0].value) > -1){
						
						}else{
							
							//         toID                chatContent	   		chatTime
							addChat2(result[i][0].value, result[i][2].value, result[i][3].value, i)
						}
						arrayFriends.push(result[i][0].value);
						console.log(arrayFriends);
					
					}
				}
				
			});
			
		}
		getUnreadById(userID);
		
		function addChat2(toID, chatContent, chatTime ,i){
			
			$(".get_Messages").append(
				'<tr class="chatTime_wrap">'+
					'<td style="width:110px;"><h5>'+toID+'</h5></td>'+
					'<td><a href="chat.jsp?toID='+toID+'">'+chatContent+'<span class="chatTime">'+chatTime+'</span><strong class="message_num'+i+'" style="color:red"></strong></a></td>'+		
				'</tr>'
			);
			
			$.ajax({
				type: "POST",
				url: './GetUnreadByIdEach',
				data: {toID: toID,userID: userID},
				success: function(data){
					$('.message_num'+i+'').html(data);
				}
				
			});
			
		}

		
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
				<li><a href="find.jsp">친구찾기</a></li>
				<li class="active"><a href="messageBox.jsp" class="count_unread">메시지함 <strong style="color:red;"></strong></a></li>
				<c:if test="${not empty userID}">
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
					<th colspan="2"><h4>주고받은 메세지 목록</h4></th>
				</tr>
			</thead>
			<tbody  class="get_Messages">
			</tbody>
		</div>
	</div>
</body>
</html>