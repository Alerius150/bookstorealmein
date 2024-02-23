

package com.fjordtek.bookstore.model.book;

import java.util.List;



public interface BookRepositoryCustom {

	void updateWithoutPriceAndWithoutPublish(Book book);

	List<Book> findAllPublished();
}