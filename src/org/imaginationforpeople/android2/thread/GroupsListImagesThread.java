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
import org.imaginationforpeople.android2.model.Group;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class GroupsListImagesThread extends Thread {
	private ListImageHandler handler;
	private List<Group> groups;
	
	private boolean stop = false;
	
	public GroupsListImagesThread(ListImageHandler h, List<Group> p) {
		handler = h;
		groups = p;
	}

	@Override
	public void run() {
		for(Group group : groups) {
			if(group.getThumbUrl() != null && !DataHelper.checkGroupThumbFile(group.getThumbUrl())) {
				HttpClient httpClient = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet();
				Bitmap bitmap = null;
				try {
					URI uri = new URI(group.getThumbUrl());
					httpGet.setURI(uri);
					httpGet.addHeader("Accept-Encoding", "gzip");
					
					for(int i=0; i<=4; i++) {
						if(stop)
							return;
						HttpResponse response = httpClient.execute(httpGet);
						bitmap = BitmapFactory.decodeStream(response.getEntity().getContent());
						if(bitmap != null) {
							group.setThumbBitmap(bitmap);
							break;
						}
					}
				} catch (URISyntaxException e) {
					Log.w("Thumbnail", "Unable to load URI " + group.getThumbUrl());
					e.printStackTrace();
				} catch (ClientProtocolException e) {
					Log.e("Thumbnail", "Error with the HTTP request");
					e.printStackTrace();
				} catch (IOException e) {
					Log.e("Thumbnail", "General I/O error");
					e.printStackTrace();
				}
				
				// Unable to load bitmap after 5 tries so deleting Thumbs from the project
				if(bitmap == null) {
					group.setThumbUrl("");
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
