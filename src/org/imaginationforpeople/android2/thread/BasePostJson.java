package org.imaginationforpeople.android2.thread;

import java.io.IOException;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.imaginationforpeople.android2.handler.BaseHandler;
import org.imaginationforpeople.android2.helper.ErrorHelper;
import org.imaginationforpeople.android2.helper.NetworkHelper;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Message;
import android.util.Log;

public abstract class BasePostJson extends Thread {
	private boolean stop = false;
	private HttpPost httpPost;

	protected BaseHandler handler;
	protected String requestUri;
	protected int arg;

	public void requestStop() {
		if(httpPost != null)
			httpPost.abort();
		stop = true;
	}

	protected boolean isStopped() {
		return stop;
	}

	public void setHandler(BaseHandler h) {
		handler = h;
	}

	@Override
	public void run() {
		if(stop)
			return;

		try {
			onStart();
			if(stop)
				return;

			String json = generateJSON().toString();
			if(stop)
				return;
			HttpResponse response = sendRequest(new StringEntity(json, HTTP.UTF_8));
			if(stop)
				return;

			Message msg = handler.obtainMessage();
			msg.arg1 = BaseHandler.STATUS_SUCCESS;
			msg.arg2 = response.getStatusLine().getStatusCode();
			msg.obj = EntityUtils.toString(response.getEntity());
			if(stop)
				return;
			handler.sendMessage(msg);
		} catch (HttpHostConnectException e) {
			sendError(ErrorHelper.ERROR_HTTP);
			Log.e("error", "Error when communicating with the server", e);
		} catch (ConnectTimeoutException e) {
			sendError(ErrorHelper.ERROR_TIMEOUT);
			Log.e("error", "The connexion timed-out", e);
		} catch (Exception e) {
			sendError(ErrorHelper.ERROR_UNKNOWN);
			Log.e("error", "An unknown error has occured", e);
			e.printStackTrace();
		}
	}

	protected abstract void onStart()
			throws IOException;

	protected abstract JSONObject generateJSON()
			throws JSONException;

	private HttpResponse sendRequest(StringEntity json) throws ConnectTimeoutException, HttpHostConnectException, Exception {
		BasicHttpParams basicHttpParams = new BasicHttpParams();
		// Connection must time out after 10 second to prevent infinite loop
		HttpConnectionParams.setConnectionTimeout(basicHttpParams, 10000);
		HttpClient httpClient = new DefaultHttpClient(basicHttpParams);

		httpPost = new HttpPost();
		NetworkHelper.addBasicHttpAuth(httpPost);
		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Content-Type", "application/json");
		URI uri = new URI(requestUri);
		httpPost.setURI(uri);
		httpPost.setEntity(json);
		NetworkHelper.addApiAuthentification(httpPost);

		return httpClient.execute(httpPost);
	}

	protected void sendError(int errorCode) {
		if(stop)
			return;
		Message msg = handler.obtainMessage();
		msg.arg1 = BaseHandler.STATUS_ERROR;
		msg.arg2 = errorCode;
		handler.sendMessage(msg);
	}
}
