

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
import javax.validation.constraints.NotBlank;



@Entity
@Table(name = "ROLE")
public class Role {



	@Id
	@GeneratedValue(
			strategy     = GenerationType.IDENTITY,
			generator    = "roleIdGenerator"
			)
	@SequenceGenerator(
			name         = "roleIdGenerator",
			sequenceName = "roleIdSequence"
			)
	private Long id;

	@Column(
			unique   = true,
			nullable = false,
			columnDefinition = "VARCHAR(20)"
			)
	@NotBlank
	private String name;

	@OneToMany(
			mappedBy      = "role",
			cascade       = CascadeType.ALL,
			fetch         = FetchType.LAZY,
			orphanRemoval = true
			)

	@Transient
	private List<UserRole> userRoles;



	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}


	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}


	public Role() {
	}

	public Role(String name) {
		// super();
		this.name = name;
	}




	@Override
	public String toString() {
		return "[" + "id: " + this.id   + ", " +
				   "name: " + this.name + "]";
	}

}