<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<style>
/* #detail {display:none; width:30%; border:solid red 1px;}
#detail.open {display:block; color:blue; text-align:left;} */
img {cursor: pointer;}
</style>
<body>
<!-- <h1>main.jsp</h1> -->
<div id="detail">
<!-- 상품 상세정보 -->
</div>
${pList}

<script>
/* $('.productImg').click(function() {
	//data함수 : set => $(this).attr("data-code","n0003");  $(this).data('code','n0003');
	//		   get => $(this).data('code');
	$('#detail').toggleClass('open').empty();	//append 사용했을때 쌓인 데이터 비우기

	if(!$('#detail').hasClass('open')){
		return false;
	}
	$.ajax({
		url:'ajaxDetail',
		type:'get',
		data:{pCode:$(this).data('code')},
		dataType:'json'
	}).done((data) => {
		let str = ""; */
/* 		for(let key in data) {
			str += "<li>"+ key+" : "+ data[key] + "<br>";
		} */
/* 		str += "<li>상품명: "+ data.name;
		str += "<li>가격: "+ data.price +" 원";
		str += "<li>재고: "+ data.qty +" 개";
		str += "<li>등록일: "+ data.date;
		str += "<li>설명: "+ data.contents;
		//$('#detail').addClass('open');
		$('#detail').html(str);
	}).fail((err) => console.log(err));
	
}); */

/*
function detail(pCode) {
	console.log(pCode);
}
$('div.product').click(function() {
	console.log($(this).attr('id'));
});
$('div.productImg').click(function() {
	console.log($(this).data('code'));	//저장: data-xxx=값, 읽기: $.data(xxx)
});
*/
</script>
</body>
</html>