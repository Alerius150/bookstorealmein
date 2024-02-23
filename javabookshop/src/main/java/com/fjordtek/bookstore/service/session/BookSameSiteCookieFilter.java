

package com.fjordtek.bookstore.service.session;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.GenericFilterBean;



public class BookSameSiteCookieFilter extends GenericFilterBean {

	@Override
	public void doFilter(
			ServletRequest requestData,
			ServletResponse responseData,
			FilterChain chain)
			throws IOException, ServletException {

			HttpServletResponse httpResponse = (HttpServletResponse) responseData;

			httpResponse.setHeader("Set-Cookie", "SameSite=strict;");
			chain.doFilter(requestData, responseData);

	}


}