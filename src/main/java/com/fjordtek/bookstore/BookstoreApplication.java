
package com.fjordtek.bookstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.thymeleaf.templateresolver.UrlTemplateResolver;



@SpringBootApplication

public class BookstoreApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(BookstoreApplication.class, args);
	}

	@Bean
	public UrlTemplateResolver urlTemplateResolver() {
		UrlTemplateResolver urlTemplateResolver = new UrlTemplateResolver();
		urlTemplateResolver.setCacheable(true);




		urlTemplateResolver.getCharacterEncoding();
		return urlTemplateResolver;
	}

}
