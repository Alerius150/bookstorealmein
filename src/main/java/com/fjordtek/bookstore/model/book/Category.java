

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
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;



@Entity
@Table(name = "CATEGORY")
public class Category {


	@Id
	@GeneratedValue(
			strategy     = GenerationType.IDENTITY,
			generator    = "categoryIdGenerator"
			)
	@SequenceGenerator(
			name         = "categoryIdGenerator",
			sequenceName = "categoryIdSequence"
			)
	@JsonIgnore
	private Long id;


	@Column(
			nullable = false,
			unique   = true,
			columnDefinition = "NVARCHAR(50)"
			)
	@NotNull
	private String name;


	@JsonIgnore

	@OneToMany(
			mappedBy     = "category",
			fetch        = FetchType.LAZY,
			cascade      = CascadeType.ALL,
			targetEntity = Book.class
			)
	private List<Book> books;



	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}



	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<Book> getBooks() {
		return books;
	}



	public Category() {
	}

	public Category(String name) {
		this.name = name;
	}



	@Override
	public String toString() {
		return "[" + "id: " + this.id       + ", " +
			   "name: "     + this.name     + "]";
	}

}