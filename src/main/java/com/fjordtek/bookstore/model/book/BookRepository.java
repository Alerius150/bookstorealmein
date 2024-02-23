

package com.fjordtek.bookstore.model.book;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;


@RepositoryRestResource(
		path            = "booklist",
		itemResourceRel = "booklist",
		exported        = true
		)
public interface BookRepository extends CrudRepository<Book, Long>, BookRepositoryCustom {

	@Override
	//@RestResource(exported = false)
	Optional<Book> findById(Long id);

	@RestResource(path = "title", rel = "title")
	List<Book> findByTitleIgnoreCaseContaining(@Param("name") String title);


	@RestResource(exported = false)
	Book findByIsbn(String isbn);

	@RestResource(exported = false)
	boolean existsByIsbn(String isbn);

	@Override
	List<Book> findAll();


	@Override
	@Modifying
	@Query(
			value = "DELETE FROM BOOK WHERE id = :id",
			nativeQuery = true
			)
	void deleteById(@Param("id") Long id);

}