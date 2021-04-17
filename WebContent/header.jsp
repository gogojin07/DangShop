<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
  <link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.2/css/all.min.css">  
<link rel="stylesheet" type="text/css" href="/css/main.css">
<title>Insert title here</title>
</head>
<body>
	<div id='logo'><a href="/"><img src="/img/logo_dangshop.jpg" alt="댕샵-logo"></a></div>
	<div id='login'>
<!-- 로그인 안된 경우 -->
	<c:if test="${empty sessionScope.id}">
		<form id="loginFrm" method="post">
		<table id='tbl_login'>
		<tr>
			<td>ID </td>
			<td>&nbsp;<input type="text" name="id"></td>
		</tr>
		<tr>
			<td>PW </td>
			<td>&nbsp;<input type="password" name="pw"></td>
		</tr>
		<tr>
			<td></td>
			<td><span style="color:red">${msgAccess }</span><br />
			<button id="joinBtn" type="button"><i class="fas fa-bone"></i> Join</button>
			<button id="loginBtn" type="button"><i class="fas fa-bone"></i> LogIn</button></td>
		</tr>
		</table>
		</form>
	</c:if>
<!-- 로그인 된 경우 -->
	<c:if test="${!empty id}">
		<span id="str_welcome"><i class="far fa-laugh-beam"></i> ${sessionScope.id }님 반가워요 :)</span><br>
		<nav class="navbar navbar-expand-sm bg-light navbar-light">
		  <ul class="navbar-nav">
		    <li class="nav-item active">
		      <a class="nav-link" href="/"><i class="fas fa-dog"></i> Home</a>
		    </li>
		    <li class="nav-item">
		      <a class="nav-link" href="/logout"><!-- <i class="fas fa-sign-out-alt"></i> -->LogOut</a>
		    </li>
		    <li class="nav-item">
		      <a class="nav-link" href="/produpFrm"><c:if test="${subTitle eq '상품등록'}"><span class='str_strong'><i class="fas fa-cloud-upload-alt"></i></c:if> 상품등록</a>
		    </li>
		     <li class="nav-item">
		      <a class="nav-link" href="/cart"><c:if test="${subTitle eq '장바구니'}"><span class='str_strong'><i class="fas fa-shopping-cart"></i></c:if> 장바구니</a>
		    </li>
		    <li class="nav-item">
		      <a class="nav-link" href="/order"><c:if test="${subTitle eq '주문내역'}"><span class='str_strong'><i class="fas fa-clipboard-list"></i></c:if> 주문내역</a>
		    </li>
		    <li class="nav-item">
		      <a class="nav-link" href="/sales"><c:if test="${subTitle eq '판매관리'}"><span class='str_strong'><i class="fas fa-box-open"></i></c:if> 판매관리</a>
		    </li>
		  </ul>
		</nav>
	</c:if>	
	</div>
	
	<script>
	$(function() {
		//비동기 통신 메소드
		$('#footer').load('footer.jsp');	//서버에 request 새로 요청.		
		Aj("menu", "#menu");

		//main의 높이만큼 menu 높이 설정
		let h = $('#main').height();
		$('#menu').height(h+20);
		
		$('#loginBtn').click(function() {
			$('#loginFrm').attr('action', 'access');
			$('#loginFrm').submit();
		});
		$('#joinBtn').click(function() {
			$('#loginFrm').attr('action', 'joinFrm');
			$('#loginFrm').submit();
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