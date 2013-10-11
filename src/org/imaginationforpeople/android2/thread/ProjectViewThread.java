package org.imaginationforpeople.android2.thread;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

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
		ArrayList<Picture> pictures = project.getProject().getPictures();
		ArrayList<Picture> unloadablePictures = new ArrayList<Picture>();
		if(pictures.size() > 0) {
			for(Picture picture : pictures) {
				if(isStopped())
					return null;
				URI uri;
				if(!DataHelper.checkThumbFile(picture.getThumbUrl())) {
					for(int i = 0; i<5; i++) {
						if(isStopped())
							return null;
						try {
							uri = new URI(picture.getThumbUrl());
							Bitmap bitmap = BitmapFactory.decodeStream(download(uri));
							if(bitmap != null) {
								picture.setThumbBitmap(bitmap);
								break;
							}
						} catch (URISyntaxException e) {
							Log.w("Thumbnail", "Unable to load URI " + picture.getThumbUrl());
							e.printStackTrace();
						}
					}
					if(picture.getThumbBitmap() == null)
						unloadablePictures.add(picture);
				}
				if(picture.getThumbBitmap() != null
						&& !DataHelper.checkImageFile(picture.getImageUrl())) {
					for(int i = 0; i<5; i++) {
						if(isStopped())
							return null;
						try {
							uri = new URI(picture.getImageUrl());
							Bitmap bitmap = BitmapFactory.decodeStream(download(uri));
							if(bitmap != null) {
								picture.setImageBitmap(bitmap);
								break;
							}
						} catch (URISyntaxException e) {
							Log.w("Thumbnail", "Unable to load URI " + picture.getImageUrl());
							e.printStackTrace();
						}
					}
					if(picture.getImageBitmap() == null)
						unloadablePictures.add(picture);
				}
			}
			if(isStopped())
				return null;
			for(Picture picture : unloadablePictures)
				pictures.remove(picture);
		}

		if(project.getProject().getMembers().size() > 0) {
			for(User member : project.getProject().getMembers()) {
				if(isStopped())
					return null;
				if(!DataHelper.checkAvatarFile(member.getAvatarUrl())) {
					for(int i = 0; i<5; i++) {
						if(isStopped())
							return null;
						try {
							URI uri = new URI(member.getAvatarUrl());
							Drawable drawable = Drawable.createFromStream(download(uri), null);
							if(drawable != null) {
								member.setAvatarDrawable(drawable);
								break;
							}
						} catch (URISyntaxException e) {
							Log.w("Thumbnail", "Unable to load URI " + member.getAvatarUrl());
							e.printStackTrace();
						}
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
