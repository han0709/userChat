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
	
		String toID = null;
		if(request.getParameter("toID") != null){
			toID = (String)request.getParameter("toID");
		}
		
		if(userID == null){
			session.setAttribute("messageType", "오류메세지");
			session.setAttribute("messageContent", "현재 로그인이 되어있지 않습니다.");
			response.sendRedirect("join.jsp");
			return;
		}
		
		if(toID == null){
			session.setAttribute("messageType", "오류메세지");
			session.setAttribute("messageContent", "대화상대가 지정되지 않았습니다.");
			response.sendRedirect("join.jsp");
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
	getUnreadMessages(userID)
	
		function autoClosingAlert(selector, delay){
			var alert = $(selector).alert();//$(selector)의 자녀클래스 alert을 반영함
			alert.show();
			
			//delay 시간 후 function실행
			window.setTimeout(function(){ alert.hide() }, delay);
		}	
		
		function submitFunction(){
			var fromID = '<%= userID %>';
			var toID = '<%= toID %>';

			var chatContent = $("#chatContent").val();

			$.ajax({
				type: "POST",
				url: "./chatSubmitServlet",
				data:{
					fromID: encodeURIComponent(fromID),
					toID: encodeURIComponent(toID),
					chatContent: encodeURIComponent(chatContent)
				},
				success: function(result){
					if(result == 1){
						autoClosingAlert('#successMessage',2000);
						
					}else if(result == 0){
						autoClosingAlert('#dangerMessage',2000);
					}else{
						autoClosingAlert('#warningMessage',2000);
					}
				}		
			})
			$('#chatContent').val('');
		}
		
		var lastID = 0;
		function chatListFunction(type){
			//alert(type);
			//fromID === session에서 받아온 값이다.
			//toID === 친구찾기에서 클릭했을때 넘어오는 값이다.
			var fromID = '<%= userID %>'; 
			var toID = '<%= toID %>'; 
			$.ajax({
				type: "POST",
				url: "./chatListServlet",
				data:{
					fromID: encodeURIComponent(fromID),
					toID: encodeURIComponent(toID),
					listType: type
				},
				success : function(data){
					console.log("data == "+data);
					if(data == "") return;
					var parsed = JSON.parse(data);
					var result = parsed.result;
					
					for(var i=0; i< result.length; i++){
						
						
						addProfile(result[i][0].value, i);
 						if(result[i][0].value == fromID){
							result[i][0].value = '나';
							
						}  
 						addChat(result[i][0].value, result[i][2].value, result[i][3].value, i)
					}
					
					lastID = Number(parsed.last);//가장마지막에 글을 남긴 채팅 아이디(primary 번호)가 남게 된다.
					console.log("lastID == "+lastID);
				}		
			});
		}
		
		function addProfile(userID, i){
			//alert(userID)
			
			$.ajax({
				type: "POST",
				url: "./GetTableById",
				data:{
					userID: userID,
				},
				success : function(data){

					$(".proImg"+i+"").attr("src","images/"+data+"");
				}		
			});
		}
		
		function addChat(chatName, chatContent, chatTime, i){
			$('#chatList').append('<div class="row">'+
					'<div class="col-lg-12">'+
					'<div class="media">'+
					'<a class="pull-left" href="#">'+
					'<img class="media-object img-circle proImg'+i+'" style="width:30px;height:30px;" src="images/icon.png" alt=""/>'+
					'</a>'+
					'<div class="media-body">'+
					'<h4 class="media-heading">'+
					chatName+
					'<span class="small pull-right">'+
					chatTime+
					'</span>'+
					'</h4>'+
					'<p>'+
					chatContent+
					'</p>'+
					'</div>'+
					'</div>'+
					'</div>'+
					'</div>'+
					'<hr />');
			$('#chatList').scrollTop($('#chatList')[0].scrollHeight);
		}
			
		function getInfiniteChat(){
			setInterval(function(){
				
				//lastID는 처음에는 ten으로 지정되어 들어갔다가 후에 chatListFunction의 lastID = Number(parsed.last);에의해 재정의 된다.
				chatListFunction(lastID);
			},2000);
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
				if(userID != null){
			%>
			<ul class="nav navbar-nav navbar-right">
				<li class="dropdown">
					<a href="#none" class="dropdown-toggle"
						data-toggle="dropdown" role="button" aria-haspopup="true"
						aria-expanded="false">회원관리<span class="caret"></span>
					</a>
					<ul class="dropdown-menu">
						<li><a href="logoutAction.jsp">로그아웃</a></li>
					</ul>
				</li>
			</ul>
			<%		
				}
			%>		
			
		</div>
	</nav>
	
	<!-- 실시간 채팅창 -->
	<div class="container bootstrap snippet">
		<div class="row">
			<div class="portlet portlet-default">
			 	<div class="portlet-heading">
			 		<div class="portlet-title">
			 			<h4><i class="fa fa-circle text-green"></i>실시간 채팅창</h4>
			 		</div>
			 		<div class="clearfix"></div>
			 	</div>
		 		<div id="chat" class="panel-collapse collapse in">
		 			<div id="chatList" class="portlet-body chat-widget" style="overflow-y:auto;width:auto;height:600px; ">
		 				
		 			</div>
		 			<div class="portlet-footer">

	 					<div class="row" style="height:90px">
	 						<div class="form-group col-xs-10">
	 							<textarea style="height:80px" id="chatContent" class="form-control" placeholder="메시지를 입력하세요" maxlength="100"></textarea>
	 						</div>
	 						<div class="form-group col-xs-2">
	 							<button type="button" class="btn btn-default pull-right" onclick="submitFunction();">전송</button>
	 							<div class="clearfix"></div>
	 						</div>
	 					</div>
		 			</div>
		 		</div>
			</div>
		</div>
	</div>
	
	<!-- alert 모달팝업 -->
	<div class="alert alert-success" id="successMessage" style="display:none;">
		<strong>메시지 전송에 성공 했습니다</strong>
	</div>
	<div class="alert alert-danger" id="dangerMessage" style="display:none;">
		<strong>이름과 내용을 모두 입력해 주세요</strong>
	</div>
	<div class="alert alert-warning" id="warningMessage" style="display:none;">
		<strong>데이터베이스 오류가 발생 하였습니다.</strong>
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
	<script>
		$(document).ready(function(){
			chatListFunction('ten');
			getInfiniteChat();
		})
	</script>
</body>
</html>