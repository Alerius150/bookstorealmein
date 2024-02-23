
package com.fjordtek.bookstore.model.auth;



public interface UserRoleRepositoryCustom {
	UserRole findByCompositeId(Long userId, Long roleId);

	void deleteByCompositeId(Long userId, Long roleId);
}