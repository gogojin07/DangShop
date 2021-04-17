package service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Forward;
import dao.MemberDao;

public class MemberMM {
	HttpServletRequest request;
	HttpServletResponse response;

	public MemberMM(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}

	public Forward access() {
		String id = request.getParameter("id");
		String pw = request.getParameter("pw");
		
		MemberDao mDao = new MemberDao();
		int result = mDao.access(id, pw);	//1:성공, -1:id부재, 0:pw부재
		mDao.close();
		
		if(result==-1) {
			request.setAttribute("msgAccess", "ID가 존재하지 않아요!");
		} else if(result==0) {
			request.setAttribute("msgAccess", "PW가 일치하지 않아요!");
		} else {
			request.getSession().setAttribute("id", id); //로그인 표식
		}
		Forward fw = new Forward();
		fw.setPath("index.jsp");
		fw.setRedirect(false);
		
		return fw;
	}

	public Forward logout() {
		request.getSession().invalidate();
		Forward fw = new Forward();
		fw.setPath("./");
		fw.setRedirect(true);
		
		return fw;
	}

}
