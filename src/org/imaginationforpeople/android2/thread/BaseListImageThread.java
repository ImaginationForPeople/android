package org.imaginationforpeople.android2.thread;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;

public abstract class BaseListImageThread extends Thread {
	protected boolean stop = false;
	
	public void requestStop() {
		stop = true;
	}
	
	private InputStream downloadImage(String url) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet();
		try {
			URI uri = new URI(url);
			httpGet.setURI(uri);
			httpGet.addHeader("Accept-Encoding", "gzip");
			HttpResponse response = httpClient.execute(httpGet);
			return response.getEntity().getContent();
		} catch (URISyntaxException e) {
			Log.w("Thumbnail", "Unable to load URI " + url);
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			Log.e("Thumbnail", "Error with the HTTP request");
			e.printStackTrace();
		} catch (IOException e) {
			Log.e("Thumbnail", "General I/O error");
			e.printStackTrace();
		}
		return null;
	}
	
	protected Bitmap downloadBitmap(String url) {
		Bitmap bitmap;
		for(int i=0; i<=4; i++) {
			if(stop)
				break;
			
			bitmap = BitmapFactory.decodeStream(downloadImage(url));
			if(bitmap != null)
				return bitmap;
		}
		
		return null;
	}
	
	protected Drawable downloadDrawable(String url) {
		Drawable drawable;
		for(int i=0; i<=4; i++) {
			if(stop)
				break;
			
			drawable = Drawable.createFromStream(downloadImage(url), null);
			if(drawable != null)
				return drawable;
		}
		
		return null;
	}
}
