package org.imaginationforpeople.android2.thread;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.imaginationforpeople.android2.handler.GroupsListHandler;
import org.imaginationforpeople.android2.helper.UriHelper;
import org.imaginationforpeople.android2.model.Group;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GroupsListThread extends BaseGetJson {
	public GroupsListThread(GroupsListHandler h) {
		handler = h;
		requestUri = UriHelper.getGroupsListUri();
	}

	@Override
	protected List<Group> parseJson(String json)
			throws JSONException, JsonParseException, IOException {
		ArrayList<Group> groups = new ArrayList<Group>();

		JsonFactory factory = new JsonFactory();
		ObjectMapper mapper = new ObjectMapper();

		JSONArray jsonProjects = new JSONObject(json).getJSONArray("objects");

		int jsonLength = jsonProjects.length();
		for(int i = 0; i < jsonLength; i++) {
			if(isStopped())
				return null;
			JsonParser parser = factory.createJsonParser(jsonProjects.getString(i));
			Group group = mapper.readValue(parser, Group.class);
			groups.add(group);
		}

		return groups;
	}

	// We do nothing when this thread starts
	@Override
	protected void onStart() throws IOException {}
}
