<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>댕샵에 오신걸 환영합니다!!! :)</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</head>
<body>
<center>
<div id="header">
	<jsp:include page="/header.jsp" />
</div>
<div id="middle">
	<div id="menu"></div>
	<div id="main">${oList }</div>
</div>

<div id="footer"></div>
</center>

<script>
$(function() {
	//비동기 통신 메소드
	$('#footer').load('../footer.jsp');	//서버에 request 새로 요청.	
	$('#back').click(function() {
		history.back();
	});
});
</script>
</body>
</html>