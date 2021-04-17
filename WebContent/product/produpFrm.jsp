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
	<div id="main">

	<form action="insertProduct" method="post" enctype="multipart/form-data">
	<table id="tbl_upFrm">
	<tr>
		<td><i class="fas fa-angle-right"></i> 카테고리</td>
		<td><input type="radio" name="p_kind" value="n" checked> 신상품 &nbsp;&nbsp;
			<input type="radio" name="p_kind" value="b"> 인기상품</td>
	</tr>
	<tr>
		<td><i class="fas fa-angle-right"></i> 상품명</td>
		<td><input type="text" name="p_name" id="p_name" size="30" maxlength="30"></td>
	</tr>
	<tr>
		<td><i class="fas fa-angle-right"></i> 가격</td>
		<td><input type="text" name="p_price"></td>
	</tr>
	<tr>
		<td><i class="fas fa-angle-right"></i> 재고량</td>
		<td><input type="text" name="p_qty"></td>
	</tr>
	<tr>
		<td><i class="fas fa-angle-right"></i> 상품설명</td>
		<td><textarea name="p_contents" cols="60" rows="7"></textarea></td>
	</tr>
	<tr>
		<td><i class="fas fa-angle-right"></i> 이미지</td>
		<td><input type="file" name="p_file"></td>
	</tr>
	<tr>
		<td></td>
		<td><br>
			<button><i class="fas fa-cloud-upload-alt"></i> 상품 등록</button>
			&nbsp;&nbsp;<button type="reset"><i class="fas fa-undo-alt"></i> 취소</button></td>
	</tr>
	</table>
	</form>
	
	</div>
</div>


<div id="footer"></div>
</center>

<script>
$(function() {
	//비동기 통신 메소드
	$('#p_name').focus();
	$('#footer').load('/footer.jsp');	//서버에 request 새로 요청.	
	$('#back').click(function() {
		history.back();
	});
});
</script>
</body>
</html>