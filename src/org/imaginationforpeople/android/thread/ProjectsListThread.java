package org.imaginationforpeople.android.thread;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.imaginationforpeople.android.handler.ProjectsListHandler;
import org.imaginationforpeople.android.helper.DataHelper;
import org.imaginationforpeople.android.helper.UriHelper;
import org.imaginationforpeople.android.model.I4pProjectTranslation;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.BitmapFactory;
import android.util.SparseArray;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ProjectsListThread extends BaseGetJson {
	public ProjectsListThread(ProjectsListHandler h) {
		handler = h;
		
		requestUri = UriHelper.getProjectsListUri();
	}
	
	@Override
	protected SparseArray<List<I4pProjectTranslation>> parseJson(String json)
			throws JSONException, JsonParseException, IOException {
		ArrayList<I4pProjectTranslation> bestProjects = new ArrayList<I4pProjectTranslation>();
		ArrayList<I4pProjectTranslation> latestProjects = new ArrayList<I4pProjectTranslation>(); 
		
		JsonFactory factory = new JsonFactory();
		ObjectMapper mapper = new ObjectMapper();
		
		JSONObject jsonProjects = new JSONObject(json);
		
		JSONArray jsonBestProjects = jsonProjects.getJSONArray("best_projects");
		int jsonLength = jsonBestProjects.length();
		for(int i = 0; i < jsonLength; i++) {
			JsonParser parser = factory.createJsonParser(jsonBestProjects.getString(i));
			I4pProjectTranslation project = mapper.readValue(parser, I4pProjectTranslation.class);
			if(project.getProject().getPictures().size() > 0) {
				String thumbUrl = project.getProject().getPictures().get(0).getThumbUrl();
				InputStream URLcontent = (InputStream) new URL(thumbUrl).getContent();
				project.getProject().getPictures().get(0).setThumbBitmap(BitmapFactory.decodeStream(URLcontent));
			}
			bestProjects.add(project);
		}
		
		JSONArray jsonLatestProjects = jsonProjects.getJSONArray("latest_projects");
		jsonLength = jsonLatestProjects.length();
		for(int i = 0; i < jsonLength; i++) {
			JsonParser parser = factory.createJsonParser(jsonLatestProjects.getString(i));
			I4pProjectTranslation project = mapper.readValue(parser, I4pProjectTranslation.class);
			if(project.getProject().getPictures().size() > 0) {
				String thumbUrl = project.getProject().getPictures().get(0).getThumbUrl();
				InputStream URLcontent = (InputStream) new URL(thumbUrl).getContent();
				project.getProject().getPictures().get(0).setThumbBitmap(BitmapFactory.decodeStream(URLcontent));
			}
			latestProjects.add(project);
		}
		
		SparseArray<List<I4pProjectTranslation>> projects = new SparseArray<List<I4pProjectTranslation>>();
		projects.put(DataHelper.BEST_PROJECTS_KEY, bestProjects);
		projects.put(DataHelper.LATEST_PROJECTS_KEY, latestProjects);
		return projects;
	}
}
