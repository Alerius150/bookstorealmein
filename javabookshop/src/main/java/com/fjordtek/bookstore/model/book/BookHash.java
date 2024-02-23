

package com.fjordtek.bookstore.model.book;

import java.security.SecureRandom;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;



@Entity
@Table(name = "BOOK_HASH")
public class BookHash {

	@Id
	@GeneratedValue(
			strategy   =  GenerationType.SEQUENCE,
			generator  = "bookHashIdGenerator"
			)
	@GenericGenerator(
			name       = "bookHashIdGenerator",
			strategy   = "foreign",
			parameters = { @Parameter(name = "property", value = "book") }
			)
	@Column(
			name       = "book_id",
			unique     = true,
			nullable   = false
			)
	@JsonIgnore
	private Long bookId;



	@OneToOne(
			cascade      = { CascadeType.MERGE, CascadeType.REMOVE },
			fetch        = FetchType.LAZY,
			mappedBy     = "bookhash",
			targetEntity = Book.class
			)
	@PrimaryKeyJoinColumn(
			referencedColumnName = "id"
			)
    private Book book;


	@Column(
			name             = "hash_id",
			unique           = true,
			columnDefinition = "CHAR(32)",
			updatable        = false,
			nullable         = false
			)
	@JsonProperty(
			value = "hashid"
			)
	private String hashId;


	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}

	public void setBook(Book book) {
		this.book = book;
	}


	public void setHashId() {

		byte[] byteInit = new byte[16];

		new SecureRandom().nextBytes(byteInit);

		StringBuilder shaStringBuilder = new StringBuilder();

		for (byte b : byteInit) {
			shaStringBuilder.append(String.format("%02x", b));
		}

		this.hashId = shaStringBuilder.toString();

	}



	public Long getBookId() {
		return bookId;
	}

	public Book getBook() {
		return book;
	}

	public String getHashId() {
		return hashId;
	}



	public BookHash() {
		this.setHashId();
	}



	@Override
	public String toString() {
		return "[" + "book_id: " + this.bookId + ", " +
				"hash_id: " + this.hashId + "]";

	}

}
