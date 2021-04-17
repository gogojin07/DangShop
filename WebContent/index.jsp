<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>댕샵에 오신걸 환영합니다!!! :)</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
</head>
<body>
<% session.setAttribute("subTitle", " "); %>
<center>
<div id="header">
	<jsp:include page="header.jsp" />
</div>

<div id="middle">
	<div id="menu"></div>
	<div id="main"></div>
</div>

<div id="footer"></div>
</center>

<script>
$(function() {
	//비동기 통신 메소드
	$('#footer').load('footer.jsp');	//서버에 request 새로 요청.
	
	let url;
	if(${page==null}) url = "bestItem";
	else url = "${page}";
	
	Aj("menu", "#menu");
	Aj(url, "#main");
	
	//브라우저 높이에 비례해서 menu, main 높이 설정
	let wh = $(window).height();
	$('#menu').height(wh-240);
	$('#main').height(wh-260);
});

function Aj(url, position) {
	$.ajax({
		url: url,
		type: 'get',
		dataType: 'html'
	})
	.done((data) => {console.log(data); $(position).html(data);})
	.fail((err) => console.log(err))
}
</script>
</body>
</html>