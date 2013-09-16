package org.imaginationforpeople.android2.thread;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import org.imaginationforpeople.android2.handler.BaseHandler;
import org.imaginationforpeople.android2.helper.UriHelper;
import org.imaginationforpeople.android2.model.Picture;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.res.AssetFileDescriptor;
import android.util.Base64;
import android.webkit.MimeTypeMap;

public class ProjectsUploadImageThread extends BasePostJson {
	private final Picture picture;
	private final String mime;
	private final AssetFileDescriptor asset;
	private final String slug;
	private final String lang;

	private String encodedImage;

	public ProjectsUploadImageThread(BaseHandler h, Picture p, String m, AssetFileDescriptor afd, String s, String l) {
		picture = p;
		mime = m;
		asset = afd;
		slug = s;
		lang = l;

		handler = h;
		requestUri = UriHelper.getPictureUploadUri();
	}

	@Override
	protected void onStart() throws IOException {
		byte[] bytes = new byte[(int) asset.getLength()];
		FileInputStream fStream = asset.createInputStream();
		BufferedInputStream stream = new BufferedInputStream(fStream);
		stream.read(bytes);
		encodedImage = Base64.encodeToString(bytes, Base64.DEFAULT);
	}

	@Override
	protected JSONObject generateJSON() throws JSONException {
		JSONObject json = new JSONObject();
		// Image informations
		json.put("desc", picture.getDesc());
		json.put("license", picture.getLicense());
		json.put("author", picture.getAuthor());
		json.put("source", picture.getSource());

		// Image file
		JSONObject image = new JSONObject();
		image.put("file", encodedImage);
		String imageName = "i4p_android." + MimeTypeMap.getSingleton().getExtensionFromMimeType(mime);
		image.put("name", imageName);
		image.put("content_type", mime);
		json.put("image", image);

		// Project informations
		JSONObject project = new JSONObject();
		project.put("slug", slug);
		project.put("lang", lang);
		json.put("project", project);

		return json;
	}
}
