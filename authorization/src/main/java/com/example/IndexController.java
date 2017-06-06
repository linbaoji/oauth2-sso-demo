package com.example;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

	@Autowired
	@Qualifier("authenticationManager")
	private AuthenticationManager authManager;

	@RequestMapping(value = "login",method=RequestMethod.GET)
	String home(@RequestParam(value = "error", required = false) String error, HttpServletRequest request,
			HttpServletResponse response) {

		HttpSession session = request.getSession();

		String memberId = (String) session.getAttribute("memberId");
		String password = (String) session.getAttribute("password");
		String username = null;
		if (request.getMethod().equals("POST")) {
			username = request.getParameter("username");
			password = request.getParameter("password");
		}else{
			username =  memberId;
		}

	    
		// memberId = "user";
		// password = "password";;
		if (username != null && password != null) {
			// 创建一个用户名密码登陆信息
			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);

			try {
				// 用户名密码登陆效验
				Authentication authResult = authManager.authenticate(token);

				// 在当前访问线程中设置 authResult
				SecurityContextHolder.getContext().setAuthentication(authResult);

				session.removeAttribute("memberId");
				session.removeAttribute("password");
				if (null != username) {
					return "redirect:/oauth/authorize";
				} else {
					return "redirect:/";
				}
			} catch (AuthenticationException e) {
				e.printStackTrace();
			}
		}
		return "login";

	}
	
	@ResponseBody
	@RequestMapping(value = "/cuser")
	public Principal user(Principal user) {
		return user;
	}

//	@RequestMapping(value = "/")
//	String index() {
//		return "index";
//	}
}
