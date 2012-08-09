package org.imaginationforpeople.android.thread;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.imaginationforpeople.android.handler.ProjectsListHandler;
import org.imaginationforpeople.android.model.I4pProjectTranslation;
import org.json.JSONArray;
import org.json.JSONException;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ProjectsListThread extends BaseGetJson {
	public ProjectsListThread(ProjectsListHandler h) {
		handler = h;
		
		//TODO: generate requestUri from a common parameter
		requestUri = "http://10.0.0.9:8000/api/project/";
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
			JsonParser parser = factory.createJsonParser(jsonProjects.getString(i));
			I4pProjectTranslation project = mapper.readValue(parser, I4pProjectTranslation.class);
			projects.add(project);
		}
		
		return projects;
	}
}
