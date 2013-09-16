package org.imaginationforpeople.android2.thread;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.imaginationforpeople.android2.handler.SearchHandler;
import org.imaginationforpeople.android2.helper.UriHelper;
import org.imaginationforpeople.android2.model.I4pProjectTranslation;
import org.json.JSONArray;
import org.json.JSONException;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SearchThread extends BaseGetJson {
	private final ArrayList<I4pProjectTranslation> projects;

	public SearchThread(String search, ArrayList<I4pProjectTranslation> p, SearchHandler h) {
		requestUri = UriHelper.getFullSearchUrl(search);
		projects = p;
		handler = h;
	}

	@Override
	protected List<I4pProjectTranslation> parseJson(String json)
			throws JSONException, JsonParseException, IOException {
		JsonFactory factory = new JsonFactory();
		ObjectMapper mapper = new ObjectMapper();

		JSONArray jsonProjects = new JSONArray(json);

		int jsonLength = jsonProjects.length();
		for(int i = 0; i < jsonLength; i++) {
			if(isStopped())
				return null;
			JsonParser parser = factory.createJsonParser(jsonProjects.getString(i));
			I4pProjectTranslation project = mapper.readValue(parser, I4pProjectTranslation.class);
			projects.add(project);
		}

		return projects;
	}

	// We do nothing when this thread starts
	@Override
	protected void onStart() throws IOException {}
}
