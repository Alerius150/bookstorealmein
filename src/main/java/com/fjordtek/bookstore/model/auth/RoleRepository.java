
package com.fjordtek.bookstore.model.auth;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource(
		path            = "roles",
		itemResourceRel = "roles",
		exported        = true
		)
public interface RoleRepository extends CrudRepository<Role, Long> {

	Role findByName(String string);

}