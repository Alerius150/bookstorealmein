

package com.fjordtek.bookstore.model.auth;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource(
		path            = "users",
		itemResourceRel = "users",
		exported        = true
		)
public interface UserRepository extends CrudRepository<User, Long> {

	User findByUsername(String username);

}