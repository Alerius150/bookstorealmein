

package com.fjordtek.bookstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fjordtek.bookstore.model.book.Author;
import com.fjordtek.bookstore.model.book.AuthorRepository;
import com.fjordtek.bookstore.model.book.Book;



@Service
public class BookAuthorHelper {

	@Autowired
	private AuthorRepository authorRepository;

	public Author detectAndSaveUpdateAuthorByName(String firstName, String lastName) {



		Author author = null;

		try {


			if (firstName != null && lastName != null) {
				author = authorRepository.findByFirstNameIgnoreCaseContainingAndLastNameIgnoreCaseContaining(
					firstName,lastName).get(0);
			}


			if (firstName != null && lastName == null) {
				author = authorRepository.findByFirstNameIgnoreCaseContaining(
						firstName).get(0);
			}


			if (firstName == null && lastName != null) {
				author = authorRepository.findByLastNameIgnoreCaseContaining(
						lastName).get(0);
			}


		} catch (IndexOutOfBoundsException e) {
			author = authorRepository.save(new Author(firstName, lastName));
		}

		return author;
	}

	public void detectAndSaveUpdateAuthorForBook(Book book) {

		Author author = this.detectAndSaveUpdateAuthorByName(
				book.getAuthor().getFirstName(),
				book.getAuthor().getLastName()
				);

		if (author != null) {
			book.getAuthor().setId(author.getId());
		} else {
			book.setAuthor(null);
		}

	}

}