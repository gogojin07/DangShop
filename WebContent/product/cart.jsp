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
	<div id="main">${cList }</div>
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
	//전체선택
	$('#chk_all').on("click", function() {
		if($(this).is(":checked")) {
			$('tbody>tr').each(function() {
				$(this).children('td').children('.check').prop("checked", true);
			});
		} else {
			$('tbody>tr').each(function() {
				$(this).children('td').children('.check').prop("checked", false);
			});			
		}
	});
	//선택상품 삭제하기
	$('#btn_del').on("click", function() {
		// 체크여부 확인
		if($("input:checkbox[name=user_chk]").is(":checked") == true) {
			if(confirm("선택하신 상품을 장바구니에서 삭제하시겠습니까?")) {
				
				var arr=[];
				$('tbody>tr').each(function(i){
					if($(this).children('td').children('.check').is(":checked")) {
						arr.push($(this).children('td').children(".pcode").val());
					}
				});
				var json=JSON.stringify(arr); 
				console.log("del=="+json);
				
				$.ajax({
					url:'/delCart',
					data:{data:json},
					dataType:"html",
					success : function(data) {
						location.href='/cart';
						console.log(data);  //성공  
					},
					error : function(error) {
						console.log(error);
					}
				});	
			} //if(confirm) End
		} else alert("삭제하실 상품을 선택해주세요!")
	}); // on("click") End
	
	//선택상품 주문하기
	$("#btn_order").on("click",	function() {
		// 체크여부 확인
		if($("input:checkbox[name=user_chk]").is(":checked") == true) {
			if(confirm("선택하신 상품을 주문하시겠습니까?")) {
				
				var arr=[];
				$('tbody>tr').each(function(i){
					if($(this).children('td').children('.check').is(":checked")) {
						let obj={};
						obj.p_qty = $(this).children('td').children(".qty").val();
						obj.p_code = $(this).children('td').children(".pcode").val();
						obj.p_price = $(this).children('td').children(".price").val();
						arr.push(obj);
					}
				});
				var json=JSON.stringify(arr); 
				console.log(json);	
				
				$.ajax({
					url:'/addOrder',
					data:{data:json},
					dataType:"html",
					success : function(data) {
						location.href='/order';
						console.log(data);  //성공  
					},
					error : function(error) {
						console.log(error);
					}
				});	
			} //if(confirm) End
		} else alert("주문하실 상품을 선택해주세요!")
	}); // on("click") End
});

</script>
</body>
</html>