package org.imaginationforpeople.android2.thread;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.imaginationforpeople.android2.handler.ListImageHandler;
import org.imaginationforpeople.android2.helper.DataHelper;
import org.imaginationforpeople.android2.model.User;

import android.graphics.drawable.Drawable;
import android.util.Log;

public class UsersListImagesThread extends Thread {
	private ListImageHandler handler;
	private List<User> users;
	
	private boolean stop = false;
	
	public UsersListImagesThread(ListImageHandler h, List<User> u) {
		handler = h;
		users = u;
	}

	@Override
	public void run() {
		for(User user : users) {
			if(!"".equals(user.getAvatarUrl()) && !DataHelper.checkAvatarFile(user.getAvatarUrl())) {
				HttpClient httpClient = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet();
				Drawable drawable = null;
				try {
					URI uri = new URI(user.getAvatarUrl());
					httpGet.setURI(uri);
					httpGet.addHeader("Accept-Encoding", "gzip");
					
					for(int i=0; i<=4; i++) {
						if(stop)
							return;
						HttpResponse response = httpClient.execute(httpGet);
						drawable = Drawable.createFromStream(response.getEntity().getContent(), null);
						if(drawable != null) {
							user.setAvatarDrawable(drawable);
							break;
						}
					}
				} catch (URISyntaxException e) {
					Log.w("Thumbnail", "Unable to load URI " + user.getAvatarUrl());
					e.printStackTrace();
				} catch (ClientProtocolException e) {
					Log.e("Thumbnail", "Error with the HTTP request");
					e.printStackTrace();
				} catch (IOException e) {
					Log.e("Thumbnail", "General I/O error");
					e.printStackTrace();
				}
				
				// Unable to load bitmap after 5 tries so deleting images from the project
				if(drawable == null) {
					user.setAvatarUrl("");
				}
				
				if(stop)
					return;
				
				handler.sendEmptyMessage(0);
			}
			if(stop)
				return;
		}
	}
	
	public void requestStop() {
		stop = true;
	}
}
