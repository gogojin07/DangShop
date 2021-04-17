package service;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import bean.Cart;
import bean.Forward;
import bean.Order;
import bean.Product;
import dao.ProductDao;

public class ProductMM {
	HttpServletRequest request;
	HttpServletResponse response;

	public ProductMM(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}

	public Forward insertProduct() {
		Forward fw = new Forward();
		if(request.getSession().getAttribute("id") == null) {
			fw.setPath("./");
			fw.setRedirect(true);
			return fw;
		}
		String uploadPath = request.getSession().getServletContext().getRealPath("upload");
		System.out.println("path = "+ uploadPath);
		File dir = new File(uploadPath);
		if(!dir.exists()) {	//폴더나 파일이 존재하지 않으면.(tomcat clean시 upload폴더가 삭제됨)
			dir.mkdir();	//폴더 생성
		}
		int size = 10*1024*1024;	//최대 10MB
		
		try {
			MultipartRequest multi = new MultipartRequest(request, uploadPath, size,
					"UTF-8", new DefaultFileRenamePolicy());
			String kind = multi.getParameter("p_kind");
			String name = multi.getParameter("p_name");
			int price = Integer.parseInt(multi.getParameter("p_price"));
			int qty = Integer.parseInt(multi.getParameter("p_qty"));
			String contents = multi.getParameter("p_contents");
			String orgFileName = multi.getOriginalFileName("p_file");
			String sysFileName = multi.getFilesystemName("p_file");
			
			HttpSession session = request.getSession();	//반드시 매번
			Product product = new Product();
			product.setP_id(session.getAttribute("id").toString());	//로그인한 아이디
			product.setP_kind(kind);
			product.setP_name(name);
			product.setP_price(price);
			product.setP_qty(qty);
			product.setP_contents(contents);
			product.setP_orgFileName(orgFileName);
			product.setP_sysFileName(sysFileName);
			
			ProductDao pDao = new ProductDao();
			if(pDao.insertProduct(product)) {
				System.out.println("상품등록 성공");
			} else System.out.println("상품등록 실패");
			pDao.close();
			
			//신상품 등록후 newItem.jsp로, 인기상품 등록후 bestItem.jsp로 이동
			if(kind.equals("n")) {
				session.setAttribute("page", "newItem");	//newItem.jsp
			} else {
				session.setAttribute("page", "bestItem");	//bestItem.jap
			}
			fw.setPath("./");
			fw.setRedirect(true);
			
		} catch (IOException e) {
			System.out.println("상품등록 예외 발생");
			e.printStackTrace();
		}
		return fw;
	}

	public Forward getItemList(String kind) {
		Forward fw = new Forward();
		ProductDao pDao = new ProductDao();
		List<Product> pList = null;
		pList = pDao.getItemList(kind);
		pDao.close();
		
		if(pList != null) {
			request.setAttribute("pList", makeHtml_pList(pList));
//			request.setAttribute("pList", pList); 	//jstl
//			request.setAttribute("pList", new Gson().toJson(pList));	//js
		}
		fw.setPath("main.jsp");
		fw.setRedirect(false);
		return fw;
	}

	private String makeHtml_pList(List<Product> pList) {
		StringBuilder sb = new StringBuilder();
		DecimalFormat df = new DecimalFormat("###,###,###");
		
		for(Product p : pList) {
//			sb.append("<div onclick=\"detail('"+ p.getP_code() +"')\">");
//			sb.append("<div class='product' id='"+ p.getP_code() +"'>");
			sb.append("<div class='productImg' data-code='"+ p.getP_code() +"'>");
			sb.append("<a href='prodDetail?pCode="+ p.getP_code() +"'><img src=\"upload/"+ p.getP_sysFileName() +"\" alt=\""+p.getP_name()+"\" class='img_main'></a>");
			sb.append(p.getP_name() +"<br><nobr>"+ df.format(p.getP_price()) +"원</nobr>");
			sb.append("<br><button type='button' onclick=\"location.href='prodDetail?pCode="+ p.getP_code() +"'\" class='btn_more'><i class=\"far fa-hand-point-right\"></i> 상세정보</button>");
			sb.append("</div>");
		}
		return sb.toString();
	}

	public HashMap<String, String> productDetail(String pCode) {

		ProductDao pDao = new ProductDao();
		HashMap<String,String> hMap = null;
		hMap = pDao.getItemDetail(pCode);
		pDao.close();
		
		return hMap;
	}

	public Forward prodDetail() {
		String pCode = request.getParameter("pCode");
		Forward fw = new Forward();
		HashMap<String, String> hMap = null;
		hMap = productDetail(pCode);
		
		if(hMap != null) {
			request.setAttribute("pDetail", makeHtml_pDetail(hMap));
		}
		fw.setPath("product/prodDetail.jsp");
		fw.setRedirect(false);
		return fw;
	}

	private String makeHtml_pDetail(HashMap<String, String> hMap) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("<table id='tbl_detail'>");
		sb.append("<tr><td rowspan='6' align='center'><div id='prodImg'><img src='upload/"+ hMap.get("sysFileName")+"'width=300></div></td>");
		sb.append("<td>&nbsp;<i class=\"fas fa-angle-right\"></i> 상품명 : "+hMap.get("name")+"</td></tr>");
		sb.append("<tr><td>&nbsp;<i class=\"fas fa-angle-right\"></i> 판매가 : "+hMap.get("price")+"원</td></tr>");
		sb.append("<tr><td>&nbsp;<i class=\"fas fa-angle-right\"></i> 재고 : "+hMap.get("qty")+"</td></tr>");
		sb.append("<tr><td>&nbsp;<i class=\"fas fa-angle-right\"></i> 설명 : <pre>"+hMap.get("contents")+"</pre></td></tr>");
		sb.append("<tr><td>&nbsp;<i class=\"fas fa-angle-right\"></i> 판매자 : "+hMap.get("id")+"</td></tr>");
		sb.append("<tr><td>&nbsp;<i class=\"fas fa-angle-right\"></i> 등록일 : "+hMap.get("date")+"</td></tr></td></table>");
		//테이블안에 사진이랑 설명 나올 수있도록 변경함		
		
		HttpSession session = request.getSession();	//반드시 매번
		if(session.getAttribute("id") != null) {			
			sb.append("<div style='float: right;'>");
			sb.append("<br/><button id='addCart' type='button'><i class=\"fas fa-cart-arrow-down\"></i> 장바구니 담기</button>");
			sb.append("&nbsp;&nbsp; <button id='back' type='button'>◀이전으로</button><br/><br/>");
			sb.append("</div>");			
		}
		return sb.toString();
	}

	public void addCart() {
		String pCode = request.getParameter("pCode");
		HttpSession session = request.getSession();	//반드시 매번
		String c_id = session.getAttribute("id").toString();
		
		if(c_id != null) {
			ProductDao pDao=new ProductDao();
			Cart c = new Cart();
				
			c=pDao.checkQty(pCode,c_id);
			if(c!=null) {//1. 장바구니에 물건이 이미 있어서 갯수만 더할 경우
				pDao.addCartPlus(pCode, c_id);
			}else {//2. 장바구니에 물건이 없어서 새로 추가할경우
				pDao.addCart(pCode, c_id);
			}
			pDao.close();
		}
	}
	
	public void addCart_gym() {
		String pCode = request.getParameter("pCode");
		HttpSession session = request.getSession();	//반드시 매번
		String c_id = session.getAttribute("id").toString();
		
		if(c_id != null) {
			ProductDao pDao = new ProductDao();
	
			if(pDao.addCart(pCode, c_id)) {
				System.out.println("장바구니 등록 성공");
			} else System.out.println("장바구니 등록 실패");
			pDao.close();
		}
	}

	public Forward cart() {
		Forward fw = new Forward();
		HttpSession session = request.getSession();	//반드시 매번
		
		if(session.getAttribute("id")==null) {
			fw.setPath("./");
			fw.setRedirect(true);
			return fw;
		}

		ProductDao pDao = new ProductDao();
		List<Product> cList = null;
		cList = pDao.getCartList(session.getAttribute("id").toString());
		pDao.close();
		
		if(cList != null) {
			session.setAttribute("cList", makeHtml_cList(cList));
		}
		fw.setPath("product/cart.jsp");
		fw.setRedirect(false);
		return fw;
	}

	private String makeHtml_cList(List<Product> cList) {
		DecimalFormat df = new DecimalFormat("###,###,###");
		StringBuilder sb = new StringBuilder();
		
		sb.append("<div class=\"container\">");
		sb.append("<table class='table table-hover' style='background-color: white;'>");
		sb.append("<thead>");
		sb.append("<tr><th>이미지</th><th>상품명</th><th>수량</th><th>가격</th>");
		sb.append("<th><input type='checkbox' id='chk_all'> 전체선택</th></tr>");
		sb.append("</thead>");
		
		if(cList.size() > 0) {
			sb.append("<tbody class='tbl'>");
			for(Product pd : cList) {
				sb.append("<tr class='prod'>");
				sb.append("<td><a href='../prodDetail?pCode="+ pd.getP_code() +"'><img src=\"../upload/"+ pd.getP_sysFileName() +"\" width=100></a></td>");
				sb.append("<td><a href='../prodDetail?pCode="+ pd.getP_code() +"'>"+ pd.getP_name() +"</a></td>");
				sb.append("<td><input name='qty' type='text' size='3' class='qty' value='"+ df.format(pd.getP_qty()) +"'></td>");
				sb.append("<td>"+ df.format(pd.getP_price()) +"원</td>");
				sb.append("<td><input name='pcode' type='hidden' class='pcode' value='"+ pd.getP_code() +"'>");
				sb.append("<input name='price' type='hidden' class='price' value='"+ pd.getP_price() +"'>");
				sb.append("<input name='user_chk' type='checkbox' class='check'></td>");
				sb.append("</tr>");
			}
			sb.append("</tbody>");
			sb.append("<thead>");
			sb.append("<tr><th colspan='2' style='text-align:left'>");		
			sb.append("<button id='btn_del' type='button'><i class=\"fas fa-trash-alt\"></i> 선택상품 삭제하기</button></th>");
			sb.append("<th colspan='3' style='text-align:right'><button id='back' type='button'>◀이전으로</button>");
			sb.append("&nbsp;&nbsp; <button id='btn_order' type='button'><i class=\"fas fa-share-square\"></i> 선택상품 주문하기</button></th></tr>");
			sb.append("</thead>");
		} else {
			sb.append("<thead>");
			sb.append("<tr><th colspan='5' style='height:250px;text-align:center'>장바구니에 등록하신 상품이 없습니다.<br/><br/><br/><br/><br/><br/></th></tr>");
			sb.append("<tr><th colspan='5' style='text-align:right'><button id='back' type='button'>◀이전으로</button></th></tr>");
			sb.append("</thead>");			
		}
		sb.append("</table>");
		sb.append("</div>");
		
		return sb.toString();
	}

	public boolean delCart(String[] pCodes) {
		HttpSession session = request.getSession();	//반드시 매번
		String c_id = session.getAttribute("id").toString();
		
		if(c_id != null) {
			ProductDao pDao = new ProductDao();
	
			if(pDao.delCart(pCodes, c_id)) {
				System.out.println("장바구니 삭제 성공");
				return true;
			} else {
				System.out.println("장바구니 삭제 실패");
			}
			pDao.close();
		} else System.out.println("세션 종료");
		return false;		
	}

	public boolean addOrder(List<Product> oList) {
		HttpSession session = request.getSession();	//반드시 매번
		String o_id = session.getAttribute("id").toString();
		
		if(o_id != null) {
			ProductDao pDao = new ProductDao();
	
			if(pDao.addOrder(oList, o_id)) {
				System.out.println("주문 성공");
				return true;
			} else {
				System.out.println("주문 실패");
			}
			pDao.close();
		} else System.out.println("세션 종료");

		return false;
	}

	public Forward order() {
		Forward fw = new Forward();
		HttpSession session = request.getSession();	//반드시 매번
		
		if(session.getAttribute("id")==null) {
			fw.setPath("./");
			fw.setRedirect(true);
			return fw;
		}

		//ORDER2 데이터 가져오기
		ProductDao pDao = new ProductDao();
		List<Order> oList = null;
		oList = pDao.getOrderList(session.getAttribute("id").toString());
		pDao.close();
		
		if(oList != null) {
			session.setAttribute("oList", makeHtml_oList(oList));
		}
		fw.setPath("product/order.jsp");
		fw.setRedirect(false);
		return fw;
	}

	private String makeHtml_oList(List<Order> oList) {
		DecimalFormat df = new DecimalFormat("###,###,###");
		StringBuilder sb = new StringBuilder();

		sb.append("<div class=\"container\">");
		sb.append("<table class='table table-hover' style='background-color: white;'>");
		
		if(oList.size() > 0) {
		
			ProductDao pDao = new ProductDao();
			List<Product> pList = null;

			sb.append("<thead>");
			sb.append("<tr><th colspan=3><i class=\"fas fa-clipboard-list\"></i> 총 주문 건수 : "+ oList.size() +" 건</th>");
			sb.append("</thead>");
			
			for(Order o : oList) {
				sb.append("<thead>");
				sb.append("<tr><th colspan=2>"+ o.getO_date() +" (주문번호 : "+ o.getO_no() +")</th>");
				sb.append("<th>결제액 : "+ df.format(o.getO_price()) +" 원</th></tr>");
				sb.append("</thead>");
				
				//ORDER_DETAIL 데이터 가져오기
				pList = pDao.getOrderDetail(o.getO_no());
				
				if(pList.size() > 0) {
					sb.append("<tbody class='tbl'>");
					for(Product p : pList) {
						sb.append("<tr>");
						sb.append("<td><a href='../prodDetail?pCode="+ p.getP_code() +"'><img src=\"../upload/"+ p.getP_sysFileName() +"\" width=100></a></td>");
						sb.append("<td>판매자 : "+ p.getP_id() +"<br><a href='../prodDetail?pCode="+ p.getP_code() +"'>"+ p.getP_name() +"</a><br>수량 : "+ p.getP_qty() +"개<br> "+ df.format(p.getP_price()) +"원</td>");
						sb.append("<td>"+ p.getS_name() +"</td>");
						sb.append("</tr>");						
					}
					sb.append("</tbody>");
				}
			}
		} else {
			sb.append("<thead>");
			sb.append("<tr><th colspan='3' style='height:250px;text-align:center'>주문 내역이 없습니다.<br/><br/><br/><br/><br/><br/></th></tr>");
			sb.append("</thead>");				
		}
		sb.append("<thead>");
		sb.append("<tr><th colspan='3' style='text-align:right'><button id='back' type='button'>◀이전으로</button></th></tr>");
		sb.append("</thead>");
		sb.append("</table>");
		sb.append("</div>");
		return sb.toString();
	}

	public Forward sales() {
		Forward fw = new Forward();
		HttpSession session = request.getSession();	//반드시 매번
		
		if(session.getAttribute("id")==null) {
			fw.setPath("./");
			fw.setRedirect(true);
			return fw;
		}

		ProductDao pDao = new ProductDao();
		List<Product> sList = null;
		sList = pDao.getSalesList(session.getAttribute("id").toString());
		pDao.close();
		
		if(sList != null) {
			session.setAttribute("sList", makeHtml_sList(sList));
		}
		fw.setPath("product/sales.jsp");
		fw.setRedirect(false);
		return fw;
	}

	private String makeHtml_sList(List<Product> sList) {
		DecimalFormat df = new DecimalFormat("###,###,###");
		StringBuilder sb = new StringBuilder();
		String select_str = "";
		String select_val = "";

		sb.append("<div class=\"container\">");
		sb.append("<table class='table table-hover' style='background-color: white;'>");
		
		if(sList.size() > 0) {

			sb.append("<thead>");
			sb.append("<tr><th colspan=3><i class=\"fas fa-box-open\"></i> 총 판매 건수 : "+ sList.size() +" 건</th>");
			sb.append("</thead>");
			sb.append("<tbody class='tbl'>");
			int n = 0;
			
			for(Product p : sList) {
				//주문상태 select box option
				select_val = p.getO_no() +","+ p.getP_code() +",";	//주문번호,상품번호,상태코드
				if(p.getS_code()==1) select_str="<option value='"+select_val+"1' selected>결제완료</option><option value='"+select_val+"2'>배송중</option><option value='"+select_val+"3'>거래완료</option>";
				else if(p.getS_code()==2) select_str="<option value='"+select_val+"1'>결제완료</option><option value='"+select_val+"2' selected>배송중</option><option value='"+select_val+"3'>거래완료</option>";
				else if(p.getS_code()==3) select_str="<option value='"+select_val+"1'>결제완료</option><option value='"+select_val+"2'>배송중</option><option value='"+select_val+"3' selected>거래완료</option>";
				
				sb.append("<tr>");
				sb.append("<td><a href='../prodDetail?pCode="+ p.getP_code() +"'><img src=\"../upload/"+ p.getP_sysFileName() +"\" width=100></a></td>");
				sb.append("<td>"+ p.getP_date() +" (주문번호 : "+ p.getO_no() +")");
				sb.append("<br>주문자 : <span class='p_id'>"+ p.getP_id() +"</span> <span class=\"badge badge-info\" data-toggle=\"modal\" data-target=\"#myModal\" style=\"cursor:pointer\" onclick='userInfo("+n+");'><i class=\"fas fa-house-user\"></i> Info</span>");
				sb.append("<br><a href='../prodDetail?pCode="+ p.getP_code() +"'>"+ p.getP_name() +"</a>");
				sb.append("<br>수량 : "+ p.getP_qty() +"개 "+ df.format(p.getP_price()) +"원</td>");
				sb.append("<td><select name='s_code' class='s_code'>"+ select_str +"</select></td>");
				sb.append("</tr>");		
				n++;
			}
			sb.append("</tbody>");
		} else {
			sb.append("<thead>");
			sb.append("<tr><th colspan='3' style='height:250px;text-align:center'>판매 내역이 없습니다.<br/><br/><br/><br/><br/><br/></th></tr>");
			sb.append("</thead>");				
		}
		sb.append("<thead>");
		sb.append("<tr><th colspan='3' style='text-align:right'><button id='back' type='button'>◀이전으로</button></th></tr>");
		sb.append("</thead>");
		sb.append("</table>");
		sb.append("</div>");
		return sb.toString();
	}

	public boolean chgScode(String scodes) {
		String[] scode_arr = scodes.split(",");
		ProductDao pDao = new ProductDao();
		if(pDao.chgScode(scode_arr))	return true;
		return false;
	}

	public HashMap<String, String> userInfo(String userId) {
		ProductDao pDao = new ProductDao();
		HashMap<String, String> hMap = null;
		hMap = pDao.userInfo(userId);
		return hMap;
	}

}
