package org.imaginationforpeople.android2.thread;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.imaginationforpeople.android2.handler.GroupViewHandler;
import org.imaginationforpeople.android2.helper.DataHelper;
import org.imaginationforpeople.android2.helper.UriHelper;
import org.imaginationforpeople.android2.model.Group;
import org.json.JSONException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GroupViewThread extends BaseGetJson {
	public GroupViewThread(GroupViewHandler h, String s) {
		handler = h;
		
		requestUri = UriHelper.getGroupViewUriBySlug(s);
	}
	
	@Override
	protected Group parseJson(String json)
			throws JSONException, JsonParseException, IOException {
		JsonFactory factory = new JsonFactory();
		ObjectMapper mapper = new ObjectMapper();
		
		JsonParser parser = factory.createJsonParser(json);
		Group group = mapper.readValue(parser, Group.class);
		
		if(group.getThumbUrl() != null) {
			if(isStopped())
				return null;
			URI uri;
			if(!DataHelper.checkThumbFile(group.getThumbUrl())) {
				try {
					uri = new URI(group.getThumbUrl());
					Bitmap bitmap = BitmapFactory.decodeStream(download(uri));
					group.setThumbBitmap(bitmap);
				} catch (URISyntaxException e) {
					Log.w("Thumbnail", "Unable to load URI " + group.getThumbUrl());
					e.printStackTrace();
				}
			}
			if(!DataHelper.checkGroupFile(group.getImageUrl())) {
				try {
					uri = new URI(group.getImageUrl());
					Bitmap bitmap = BitmapFactory.decodeStream(download(uri));
					group.setImageBitmap(bitmap);
				} catch (URISyntaxException e) {
					Log.w("Thumbnail", "Unable to load URI " + group.getImageUrl());
					e.printStackTrace();
				}
			}
		}
		
		return group;
	}
	
	// We do nothing when this thread starts
	@Override
	protected void onStart() {}
}
