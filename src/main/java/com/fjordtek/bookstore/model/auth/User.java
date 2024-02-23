

package com.fjordtek.bookstore.model.auth;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;



@Entity
@Table(name = "USER")
public class User {


	private static final int strMax         = 100;
	private static final int strMaxPasswd   = 1024;

	////////////////////
	// Primary key value in database

	@Id
	@GeneratedValue(
			strategy     = GenerationType.IDENTITY,
			generator    = "userIdGenerator"
			)
	@SequenceGenerator(
			name         = "userIdGenerator",
			sequenceName = "userIdSequence"
			)
	private Long id;

	////////////////////


	@Column(
			unique   = true,
			nullable = false,
			columnDefinition = "NVARCHAR(" + strMax + ")"
			)
	@NotBlank
	private String username;


	@Column(
			nullable = false,
			columnDefinition = "NVARCHAR(" + strMaxPasswd + ")"
			)
	@JsonIgnore
	@NotBlank
	private String password;

	@Email
	@Column(
			nullable = false,
			columnDefinition = "NVARCHAR(" + strMax + ")"
			)
	@NotBlank
	private String email;

	@OneToMany(
			mappedBy      = "user",
			cascade       = CascadeType.ALL,
			fetch         = FetchType.LAZY,
			orphanRemoval = true
			)

	@Transient
	private List<UserRole> userRoles;



	public void setId(Long id) {
		this.id = id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	public Long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getEmail() {
		return email;
	}


	public User() {
	}

	public User(String username, String password, String email) {
		// super();
		this.username  = username;
		this.password  = password;
		this.email     = email;
	}


	@Override
	public String toString() {
		return "[" + "id: "   + this.id       + ", " +
				"username: "  + this.username + ", " +
				"password: "  + this.password + ", " +
				"email: "     + this.email    + "]";
	}


}