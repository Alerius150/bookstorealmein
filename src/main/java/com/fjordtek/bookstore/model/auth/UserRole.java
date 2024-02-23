

package com.fjordtek.bookstore.model.auth;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;



@Entity
@Table(name = "USER_ROLE")
public class UserRole {

	////////////////////
	// Primary key value in database

	@EmbeddedId
	private UserRoleCompositeKey id = new UserRoleCompositeKey();

	@ManyToOne
	@MapsId("roleId")
	private User user;

	@ManyToOne
	@MapsId("userId")
	private Role role;

	////////////////////
	// Other attributes

	////////////////////
	// Attribute setters

	public void setId(UserRoleCompositeKey id) {
		this.id = id;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	////////////////////
	// Attribute getters

	public UserRoleCompositeKey getId() {
		return id;
	}

	public User getUser() {
		return user;
	}

	public Role getRole() {
		return role;
	}

	////////////////////
	// Class constructors

	public UserRole() {
	}

	public UserRole(User user, Role role) {
		//super();
		this.user = user;
		this.role = role;
	}

	////////////////////
	// Class overrides

	@Override
	public String toString() {
		return "[" + "user: " + this.user + ", " +
				"role: "      + this.role + "]";
	}

}