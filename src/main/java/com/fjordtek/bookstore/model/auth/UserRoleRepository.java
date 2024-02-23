

package com.fjordtek.bookstore.model.auth;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource(
		path            = "userroles",
		itemResourceRel = "userroles",
		exported        = true
		)
public interface UserRoleRepository extends CrudRepository<UserRole, Long>, UserRoleRepositoryCustom {
}