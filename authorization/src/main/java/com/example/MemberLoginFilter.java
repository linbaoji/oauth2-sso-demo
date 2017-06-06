package com.example;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
/*
 * 
 */
public class MemberLoginFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		String memberId = request.getParameter("memberId");
		if (memberId != null) {

			String password = request.getParameter("password");

			System.out.println("memberId : " + memberId);
			System.out.println("password : " + password);

			HttpSession session = ((HttpServletRequest) request).getSession(true);

			session.setAttribute("memberId", memberId);
			session.setAttribute("password", password);

		}

		chain.doFilter(request, response);

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
