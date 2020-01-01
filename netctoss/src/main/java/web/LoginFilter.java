package web;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用来做登录检查，判断用户是否登录。
 */
public class LoginFilter implements Filter {

	public void destroy() {
		
	}

	public void doFilter(
		ServletRequest req, 
		ServletResponse res,
		FilterChain chain) 
		throws IOException, ServletException {
		HttpServletRequest request = 
			(HttpServletRequest) req;
		HttpServletResponse response = 
			(HttpServletResponse) res;
		//1.先明确校验的范围，
		//有3个请求不必判断：
		//toLogin.do,createImg.do,login.do
		String[] paths = {"/toLogin.do",
			"/createImg.do", "/login.do"};
		String path = request.getServletPath();
		for(String p : paths) {
			if(p.equals(path)) {
				chain.doFilter(req, res);
				return;
			}
		}
		//2.如果不是上述范围内的路径，
		//则是我们要做登录检查的目标。
		String adminCode = (String)
			request.getSession()
			.getAttribute("adminCode");
		if(adminCode == null) {
			//没登录，重定向到登录页
			response.sendRedirect(
				request.getContextPath() + 
				"/toLogin.do");
		} else {
			//已登录，放行
			chain.doFilter(req, res);
		}
	}

	public void init(FilterConfig arg0) 
		throws ServletException {
		
	}

}
