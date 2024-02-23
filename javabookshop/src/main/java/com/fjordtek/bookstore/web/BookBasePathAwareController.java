
package com.fjordtek.bookstore.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fjordtek.bookstore.model.book.Book;
import com.fjordtek.bookstore.model.book.BookEventHandler;
import com.fjordtek.bookstore.model.book.BookRepository;
import com.fjordtek.bookstore.model.book.CategoryRepository;
import com.fjordtek.bookstore.service.BookAuthorHelper;
import com.fjordtek.bookstore.service.HttpServerLogger;
import com.fjordtek.bookstore.service.session.BookStoreWebRestrictions;



@BasePathAwareController
public class BookBasePathAwareController {

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private BookAuthorHelper bookAuthorHelper;

	@Autowired
	private BookEventHandler bookEventHandler;

	@Autowired
	private HttpServerLogger httpServerLogger;

	@Autowired
	private BookStoreWebRestrictions webRestrictions;

	//////////////////////////////
	private void bookGetAndSetNestedJSON(Book book, JsonNode bookNode) {
		// Nested data: Determine nested JSON keys & their values

		String
		authorFirstName = null,
		authorLastName  = null,
		categoryName    = null
		;


		try { authorFirstName = bookNode.get("author").get("firstname").textValue(); } catch (NullPointerException e) {};
		try { authorLastName  = bookNode.get("author").get("lastname").textValue(); } catch (NullPointerException e) {};
		try { categoryName    = bookNode.get("category").get("name").textValue(); } catch (NullPointerException e) {};




		book.setAuthor(null);
		book.setCategory(null);

		book.setAuthor(
				bookAuthorHelper.detectAndSaveUpdateAuthorByName(authorFirstName, authorLastName)
				);

		if (categoryName != null) {
			book.setCategory(
					categoryRepository.findByNameIgnoreCaseContaining(categoryName)
					);
		}
	}


	@RequestMapping(

			value    = "/booklist",
			method   = RequestMethod.POST,
			consumes = "application/json",
			produces = "application/hal+json"
			)
	public ResponseEntity<?> bookAddJSONFormPost(
			@RequestBody JsonNode bookJsonNodeEntity,
			PersistentEntityResourceAssembler bookAssembler,
    		HttpServletRequest requestData,
			HttpServletResponse responseData
			) {


		if (webRestrictions.limitBookMaxCount("prod")) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		try {


			ObjectMapper jsonMapper = new ObjectMapper();
			Book bookJ = jsonMapper.readValue(bookJsonNodeEntity.toString(), Book.class);

			this.bookGetAndSetNestedJSON(bookJ, bookJsonNodeEntity);


			bookRepository.save(bookJ);


			bookEventHandler.handleAfterCreate(bookJ);

			httpServerLogger.log(requestData, responseData);


			return ResponseEntity.ok(bookAssembler.toFullResource(bookJ));

		} catch (Exception e) {
			e.printStackTrace();

			responseData.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			httpServerLogger.log(requestData, responseData);

			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(

			value    = "/booklist" + "/{id}",
			method   = RequestMethod.PUT,
			consumes = "application/json",
			produces = "application/hal+json"
			)
	public ResponseEntity<?> bookUpdateJSONFormPut(
			@RequestBody JsonNode bookPartialJsonNodeEntity,
			@PathVariable("id") Long bookId,
			PersistentEntityResourceAssembler bookAssembler,
    		HttpServletRequest requestData,
			HttpServletResponse responseData
			) {

		try {
			Book bookR = bookRepository.findById(bookId).get();

			ObjectMapper jsonMapper = new ObjectMapper();
			ObjectReader jsonReader = jsonMapper.readerForUpdating(bookR);

			Book bookJ = jsonReader.readValue(bookPartialJsonNodeEntity.toString(), Book.class);

			this.bookGetAndSetNestedJSON(bookJ, bookPartialJsonNodeEntity);

			bookRepository.save(bookJ);

			httpServerLogger.log(requestData, responseData);


			return ResponseEntity.ok(bookAssembler.toFullResource(bookJ));

		} catch (Exception e) {
			e.printStackTrace();

			responseData.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			httpServerLogger.log(requestData, responseData);

			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

	}
}