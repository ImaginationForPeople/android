package org.imaginationforpeople.android.thread;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.imaginationforpeople.android.handler.ProjectsListHandler;
import org.imaginationforpeople.android.helper.DataHelper;
import org.imaginationforpeople.android.helper.UriHelper;
import org.imaginationforpeople.android.model.I4pProjectTranslation;
import org.json.JSONArray;
import org.json.JSONException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ProjectsListThread extends BaseGetJson {
	public ProjectsListThread(ProjectsListHandler h, int l) {
		handler = h;
		
		arg = l;
		switch(arg) {
		case ProjectsListHandler.BEST_PROJECTS:
			requestUri = UriHelper.getBestProjectsListUri();
			break;
		case ProjectsListHandler.LATEST_PROJECTS:
			requestUri = UriHelper.getLatestProjectsListUri();
		}
	}
	
	@Override
	protected List<I4pProjectTranslation> parseJson(String json)
			throws JSONException, JsonParseException, IOException {
		ArrayList<I4pProjectTranslation> projects = new ArrayList<I4pProjectTranslation>();
		
		JsonFactory factory = new JsonFactory();
		ObjectMapper mapper = new ObjectMapper();
		
		JSONArray jsonProjects = new JSONArray(json);
		
		int jsonLength = jsonProjects.length();
		for(int i = 0; i < jsonLength; i++) {
			if(isStopped())
				return null;
			JsonParser parser = factory.createJsonParser(jsonProjects.getString(i));
			I4pProjectTranslation project = mapper.readValue(parser, I4pProjectTranslation.class);
			if(project.getProject().getPictures().size() > 0 && !DataHelper.checkThumbFile(project.getProject().getPictures().get(0).getThumbUrl())) {
				HttpClient httpClient = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet();
				try {
					URI uri = new URI(project.getProject().getPictures().get(0).getThumbUrl());
					httpGet.setURI(uri);
					HttpResponse response = httpClient.execute(httpGet);
					Bitmap bitmap = BitmapFactory.decodeStream(response.getEntity().getContent());
					project.getProject().getPictures().get(0).setThumbBitmap(bitmap);
				} catch (URISyntaxException e) {
					Log.w("Thumbnail", "Unable to load URI " + project.getProject().getPictures().get(0).getThumbUrl());
					e.printStackTrace();
				}
			}
			projects.add(project);
		}
		
		return projects;
	}
}
