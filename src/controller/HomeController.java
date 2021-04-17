package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Forward;
import service.MemberMM;
import service.ProductMM;

@WebServlet({"/access","/joinFrm","/produpFrm","/insertProduct","/logout",
			"/menu","/newItem","/bestItem","/prodDetail","/addCart","/cart",
			"/order","/sales"})
public class HomeController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String cmd = request.getServletPath();
		System.out.println("cmd = "+cmd);
		
		Forward fw = null;
		MemberMM mm = new MemberMM(request, response);
		ProductMM pm = new ProductMM(request, response);

		switch(cmd) {
		case "/prodDetail":
			request.getSession().setAttribute("subTitle", "상품 상세정보");
			fw = pm.prodDetail();
			break;
		case "/access":
			request.getSession().setAttribute("subTitle", " ");
			fw = mm.access();
			System.out.println("login id => "+request.getSession().getAttribute("id"));
			break;
		case "/joinFrm":
			request.getSession().setAttribute("subTitle", " ");
			break;
		case "/produpFrm":
			request.getSession().setAttribute("subTitle", "상품등록");
			fw = new Forward();
			fw.setPath("product/produpFrm.jsp");
			fw.setRedirect(false);
			break;
		case "/insertProduct":
			request.getSession().setAttribute("subTitle", " ");
			fw = pm.insertProduct();			
			break;
		case "/logout":
			request.getSession().setAttribute("subTitle", " ");
			fw = mm.logout();			
			break;
		case "/menu":
			request.getSession().setAttribute("subTitle", " ");
			fw = new Forward();
			fw.setPath("menu.jsp");
			fw.setRedirect(false);
			break;
		case "/newItem":
			request.getSession().setAttribute("subTitle", " ");
			fw = pm.getItemList("n");
			break;
		case "/bestItem":
			request.getSession().setAttribute("subTitle", " ");
			fw = pm.getItemList("b");
			break;
		case "/addCart":
			request.getSession().setAttribute("subTitle", " ");
			pm.addCart();
			break;
		case "/cart":
			request.getSession().setAttribute("subTitle", "장바구니");
			fw = pm.cart();
			break;
		case "/order":
			request.getSession().setAttribute("subTitle", "주문내역");
			fw = pm.order();
			break;
		case "/sales":
			request.getSession().setAttribute("subTitle", "판매관리");
			fw = pm.sales();
			break;
		}
		if(fw != null) {
			if(fw.isRedirect()) {
				response.sendRedirect(fw.getPath());
			} else {
				request.getRequestDispatcher(fw.getPath()).forward(request, response);
			}
		}
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}

}
