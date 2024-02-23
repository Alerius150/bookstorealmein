

package com.fjordtek.bookstore.model.book;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;


@RepositoryRestResource(
		path            = "categories",
		itemResourceRel = "categories",
		exported        = true
		)
public interface CategoryRepository extends CrudRepository<Category, Long> {

	@RestResource(exported = false)
	Category findByName(@Param("name") String name);

	@RestResource(path = "category", rel = "category")
	Category findByNameIgnoreCaseContaining(@Param("name") String name);

}