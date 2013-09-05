package org.imaginationforpeople.android2.helper;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.methods.HttpRequestBase;

import android.util.Base64;
import android.util.Log;

public class NetworkHelper extends BaseHelper {
	private NetworkHelper() {}

	public static enum API_AUTH_TYPES {NONE, HEADER, URL};

	public static void addBasicHttpAuth(HttpRequestBase request) {
		if(ConfigHelper.API_REQUIRES_BASIC_AUTH) {
			String authentifier = ConfigHelper.BASIC_AUTH_USER + ":" + ConfigHelper.BASIC_AUTH_PSWD;
			String header = "Basic " + Base64.encodeToString(authentifier.getBytes(), Base64.NO_WRAP);
			request.setHeader("Authorization", header);
		}
	}

	@SuppressWarnings("incomplete-switch")
	public static void addApiAuthentification(HttpRequestBase request) {
		switch(ConfigHelper.API_AUTH) {
		case HEADER:
			String authentifier = ConfigHelper.API_AUTH_USER + ":" + ConfigHelper.API_AUTH_KEY;
			request.setHeader("Authorization", "ApiKey " + authentifier);
			break;
		case URL:
			URI uri = request.getURI();
			StringBuilder newUrl = new StringBuilder();
			newUrl.append(uri.getScheme());
			newUrl.append("://");
			newUrl.append(uri.getAuthority());
			newUrl.append(uri.getPath());
			newUrl.append("?");
			if(uri.getQuery() != null) {
				newUrl.append(uri.getQuery());
				newUrl.append("&");
			}
			newUrl.append("username=");
			newUrl.append(ConfigHelper.API_AUTH_USER);
			newUrl.append("&api_key=");
			newUrl.append(ConfigHelper.API_AUTH_KEY);
			try {
				URI newURI = new URI(newUrl.toString());
				request.setURI(newURI);
			} catch (URISyntaxException e) {
				Log.e("error", "Cannot generate new URI for URL API authentication");
				Log.e("error", "Tried to generate: " + newUrl);
				e.printStackTrace();
			}
		}
	}
}
