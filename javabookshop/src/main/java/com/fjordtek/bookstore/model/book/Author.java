
package com.fjordtek.bookstore.model.book;

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
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;


@Entity
@Table(name = "AUTHOR")
public class Author {

	private static final int strMax         = 100;
	private static final String regexCommon = "^[a-zA-Z\\-:\\s]*$";


	@Id
	@GeneratedValue(
			strategy     = GenerationType.IDENTITY,
			generator    = "authorIdGenerator"
			)
	@SequenceGenerator(
			name         = "authorIdGenerator",
			sequenceName = "authorIdSequence"
			)
	@JsonIgnore
    private Long id;


	@Column(
			name = "firstname",
			nullable = true,
			columnDefinition = "NVARCHAR(" + strMax + ")"
			)
	@Size(
			max = strMax,
			message = "First name length must be " + strMax + " characters at most"
			)

	@Pattern(
			regexp  = regexCommon,
			message = "Invalid characters"
			)
	@JsonProperty(
			value = "firstname"
			)
	private String firstName;


	@Column(
			name = "lastname",
			nullable = true,
			columnDefinition = "NVARCHAR(" + strMax + ")"
			)
	@Size(
			max = strMax,
			message = "Last name length must be " + strMax + " characters at most"
			)

	@Pattern(
			regexp  = regexCommon,
			message = "Invalid characters"
			)
	@JsonProperty(
			value = "lastname"
			)
	private String lastName;


	@JsonIgnore

	@OneToMany(
			mappedBy      = "author",
			fetch         = FetchType.LAZY,
			cascade       = CascadeType.ALL,
			targetEntity  = Book.class

			)
	private List<Book> books;



	public void setId(Long id) {
		this.id = id;
	}

	public void setFirstName(String firstName) {


		firstName = firstName.trim();

		if (firstName.isEmpty()) {
			this.firstName = null;
		} else {
			this.firstName = firstName.substring(0,1).toUpperCase() + firstName.substring(1);
		}
	}

	public void setLastName(String lastName) {


		lastName = lastName.trim();

		if (lastName.isEmpty()) {
			this.lastName = null;
		} else {

			this.lastName = lastName.substring(0,1).toUpperCase() + lastName.substring(1);
		}
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}



	public Long getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public List<Book> getBooks() {
		return books;
	}



	public Author() {}

	public Author(String firstName, String lastName) {
		// super();
	    this.firstName  = firstName;
	    this.lastName   = lastName;
	}



	@Override
	public String toString() {
		return "[" + "id: "   + this.id        + ", " +
			   "firstname: "  + this.firstName + ", " +
			   "lastname: "   + this.lastName  + "]";
	}

}