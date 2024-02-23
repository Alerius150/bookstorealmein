

package com.fjordtek.bookstore.service.session;

import java.io.IOException;



public interface BookStoreExternalUrlService {

	boolean getUrl(String url) throws IOException;

}