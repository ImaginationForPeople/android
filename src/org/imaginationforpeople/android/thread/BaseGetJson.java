package org.imaginationforpeople.android.thread;

import java.io.IOException;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.imaginationforpeople.android.handler.BaseHandler;
import org.json.JSONException;

import com.fasterxml.jackson.core.JsonParseException;

import android.os.Message;
import android.util.Log;

public abstract class BaseGetJson extends Thread {
	protected BaseHandler handler;
	protected String requestUri;

	@Override
	public void run() {
		Message msg;
		msg = handler.obtainMessage();
		msg.arg1 = BaseHandler.STATUS_START;
		handler.sendMessage(msg);
		
		String json;
		try {
			json = sendRequest();
			msg = handler.obtainMessage();
			msg.arg1 = BaseHandler.STATUS_SUCCESS;
			msg.obj = parseJson(json);
			handler.sendMessage(msg);
		} catch (HttpHostConnectException e) {
			sendError(BaseHandler.ERROR_HTTP);
			Log.e("error", "Error when communicating with the server", e);
		} catch (ConnectTimeoutException e) {
			sendError(BaseHandler.ERROR_TIMEOUT);
			Log.e("error", "The connexion timed-out", e);
		} catch (JSONException e) {
			sendError(BaseHandler.ERROR_JSON);
			Log.e("error", "Error when analyzing the JSON", e);
		} catch (JsonParseException e) {
			sendError(BaseHandler.ERROR_JSON);
			Log.e("error", "Error with Jackson library", e);
		} catch (Exception e) {
			sendError(BaseHandler.ERROR_UNKNOWN);
			Log.e("error", "An unknown error has occured", e);
		}
	}
	
	private String sendRequest() throws ConnectTimeoutException, HttpHostConnectException, Exception {
		BasicHttpParams basicHttpParams = new BasicHttpParams();
		// Connection must time out after 10 second to prevent infinite loop
		HttpConnectionParams.setConnectionTimeout(basicHttpParams, 10000);
		HttpClient httpClient = new DefaultHttpClient(basicHttpParams);
		
		HttpGet httpGet = new HttpGet();
		httpGet.setHeader("Accept", "application/json");
		URI uri = new URI(requestUri);
		httpGet.setURI(uri);
		
		HttpResponse response = httpClient.execute(httpGet);
		return EntityUtils.toString(response.getEntity()); 
	}
	
	protected abstract Object parseJson(String json)
			throws JSONException, JsonParseException, IOException;
	
	protected void sendError(int errorCode) {
		Message msg = handler.obtainMessage();
		msg.arg1 = BaseHandler.STATUS_ERROR;
		msg.arg2 = errorCode;
		handler.sendMessage(msg);
	}
}
