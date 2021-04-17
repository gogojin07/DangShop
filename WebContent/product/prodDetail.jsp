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
<%String pCode = request.getParameter("pCode");%>
<!-- img upload 폴더 => D:\JAVA_WORK\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\DangShop\upload -->
<center>
<div id="header">
	<jsp:include page="/header.jsp" />
</div>
<div id="middle">
	<div id="menu"></div>
	<div id="main">${pDetail }</div>
</div>

<div id="footer"></div>
</center>

<script>
$(function() {
	//비동기 통신 메소드
	$('#footer').load('footer.jsp');	//서버에 request 새로 요청.		
	Aj("menu", "#menu");
	
	//장바구니 담기
	$('#addCart').on("click", function() {
		$.ajax({
			url:'addCart?pCode=<%=pCode%>',
			type:'get',
			dataType:'html',
			success: function(data) {
				console.log(data);
			},
			error : function(error) {
				console.log(error);
			}
		}).done((data) => {
			if(confirm('장바구니에 담았습니다.\n장바구니 페이지로 이동할까요?')) {
				location.replace('cart');
			}			
		});
	});
	$('#back').click(function() {
		history.back();
	});
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