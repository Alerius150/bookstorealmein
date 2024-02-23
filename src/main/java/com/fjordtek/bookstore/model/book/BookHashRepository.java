

package com.fjordtek.bookstore.model.book;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource(
		path            = "bookhashes",
		itemResourceRel = "bookhashes",
		exported        = false
		)
public interface BookHashRepository extends CrudRepository<BookHash, String> {

	BookHash findByHashId(String bookHashId);

	BookHash findByBookId(@Param("bookid") Long bookId);


	@Modifying
	@Query(
			value = "DELETE FROM BOOK_HASH WHERE book_id = :bookid",
			nativeQuery = true
			)
	void deleteByBookId(@Param("bookid") Long bookId);

}