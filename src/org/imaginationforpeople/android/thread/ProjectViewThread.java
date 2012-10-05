package org.imaginationforpeople.android.thread;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.imaginationforpeople.android.handler.ProjectViewHandler;
import org.imaginationforpeople.android.helper.DataHelper;
import org.imaginationforpeople.android.helper.UriHelper;
import org.imaginationforpeople.android.model.I4pProjectTranslation;
import org.imaginationforpeople.android.model.Picture;
import org.imaginationforpeople.android.model.User;
import org.json.JSONException;

import android.graphics.BitmapFactory;

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
				InputStream URLcontent = (InputStream) new URL(picture.getThumbUrl()).getContent();
				picture.setThumbBitmap(BitmapFactory.decodeStream(URLcontent));
				URLcontent = (InputStream) new URL(picture.getImageUrl()).getContent();
				picture.setImageBitmap(BitmapFactory.decodeStream(URLcontent));
			}
		}
		
		if(project.getProject().getMembers().size() > 0) {
			for(User member : project.getProject().getMembers()) {
				InputStream URLcontent = (InputStream) new URL(member.getAvatarUrl()).getContent();
				member.setAvatarBitmap(BitmapFactory.decodeStream(URLcontent));
			}
		}
		
		return project;
	}
}
