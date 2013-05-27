package org.imaginationforpeople.android2.thread;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.imaginationforpeople.android2.handler.ProjectViewHandler;
import org.imaginationforpeople.android2.helper.DataHelper;
import org.imaginationforpeople.android2.helper.UriHelper;
import org.imaginationforpeople.android2.model.I4pProjectTranslation;
import org.imaginationforpeople.android2.model.Picture;
import org.imaginationforpeople.android2.model.User;
import org.json.JSONException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;

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
				if(isStopped())
					return null;
				URI uri;
				if(!DataHelper.checkThumbFile(picture.getThumbUrl())) {
					try {
						uri = new URI(picture.getThumbUrl());
						Bitmap bitmap = BitmapFactory.decodeStream(download(uri));
						picture.setThumbBitmap(bitmap);
					} catch (URISyntaxException e) {
						Log.w("Thumbnail", "Unable to load URI " + picture.getThumbUrl());
						e.printStackTrace();
					}
				}
				if(!DataHelper.checkImageFile(picture.getImageUrl())) {
					try {
						uri = new URI(picture.getImageUrl());
						Bitmap bitmap = BitmapFactory.decodeStream(download(uri));
						picture.setImageBitmap(bitmap);
					} catch (URISyntaxException e) {
						Log.w("Thumbnail", "Unable to load URI " + picture.getImageUrl());
						e.printStackTrace();
					}
				}
			}
		}
		
		if(project.getProject().getMembers().size() > 0) {
			for(User member : project.getProject().getMembers()) {
				if(isStopped())
					return null;
				if(!DataHelper.checkAvatarFile(member.getAvatarUrl())) {
					try {
						URI uri = new URI(member.getAvatarUrl());
						Drawable drawable = Drawable.createFromStream(download(uri), null);
						member.setAvatarDrawable(drawable);
					} catch (URISyntaxException e) {
						Log.w("Thumbnail", "Unable to load URI " + member.getAvatarUrl());
						e.printStackTrace();
					}
				}
			}
		}
		
		return project;
	}
	
	// We do nothing when this thread starts
	@Override
	protected void onStart() {}
}
