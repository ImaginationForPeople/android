package org.imaginationforpeople.android.thread;

import java.io.IOException;

import org.imaginationforpeople.android.handler.BaseHandler;
import org.imaginationforpeople.android.helper.UriHelper;
import org.imaginationforpeople.android.model.About;
import org.json.JSONException;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AboutThread extends BaseGetJson {
	public AboutThread(BaseHandler h) {
		requestUri = UriHelper.getAboutUri();
		handler = h;
	}

	@Override
	protected Boolean parseJson(String json)
			throws JSONException, JsonParseException, IOException {
		JsonFactory factory = new JsonFactory();
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		JsonParser parser = factory.createJsonParser(json);
		About about = mapper.readValue(parser, About.class);
		return (about.getVersion() != About.API_VERSION);
	}

}
