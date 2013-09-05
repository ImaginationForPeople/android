package org.imaginationforpeople.android2.thread;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.imaginationforpeople.android2.handler.BaseHandler;
import org.imaginationforpeople.android2.helper.ErrorHelper;
import org.imaginationforpeople.android2.helper.NetworkHelper;
import org.json.JSONException;

import com.fasterxml.jackson.core.JsonParseException;

import android.os.Message;
import android.util.Log;

public abstract class BaseGetJson extends Thread {
	private boolean stop = false;
	
	protected BaseHandler handler;
	protected String requestUri;
	protected int arg;
	
	public void requestStop() {
		stop = true;
	}
	
	protected boolean isStopped() {
		return stop;
	}
	
	@Override
	public void run() {
		if(stop)
			return;
		
		Message msg;
		msg = handler.obtainMessage();
		msg.arg1 = BaseHandler.STATUS_START;
		msg.arg2 = arg;
		handler.sendMessage(msg);
		
		String json;
		try {
			onStart();
			
			json = sendRequest();
			if(stop)
				return;
			Object parsedJson = parseJson(json);
			if(stop)
				return;
			msg = handler.obtainMessage();
			msg.arg1 = BaseHandler.STATUS_SUCCESS;
			msg.arg2 = arg;
			msg.obj = parsedJson;
			if(stop)
				return;
			handler.sendMessage(msg);
		} catch (HttpHostConnectException e) {
			sendError(ErrorHelper.ERROR_HTTP);
			Log.e("error", "Error when communicating with the server", e);
		} catch (ConnectTimeoutException e) {
			sendError(ErrorHelper.ERROR_TIMEOUT);
			Log.e("error", "The connexion timed-out", e);
		} catch (JSONException e) {
			sendError(ErrorHelper.ERROR_JSON);
			Log.e("error", "Error when analyzing the JSON", e);
			Log.e("error", "Request URL: " + requestUri, e);
		} catch (JsonParseException e) {
			sendError(ErrorHelper.ERROR_JSON);
			Log.e("error", "Error with Jackson library", e);
			Log.e("error", "Request URL: " + requestUri, e);
		} catch (Exception e) {
			sendError(ErrorHelper.ERROR_UNKNOWN);
			Log.e("error", "An unknown error has occured", e);
			e.printStackTrace();
		}
	}
	
	protected abstract void onStart()
			throws IOException;
	
	private String sendRequest() throws ConnectTimeoutException, HttpHostConnectException, Exception {
		BasicHttpParams basicHttpParams = new BasicHttpParams();
		// Connection must time out after 10 second to prevent infinite loop
		HttpConnectionParams.setConnectionTimeout(basicHttpParams, 10000);
		HttpClient httpClient = new DefaultHttpClient(basicHttpParams);
		
		HttpGet httpGet = new HttpGet();
		NetworkHelper.addBasicHttpAuth(httpGet);
		httpGet.setHeader("Accept", "application/json");
		URI uri = new URI(requestUri);
		httpGet.setURI(uri);
		
		HttpResponse response = httpClient.execute(httpGet);
		return EntityUtils.toString(response.getEntity(), HTTP.UTF_8); 
	}
	
	protected abstract Object parseJson(String json)
			throws JSONException, JsonParseException, IOException;
	
	protected void sendError(int errorCode) {
		if(stop)
			return;
		Message msg = handler.obtainMessage();
		msg.arg1 = BaseHandler.STATUS_ERROR;
		msg.arg2 = errorCode;
		handler.sendMessage(msg);
	}
	
	protected InputStream download(URI uri) throws IOException {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet();
		httpGet.setURI(uri);
		httpGet.addHeader("Accept-Encoding", "gzip");
		HttpResponse response = httpClient.execute(httpGet);
		return response.getEntity().getContent();
	}
}
