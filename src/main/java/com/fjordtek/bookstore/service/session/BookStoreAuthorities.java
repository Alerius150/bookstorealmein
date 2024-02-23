

package com.fjordtek.bookstore.service.session;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;



@Component("BookAuth")
public class BookStoreAuthorities {

	@Autowired
	private Environment env;

	public String
	ADMIN,
	HELPDESK,
	SALES,
	USER
	;

	@PostConstruct
	private void constructAuthorities() {
		this.ADMIN    = env.getProperty("auth.authority.admin");
		this.HELPDESK = env.getProperty("auth.authority.helpdesk");
		this.SALES    = env.getProperty("auth.authority.sales");
		this.USER     = env.getProperty("auth.authority.user");
	}

}


