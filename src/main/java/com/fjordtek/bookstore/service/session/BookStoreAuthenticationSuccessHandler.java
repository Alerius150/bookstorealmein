

package com.fjordtek.bookstore.service.session;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;




public class BookStoreAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Override
	public void onAuthenticationSuccess(
			HttpServletRequest requestData,
			HttpServletResponse responseData,
			Authentication authentication
			) throws IOException, ServletException {



		redirectStrategy.sendRedirect(requestData, responseData, "/");
		//responseData.sendRedirect("/");

	}

}