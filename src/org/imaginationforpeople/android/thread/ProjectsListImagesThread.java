package org.imaginationforpeople.android.thread;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.imaginationforpeople.android.handler.ProjectsListImageHandler;
import org.imaginationforpeople.android.helper.DataHelper;
import org.imaginationforpeople.android.model.I4pProjectTranslation;
import org.imaginationforpeople.android.model.Picture;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class ProjectsListImagesThread extends Thread {
	private ProjectsListImageHandler handler;
	private List<I4pProjectTranslation> projects;
	
	private boolean stop = false;
	
	public ProjectsListImagesThread(ProjectsListImageHandler h, List<I4pProjectTranslation> p) {
		handler = h;
		projects = p;
	}

	@Override
	public void run() {
		for(I4pProjectTranslation project : projects) {
			if(project.getProject().getPictures().size() > 0 && !DataHelper.checkThumbFile(project.getProject().getPictures().get(0).getThumbUrl())) {
				HttpClient httpClient = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet();
				Bitmap bitmap = null;
				try {
					URI uri = new URI(project.getProject().getPictures().get(0).getThumbUrl());
					httpGet.setURI(uri);
					httpGet.addHeader("Accept-Encoding", "gzip");
					
					for(int i=0; i<=4; i++) {
						if(stop)
							return;
						HttpResponse response = httpClient.execute(httpGet);
						bitmap = BitmapFactory.decodeStream(response.getEntity().getContent());
						if(bitmap != null) {
							project.getProject().getPictures().get(0).setThumbBitmap(bitmap);
							break;
						}
					}
				} catch (URISyntaxException e) {
					Log.w("Thumbnail", "Unable to load URI " + project.getProject().getPictures().get(0).getThumbUrl());
					e.printStackTrace();
				} catch (ClientProtocolException e) {
					Log.e("Thumbnail", "Error with the HTTP request");
					e.printStackTrace();
				} catch (IOException e) {
					Log.e("Thumbnail", "General I/O error");
					e.printStackTrace();
				}
				
				// Unable to load bitmap after 5 tries so deleting images from the project
				if(bitmap == null) {
					project.getProject().setPictures(new ArrayList<Picture>());
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
