package com.cloudinary.service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/*
 * Responsible to handle all the validations and data of the URL
 */
public class URLService {

	private static URLService instance = null;

	public static URLService getInstance() {
		if (instance == null) {
			instance = new URLService();
		}

		return instance;
	}

	private URLService() {

	}

	// Return an open connection to the URL
	public HttpURLConnection getHttpURLConnection(URL url) throws IOException {
		// We want to check the current URL
		HttpURLConnection.setFollowRedirects(false);

		HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

		// We don't need to get data
		httpURLConnection.setRequestMethod("HEAD");

		// Some websites don't like programmatic access so pretend to be a browser
		httpURLConnection
				.setRequestProperty(
						"User-Agent",
						"Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2 (.NET CLR 3.5.30729)");

		return httpURLConnection;
	}
	
	// Checking if the URL is exists
	public boolean doesURLExist(HttpURLConnection httpURLConnection) throws IOException {
		int responseCode = httpURLConnection.getResponseCode();

		// We only accept response code 200
		return responseCode == HttpURLConnection.HTTP_OK;
	}
	
	// Getting the connection's content type
	public String getURLContentType(HttpURLConnection httpURLConnection) {
		return httpURLConnection.getContentType();
	}

}
