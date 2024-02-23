

package com.fjordtek.bookstore.model.book;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;

//import java.sql.Timestamp;
//import javax.validation.constraints.PastOrPresent;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fjordtek.bookstore.annotation.CurrentYear;


@Entity
@Table(name = "BOOK")
public class Book {

	private static final int strMin         = 2;
	private static final int strMax         = 100;
	// We format length check in Size annotation, not here
	private static final String regexCommon = "^[a-zA-Z0-9\\-:\\s]*$";

	private static final int strIsbnFirstPartMin = 7;
	private static final int strIsbnFirstPartMax = 7;
	private static final int strIsbnLastPartMin  = 1;
	private static final int strIsbnLastPartMax  = 3;
	private static final int strIsbnMin = strIsbnFirstPartMin + strIsbnLastPartMin + 1;
	private static final int strIsbnMax = strIsbnFirstPartMax + strIsbnLastPartMax + 1;

	private static final String regexIsbn = "^[0-9]{" + strIsbnFirstPartMin + "," + strIsbnFirstPartMax + "}" +
			"\\-[0-9]{" + strIsbnLastPartMin + "," + strIsbnLastPartMax + "}$";

	private static final int yearMin     = 1800;
	private static final String minPrice = "0.01";
	private static final String maxPrice = "999.99";


	// Primary key value in database

	@Id
	@GeneratedValue(
			strategy     = GenerationType.IDENTITY,
			generator    = "bookIdGenerator"
			)
	@SequenceGenerator(
			name         = "bookIdGenerator",
			sequenceName = "bookIdSequence"
			)
	@JsonIgnore
    private Long id;



	@JsonIgnore
	@OneToOne(
			fetch        = FetchType.LAZY,
			cascade      = CascadeType.ALL,
			targetEntity = BookHash.class
			)
	@PrimaryKeyJoinColumn
	private BookHash bookhash;




	@Column(
			nullable = false,
			columnDefinition = "NVARCHAR(" + strMax + ")"
			)
	@Size(
			min = strMin, max = strMax,
			message = "Title length must be " + strMin + "-" + strMax + " characters"
			)
	@NotBlank(
			message = "Fill the book title form"
			)
	@Pattern(
			regexp  = regexCommon,
			message = "Invalid characters"
			)
	private String title;


	@JsonUnwrapped


	@JsonSerialize(using = AuthorJsonSerializer.class)
	@ManyToOne(
			fetch        = FetchType.EAGER,
			optional     = true,
			targetEntity = Author.class
			)
    @JoinColumn(
    		name     = "author_id",
    		nullable = true
    		)

	@Valid
	private Author author;


	@Column(
			nullable = false,
			columnDefinition = "INT(4)"
			)
	@Min(
			value   = yearMin,
			message = "Minimum allowed year: " + yearMin
			)
	@CurrentYear
	private int year;


	@Column(
			unique  = true,
			nullable = false
			)
	@NotBlank(
			message = "Fill the ISBN code form"
			)
	@Pattern(
			regexp  = regexIsbn,
			message = "Please use syntax: <" +
			strIsbnFirstPartMin + " integers>-<" +
			strIsbnLastPartMin + "-" +
			strIsbnLastPartMax + " integers>"
			)
	@Size(
			min     = strIsbnMin, max = strIsbnMax,
			message = "Length must be " + strIsbnMin + "-" + strIsbnMax + " characters"
			)
	private String isbn;

	//////////
	@Column(nullable = false)
	@Digits(
			integer = 3, fraction = 2,
			message = "Invalid price value"
			)
	@DecimalMin(
			value   = minPrice, message = "Too low price value. Minimum allowed: " + minPrice
			)
	@DecimalMax(
			value   = maxPrice, message = "Too high price value. Maximum allowed: " + maxPrice
			)
	private BigDecimal price;


	@JsonUnwrapped


	@JsonSerialize(using = CategoryJsonSerializer.class)
	@ManyToOne(
			fetch        = FetchType.EAGER,
			optional     = true,
			targetEntity = Category.class
			)
    @JoinColumn(
    		name     = "category_id",
    		nullable = true
    		)
	private Category category;


	@Column(
			nullable = false,
			columnDefinition = "BIT"
			)

	@JsonProperty(
			access = Access.WRITE_ONLY
			)
	private boolean publish;


	public void setId(Long id) {
		this.id = id;
	}

	public void setBookHash(BookHash bookhash) {
		this.bookhash = bookhash;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public void setPublish(boolean publish) {
		this.publish = publish;
	}

	public Long getId() {
		return id;
	}

	@JsonIgnore
	public BookHash getBookHash() {
		return bookhash;
	}

	public String getTitle() {
		return title;
	}

	public Author getAuthor() {
		return author;
	}

	public int getYear() {
		return year;
	}

	public String getIsbn() {
		return isbn;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public Category getCategory() {
		return category;
	}

	public boolean getPublish() {
		return publish;
	}



	public Book() {}

	public Book(String title, Author author, int year, String isbn, BigDecimal price, Category category, boolean publish) {
		// super();
	    this.title    = title;
	    this.author   = author;
	    this.year     = year;
	    this.isbn     = isbn;
	    this.price    = price;
	    this.category = category;
	    this.publish  = publish;
	}



	@Override
	public String toString() {
		return "[" + "id: "     + this.id       + ", " +
				"bookhash_id: " + this.bookhash + ", " +
				   "title: "    + this.title    + ", " +
				   "author: "   + this.author   + ", " +
				   "year: "     + this.year     + ", " +
				   "isbn: "     + this.isbn     + ", " +
				   "price: "    + this.price    + ", " +
				   "category: " + this.category + ", " +
				   "publish: "  + this.publish + "]";
	}

}
