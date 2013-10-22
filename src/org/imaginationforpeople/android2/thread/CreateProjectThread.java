package org.imaginationforpeople.android2.thread;

import java.io.IOException;

import org.imaginationforpeople.android2.handler.BaseHandler;
import org.imaginationforpeople.android2.helper.LanguageHelper;
import org.imaginationforpeople.android2.helper.UriHelper;
import org.imaginationforpeople.android2.model.I4pProjectTranslation;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CreateProjectThread extends BasePostJson {
	private final I4pProjectTranslation project;

	public CreateProjectThread(BaseHandler h, I4pProjectTranslation p) {
		project = p;

		handler = h;
		requestUri = UriHelper.getCreateProjectUri();
	}

	@Override
	protected JSONObject generateJSON() throws JSONException {
		JSONObject translatedProject = new JSONObject();
		translatedProject.put("about_section", project.getAboutSection());
		translatedProject.put("baseline", project.getBaseline());
		translatedProject.put("title", project.getTitle());
		translatedProject.put("themes", project.getThemes());

		JSONObject lang = new JSONObject();
		lang.put(LanguageHelper.getPreferredLanguageCode(), translatedProject);

		JSONObject json = new JSONObject();
		json.put("status", project.getProject().getStatus());
		json.put("lang", lang);

		// Defining topic in code as site doesn't ask for a topic
		JSONArray topics = new JSONArray();
		topics.put("social-innovation");
		json.put("topics", topics);
		return json;
	}

	@Override
	protected void onStart() throws IOException {}
}
