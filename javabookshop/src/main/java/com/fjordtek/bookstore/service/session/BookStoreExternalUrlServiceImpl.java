

package com.fjordtek.bookstore.service.session;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

import org.springframework.stereotype.Component;



@Component("ExternalUrl")
public class BookStoreExternalUrlServiceImpl implements BookStoreExternalUrlService {

	@Override
	public boolean getUrl(String urlString) throws IOException {

		try {
			URL url = new URL(urlString);

			URLConnection connection = url.openConnection();
			HttpURLConnection httpConnection = (HttpURLConnection) connection;
			httpConnection.setRequestMethod("HEAD");
			try {
				int responseCode = httpConnection.getResponseCode();

				if (responseCode == HttpURLConnection.HTTP_OK) {
					httpConnection.disconnect();
					return true;
				} else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
					return false;
				}
			} catch (UnknownHostException eu) {

				return false;
			}

			httpConnection.disconnect();
			return false;
		} catch (IOException ei) {
			return false;
		}
	}

}