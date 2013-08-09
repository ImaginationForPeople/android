package org.imaginationforpeople.android2.helper;

import org.apache.http.client.methods.HttpRequestBase;

import android.util.Base64;

public class NetworkHelper extends BaseHelper {
	private NetworkHelper() {}

	public static void addBasicHttpAuth(HttpRequestBase request) {
		if(ConfigHelper.API_REQUIRES_BASIC_AUTH) {
			String authentifier = ConfigHelper.BASIC_AUTH_USER + ":" + ConfigHelper.BASIC_AUTH_PSWD;
			String header = "Basic " + Base64.encodeToString(authentifier.getBytes(), Base64.NO_WRAP);
			request.setHeader("Authorization", header);
		}
	}
}
