

package com.fjordtek.bookstore.model.auth;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;



@Embeddable
public class UserRoleCompositeKey implements Serializable {

	private static final long serialVersionUID = 2889337731246989510L;

	@NotNull
	private Long userId;
	@NotNull
	private Long roleId;



	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}



	public Long getUserId() {
		return userId;
	}

	public Long getRoleId() {
		return roleId;
	}



	public UserRoleCompositeKey() {
	}

	public UserRoleCompositeKey(Long userId, Long roleId) {

		this.userId = userId;
		this.roleId = roleId;
	}



	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}

		UserRoleCompositeKey that = (UserRoleCompositeKey) obj;

		return Objects.equals(userId, that.userId) &&
				Objects.equals(roleId, that.roleId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(userId, roleId);
	}

	@Override
	public String toString() {
		return "[" + "user_id: " + this.userId + ", " +
				"role_id: "      + this.roleId + "]";
	}

}
