<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
</head>
<body>
	<%
		String userID = null;
	//사용자 접속유무 확인
		if(session.getAttribute("userID") != null){
			userID = (String)session.getAttribute("userID");
		}
	%>
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
	
	function listFriends(userID){
		$.ajax({
			type:"POST",
			url:"./ListFriends",
			data:{userID : userID},
			success: function(data){
				console.log("data == "+data);
				if(data == "") return;
				var parsed = JSON.parse(data);
				var result = parsed.result;
				
				for(var i=0; i< result.length; i++){
					//alert(result[i][0].value)
					showFriends(result[i][0].value, i)
					addProfile(result[i][0].value, i)
				}

			}
		})
	}
	listFriends(userID);
	
	function addProfile(userID, i){
		
		$.ajax({
			type:"POST",
			url:"./AddProText",
			data:{userID : userID},
			success: function(data){
				console.log("data == "+data);
				if(data == "") return;
				var parsed = JSON.parse(data);
				var result = parsed.result;
				//alert("result.length=="+result.length)
				
				for(var z=0; z< result.length; z++){
					//alert(result[i][0].value+"//"+result[i][1].value)
					
					if(result[z][0].value == "null" || result[z][0].value ==""){
						result[z][0].value = "icon.png";
					}
					
					if(result[z][1].value == "null" || result[z][1].value ==""){
						result[z][1].value = "";
					}
					
					$(".profileImg"+i+"").attr("src","images/"+result[z][0].value+"");
					$(".userText"+i+"").text(result[z][1].value);
				}
			}
		})
	}
	
	function showFriends(friends, i){
		
		$(".get_friends").append(
			'<tr class="chatTime_wrap">'+
				'<td style="width:110px;">'+
					'<h5>'+
					'<a href="modifyAction?userIDforMod='+friends+'" style="color:#333;font-weight:700">'+friends+'</a>'+
					'</h5></td>'+
				'<td style="width:50px;"><img class="profileImg'+i+'" style="width:50px;height:50px" /></td>'+
				'<td><a href="chat.jsp?toID='+friends+'" class="userText'+i+'"></a></td>'+		
			'</tr>'
		);
	}
	
</script>
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
	
	<!-- 친구 목록 폼 -->
	<div class="container">
		<table class="table table-bordered table-hover" style="text-align:center;border:1px solid #ddd">
			<thead>
				<tr>
					<th colspan="3"><h4>친구 목록</h4></th>
				</tr>
			</thead>
	<% if(userID != null){ %>
			<tbody  class="get_friends">
			</tbody>
		<% }else{ %>
			<tbody>
				<tr><td>로그인 해 주세요</td></tr>
			</tbody>

		<% } %>		
		</div>
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
</body>
</html>