

package com.fjordtek.bookstore.web;

import java.math.BigDecimal;
import java.time.Year;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fjordtek.bookstore.model.book.AuthorRepository;
import com.fjordtek.bookstore.model.book.Book;
import com.fjordtek.bookstore.model.book.BookEventHandler;
import com.fjordtek.bookstore.model.book.BookHash;
import com.fjordtek.bookstore.model.book.BookHashRepository;
import com.fjordtek.bookstore.model.book.BookRepository;
import com.fjordtek.bookstore.model.book.CategoryRepository;
import com.fjordtek.bookstore.service.BigDecimalPropertyEditor;
import com.fjordtek.bookstore.service.BookAuthorHelper;
import com.fjordtek.bookstore.service.HttpServerLogger;
import com.fjordtek.bookstore.service.session.BookStoreWebRestrictions;



@Controller
public class BookController {


	@InitBinder("book")
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(BigDecimal.class, new BigDecimalPropertyEditor());
	}

	@Autowired
	private Environment env;

	@Autowired
	private MessageSource msg;

	@Autowired
	private CategoryRepository   categoryRepository;

	@Autowired
	private AuthorRepository     authorRepository;

	@Autowired
	private BookRepository       bookRepository;

	@Autowired
	private BookHashRepository   bookHashRepository;

	@Autowired
	private HttpServerLogger     httpServerLogger;

	@Autowired
	private BookAuthorHelper     bookAuthorHelper;

	@Autowired
	private BookEventHandler     bookEventHandler;

	@Autowired
	private BookStoreWebRestrictions webRestrictions;



	@ModelAttribute
	public void globalAttributes(Model dataModel) {


		dataModel.addAttribute("categories", categoryRepository.findAll());
		dataModel.addAttribute("authors", authorRepository.findAll());
	}


	@RequestMapping(
			value    = "${page.url.list}",
			method   = { RequestMethod.GET, RequestMethod.POST }
			)
	public String defaultWebFormGetPost(
			HttpServletRequest requestData,
			HttpServletResponse responseData,
			Model dataModel
			) {

		dataModel.addAttribute("books", bookRepository.findAll());
		httpServerLogger.log(requestData, responseData);

		return env.getProperty("page.url.list");
	}




	@RequestMapping(
			value   = "${page.url.autherror}",
			method   = RequestMethod.POST
			)
	public String authErrorWebFormPost(
			HttpServletRequest requestData,
			HttpServletResponse responseData,
			RedirectAttributes redirectAttributes
			) {


		String authfailure = (String) requestData.getAttribute("authfailure");
		String username    = (String) requestData.getAttribute("username");

		if (!username.trim().isEmpty()) {
			authfailure = authfailure + " (" + username + ")";
		}


		redirectAttributes.addFlashAttribute("authfailure", authfailure);

		return "redirect:" + env.getProperty("page.url.list");

	}



	@PreAuthorize("hasAuthority(@BookAuth.SALES)")
	@RequestMapping(
			value    = "${page.url.add}",
			method   = { RequestMethod.GET, RequestMethod.PUT }
			)
	public String webFormAddBook(
			HttpServletRequest requestData,
			HttpServletResponse responseData,
			Model dataModel
			) {

		Book newBook = new Book();
		newBook.setYear(Year.now().getValue());
		dataModel.addAttribute("book", newBook);

		httpServerLogger.log(requestData, responseData);

		return env.getProperty("page.url.add");
	}

	@PreAuthorize("hasAuthority(@BookAuth.SALES)")
	@RequestMapping(
			value    = "${page.url.add}",
			method   = RequestMethod.POST
			)
	public String webFormSaveNewBook(
			@Valid @ModelAttribute("book") Book book,
			BindingResult bindingResult,
			HttpServletRequest requestData,
			HttpServletResponse responseData,
			RedirectAttributes redirectAttributes
			) {


		if (webRestrictions.limitBookMaxCount("prod")) {
			redirectAttributes.addFlashAttribute(
					"bookmaxcount",
					msg.getMessage(
							"security.book.count.max.msg",
							null,
							"security.book.count.max.msg [placeholder]",
							requestData.getLocale()
						)
					+ " " + env.getProperty("security.book.count.max") + "."
					);

			return "redirect:" + env.getProperty("page.url.list");
		}



		if (bookRepository.existsByIsbn(book.getIsbn())) {
			bindingResult.rejectValue(
					"isbn",
					"error.user",
					msg.getMessage(
							"book.error.isbn.exists",
							null,
							"book.error.isbn.exists [placeholder]",
							requestData.getLocale()
						)
					);
		}

		if (bindingResult.hasErrors()) {
			responseData.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			httpServerLogger.log(requestData, responseData);
			return env.getProperty("page.url.add");
		}

		httpServerLogger.log(requestData, responseData);

		bookAuthorHelper.detectAndSaveUpdateAuthorForBook(book);

		bookRepository.save(book);

		bookEventHandler.handleAfterCreate(book);

		return "redirect:" + env.getProperty("page.url.list");
	}

	//////////////////////////////
	// DELETE BOOK

	@Transactional
	@PreAuthorize("hasAuthority(@BookAuth.ADMIN)")
	@RequestMapping(
			value    = "${page.url.delete}" + "/{hash_id}",
			method   = RequestMethod.GET
			)
	public String webFormDeleteBook(
			@PathVariable("hash_id") String bookHashId,
			HttpServletRequest requestData,
			HttpServletResponse responseData
			) {

		try {
			Long bookId = new Long(bookHashRepository.findByHashId(bookHashId).getBookId());


			bookHashRepository.deleteByBookId(bookId);
			bookRepository.deleteById(bookId);

		} catch (NullPointerException e) {
			responseData.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}

		httpServerLogger.log(requestData, responseData);

		return "redirect:" + env.getProperty("page.url.list");
	}

	//////////////////////////////
	// UPDATE BOOK

	@PreAuthorize("hasAnyAuthority(@BookAuth.SALES, @BookAuth.HELPDESK)")
	@RequestMapping(
			value    = "${page.url.edit}" + "/{hash_id}",
			method   = RequestMethod.GET
			)
	public String webFormEditBook(
			@PathVariable("hash_id") String bookHashId,
			Model dataModel,
			HttpServletRequest requestData,
			HttpServletResponse responseData,
			Authentication authData
			) {

		String authorities = authData.getAuthorities().toString();

		try {
			Long bookIdFromHash = bookHashRepository.findByHashId(bookHashId).getBookId();
			Book book = bookRepository.findById(bookIdFromHash).get();

			dataModel.addAttribute("book", book);


			if ( !book.getPublish() && !authorities.contains(env.getProperty("auth.authority.sales")) ) {

				return "redirect:" + env.getProperty("page.url.list");
			}

			httpServerLogger.log(requestData, responseData);
			return env.getProperty("page.url.edit");

		} catch (NullPointerException e) {
			responseData.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			httpServerLogger.log(requestData, responseData);
			return "redirect:" + env.getProperty("page.url.list");
		}

	}


	@PreAuthorize("hasAnyAuthority(@BookAuth.SALES, @BookAuth.HELPDESK)")
	@RequestMapping(
			value    = "${page.url.edit}" + "/{hash_id}",
			method   = RequestMethod.POST
			)
	public String webFormUpdateBook(
			@Valid @ModelAttribute("book") Book book,
			BindingResult bindingResultBook,
			@ModelAttribute ("hash_id") String bookHashId,
			HttpServletRequest requestData,
			HttpServletResponse responseData,
			Authentication authData
			) {

		String authorities = authData.getAuthorities().toString();

		BookHash bookHash = bookHashRepository.findByHashId(bookHashId);

		if (bookHash == null) {
			responseData.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			httpServerLogger.log(requestData, responseData);
			return "redirect:" + env.getProperty("page.url.list");
		}


		book.setBookHash(bookHash);

		bookHash.setBook(book);


		Book bookI = bookRepository.findByIsbn(book.getIsbn());
		Long bookId = bookHash.getBookId();

		if (bookId == null) {
			bindingResultBook.rejectValue(
					"name",
					"error.user",
					msg.getMessage(
							"book.error.unknownbook",
							null,
							"book.error.unknownbook [placeholder]",
							requestData.getLocale()
						)
					);
		} else {


			if (bookI != null) {
				if (bookI.getId() != bookId) {
					bindingResultBook.rejectValue(
							"isbn",
							"error.user",
							msg.getMessage(
									"book.error.isbn.exists",
									null,
									"book.error.isbn.exists [placeholder]",
									requestData.getLocale()
								)
							);
				}
			}


			book.setId(bookId);
		}

		if (bindingResultBook.hasErrors()) {
			responseData.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			httpServerLogger.log(requestData, responseData);
			return env.getProperty("page.url.edit");
		}


		if (
				( book.getPublish() && !authorities.contains(env.getProperty("auth.authority.sales")) ) ||
				( book.getPrice() != null && !authorities.contains(env.getProperty("auth.authority.sales")) )
				) {
			//responseData.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return "redirect:" + env.getProperty("page.url.list");
		}


		//authorRepository.save(book.getAuthor());
		bookAuthorHelper.detectAndSaveUpdateAuthorForBook(book);

		if (authorities.contains(env.getProperty("auth.authority.sales")) ) {
			bookRepository.save(book);
		} else {
			bookRepository.updateWithoutPriceAndWithoutPublish(book);
		}

		httpServerLogger.log(requestData, responseData);
		return "redirect:" + env.getProperty("page.url.list");
	}


	@RequestMapping(
			value    = "${page.url.apiref}",
			method   = { RequestMethod.GET }
			)
	public String webFormRestApiRef(
			HttpServletRequest requestData,
			HttpServletResponse responseData
			) {
		httpServerLogger.log(requestData, responseData);
		return env.getProperty("page.url.apiref");
	}



	@RequestMapping(
			value    = { "*", "${page.url.error}" }
			)
	@ResponseStatus(HttpStatus.FOUND)
	public String redirectToDefaultWebForm(
			HttpServletRequest requestData,
			HttpServletResponse responseData
			)
	{
		if (requestData.getRequestURI().matches("error")) {
			responseData.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
		httpServerLogger.log(requestData, responseData);
		return "redirect:" + env.getProperty("page.url.list");
	}

	@RequestMapping(
			value    = "favicon.ico",
			method   = RequestMethod.GET
			)
	@ResponseBody
	public void faviconRequest() {

	}

}
