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
	<div id="main">${sList }</div>

<!-- The Modal -->
  <div class="modal fade" id="myModal">
    <div class="modal-dialog modal-dialog-centered">
      <div class="modal-content">
      
        <!-- Modal Header -->
        <div class="modal-header">
          <h4 class="modal-title">Modal Heading</h4>
          <button type="button" class="close" data-dismiss="modal">&times;</button>
        </div>
        
        <!-- Modal body -->
        <div class="modal-body">
          Modal body..
        </div>
        
        <!-- Modal footer -->
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
        </div>
        
      </div>
    </div>
  </div>
<!-- The Modal End -->

</div>

<div id="footer"></div>
</center>

<script>
$(function() {
	//비동기 통신 메소드
	$('#footer').load('../footer.jsp');	//서버에 request 새로 요청.	
	Aj("menu", "#menu");
	$('#back').click(function() {
		history.back();
	});

	//selectbox option의 text값에 따라 배경색 바꾸기 
	$('tbody>tr').each(function(i){
		if($(".s_code option:checked").eq(i).text()=="결제완료") {
			$(this).children('td').children('.s_code').css("background","gold");
		}
		if($(".s_code option:checked").eq(i).text()=="배송중") {
			$(this).children('td').children('.s_code').css("background","lightgreen");
		}
		if($(".s_code option:checked").eq(i).text()=="거래완료") {
			$(this).children('td').children('.s_code').css("background","#efefef");
		}
	});
	
	$('.s_code').on("change",function(){
		let json = JSON.stringify($(this).val());
		console.log(json);	
		$.ajax({
			url:'/chgScode',
			data:{data:json},
			dataType:"html",
			success : function(data) {
				console.log(data);  //성공  
			},
			error : function(error) {
				console.log(error);
			}
		});	//ajax End	
	});	//on("click") End
});	//ready() End

function userInfo(n) {	
	var json=JSON.stringify($('.p_id').eq(n).text()); 
	console.log("id=="+json);
	
	$.ajax({
		url:'/userInfo',
		data:{data:json},
		dataType:"json",
		success : function(data) {
			console.log(data);  //성공 
			let title = "<i class='far fa-paper-plane'></i> "+ data.name + " ("+ data.phone +")";
			$('.modal-title').html(title);
			$('.modal-body').text(data.addr);			
		},
		error : function(error) {
			console.log(error);
		}
	});	
}
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