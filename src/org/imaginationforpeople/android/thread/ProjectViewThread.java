package org.imaginationforpeople.android.thread;

import java.io.IOException;

import org.imaginationforpeople.android.handler.ProjectViewHandler;
import org.imaginationforpeople.android.helper.DataHelper;
import org.imaginationforpeople.android.helper.UriHelper;
import org.imaginationforpeople.android.model.I4pProjectTranslation;
import org.json.JSONException;

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
			requestUri = UriHelper.getProjectViewUri(p);
	}
	
	@Override
	protected I4pProjectTranslation parseJson(String json)
			throws JSONException, JsonParseException, IOException {
		JsonFactory factory = new JsonFactory();
		ObjectMapper mapper = new ObjectMapper();
		
		JsonParser parser = factory.createJsonParser(json);
		I4pProjectTranslation project = mapper.readValue(parser, I4pProjectTranslation.class);
		
		return project;
	}
}
