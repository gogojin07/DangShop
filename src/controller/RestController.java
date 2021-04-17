package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import bean.Product;
import service.ProductMM;

@WebServlet({"/ajaxDetail","/delCart","/addOrder","/chgScode","/userInfo"})
public class RestController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String cmd = request.getServletPath();
		System.out.println("cmd = "+cmd);
		
		ProductMM pm = new ProductMM(request, response);
		String json = null;
		String param = "";
		boolean result;

		switch(cmd) {
		case "/ajaxDetail":
//			param = request.getParameter("pCode");
//			System.out.println("param== "+ param);
//			
//			HashMap<String,String> hMap = new HashMap<>();
//			hMap = pm.productDetail(param);
//			//toJson: java객체-->json
//			if(hMap != null) json = new Gson().toJson(hMap);
//			System.out.println("json = "+ json);			
			break;
		case "/delCart":
			param = request.getParameter("data");			
			String[] params = new Gson().fromJson(param, String[].class);
			System.out.println("data=="+params[0]);
			
			result = pm.delCart(params);
			json = new Gson().toJson(result);
			break;
			
		case "/addOrder":
			param = request.getParameter("data");	
			List<Product> oList = new Gson().fromJson(param, new TypeToken<List<Product>>(){}.getType());
			System.out.println("oList.size==>"+ oList.size());
			
			result = pm.addOrder(oList);
			json = new Gson().toJson(result);
			break;
			
		case "/chgScode":
			param = request.getParameter("data");
			String scodes = new Gson().fromJson(param, String.class);
			System.out.println("param== "+ scodes);
			
			result = pm.chgScode(scodes);
			json = new Gson().toJson(result);			
			break;
		case "/userInfo":
			param = request.getParameter("data");
			String userId = new Gson().fromJson(param, String.class);
			System.out.println("param== "+ userId);
			
			HashMap<String,String> hMap = new HashMap<>();
			hMap = pm.userInfo(userId);
			//toJson: java객체-->json
			if(hMap != null) json = new Gson().toJson(hMap);
			System.out.println("json = "+ json);			
			break;
		}
		if(json != null) {
			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = response.getWriter();
			out.write(json);
			out.close();
			//문자 스트림 입출력 : 개행 자동 변환
			//PrintWriter : Writer, Reader, println, print, write
			//바이트(문자,파일) 스트림 입출력
			//PrintStream : OutputStream, InputStream
		}
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}

}
