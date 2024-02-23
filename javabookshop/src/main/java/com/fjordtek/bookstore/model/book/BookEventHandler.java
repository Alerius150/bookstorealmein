

package com.fjordtek.bookstore.model.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;



@Component
@RepositoryEventHandler(Book.class)
public class BookEventHandler {

	@Autowired
	private BookHashRepository bookHashRepository;


	@HandleAfterCreate
	public void handleAfterCreate(Book book) {
		BookHash bookHash = new BookHash();


		int i = 0;
		while (i < 5) {
			if (bookHashRepository.findByHashId(bookHash.getHashId()) != null) {
				bookHash = new BookHash();
			} else {
				break;
			}
			i++;
		}

		book.setBookHash(bookHash);
		bookHash.setBook(book);

		bookHashRepository.save(bookHash);
	}
}