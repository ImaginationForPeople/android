package org.imaginationforpeople.android.thread;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.imaginationforpeople.android.handler.ProjectViewHandler;
import org.imaginationforpeople.android.helper.DataHelper;
import org.imaginationforpeople.android.helper.UriHelper;
import org.imaginationforpeople.android.model.I4pProjectTranslation;
import org.imaginationforpeople.android.model.Picture;
import org.imaginationforpeople.android.model.User;
import org.json.JSONException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ProjectViewThread extends BaseGetJson {
	public ProjectViewThread(ProjectViewHandler h, int p) {
		handler = h;
		
		if(p == DataHelper.PROJECT_RANDOM)
			requestUri = UriHelper.getRandomProjectViewUri();
		else
			requestUri = UriHelper.getProjectViewUriById(p);
	}
	
	public ProjectViewThread(ProjectViewHandler h, String lc, String s) {
		handler = h;
		
		requestUri = UriHelper.getProjectViewUriBySlug(lc, s);
	}
	
	@Override
	protected I4pProjectTranslation parseJson(String json)
			throws JSONException, JsonParseException, IOException {
		JsonFactory factory = new JsonFactory();
		ObjectMapper mapper = new ObjectMapper();
		
		JsonParser parser = factory.createJsonParser(json);
		I4pProjectTranslation project = mapper.readValue(parser, I4pProjectTranslation.class);
		
		if(project.getProject().getPictures().size() > 0) {
			for(Picture picture : project.getProject().getPictures()) {
				if(isStopped())
					return null;
				HttpClient httpClient;
				HttpGet httpGet;
				if(!DataHelper.checkThumbFile(picture.getThumbUrl())) {
					httpClient = new DefaultHttpClient();
					httpGet = new HttpGet();
					try {
						httpGet.setURI(new URI(picture.getThumbUrl()));
						HttpResponse response = httpClient.execute(httpGet);
						Bitmap bitmap = BitmapFactory.decodeStream(response.getEntity().getContent());
						picture.setThumbBitmap(bitmap);
					} catch (URISyntaxException e) {
						Log.w("Thumbnail", "Unable to load URI " + picture.getThumbUrl());
						e.printStackTrace();
					}
				}
				if(!DataHelper.checkImageFile(picture.getImageUrl())) {
					httpClient = new DefaultHttpClient();
					httpGet = new HttpGet();
					try {
						httpGet.setURI(new URI(picture.getImageUrl()));
						HttpResponse response = httpClient.execute(httpGet);
						Bitmap bitmap = BitmapFactory.decodeStream(response.getEntity().getContent());
						picture.setImageBitmap(bitmap);
					} catch (URISyntaxException e) {
						Log.w("Thumbnail", "Unable to load URI " + picture.getImageUrl());
						e.printStackTrace();
					}
				}
			}
		}
		
		if(project.getProject().getMembers().size() > 0) {
			for(User member : project.getProject().getMembers()) {
				if(isStopped())
					return null;
				HttpClient httpClient = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet();
				try {
					httpGet.setURI(new URI(member.getAvatarUrl()));
					HttpResponse response = httpClient.execute(httpGet);
					Drawable drawable = Drawable.createFromStream(response.getEntity().getContent(), null);
					member.setAvatarDrawable(drawable);
				} catch (URISyntaxException e) {
					Log.w("Thumbnail", "Unable to load URI " + member.getAvatarUrl());
					e.printStackTrace();
				}
			}
		}
		
		return project;
	}
}
