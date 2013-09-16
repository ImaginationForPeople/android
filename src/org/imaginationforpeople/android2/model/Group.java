package org.imaginationforpeople.android2.model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.imaginationforpeople.android2.helper.DataHelper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Group implements Parcelable {
	private int id;
	private String name;
	private String description;
	private String article;
	private String slug;
	private String language;
	@JsonProperty("resource_uri")
	private String resourceUri;
	@JsonProperty("image")
	private String imageUrl;
	private Bitmap imageBitmap;
	@JsonProperty("thumb")
	private String thumbUrl;
	private Bitmap thumbBitmap;
	private ArrayList<I4pProjectTranslation> projects;
	private ArrayList<User> subscribers;
	private ArrayList<String> tags;

	public Group() {}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getArticle() {
		return article;
	}
	public void setArticle(String article) {
		this.article = article;
	}
	public String getSlug() {
		return slug;
	}
	public void setSlug(String slug) {
		this.slug = slug;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public Bitmap getImageBitmap() {
		if(imageBitmap == null) {
			String path;
			try {
				path = new URL(imageUrl).getPath();
				if(DataHelper.checkGroupFile(imageUrl)) {
					FileInputStream file = DataHelper.openFileInput(DataHelper.FILE_PREFIX_GROUP_IMAGE + path.substring(path.lastIndexOf('/') + 1));
					imageBitmap = BitmapFactory.decodeStream(file);
				} else
					return null;
			} catch (MalformedURLException e) {
				e.printStackTrace();
				return null;
			}
		}
		return imageBitmap;
	}
	public void setImageBitmap(Bitmap imageBitmap) {
		try {
			String path = new URL(imageUrl).getPath();
			FileOutputStream file = DataHelper.openFileOutput(DataHelper.FILE_PREFIX_GROUP_IMAGE + path.substring(path.lastIndexOf('/') + 1));
			imageBitmap.compress(Bitmap.CompressFormat.PNG, 90, file);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		this.imageBitmap = imageBitmap;
	}
	public String getThumbUrl() {
		return thumbUrl;
	}
	public void setThumbUrl(String thumbUrl) {
		this.thumbUrl = thumbUrl;
	}
	public Bitmap getThumbBitmap() {
		if(thumbBitmap == null) {
			String path;
			try {
				path = new URL(thumbUrl).getPath();
				if(DataHelper.checkGroupThumbFile(thumbUrl)) {
					FileInputStream file = DataHelper.openFileInput(DataHelper.FILE_PREFIX_GROUP_THUMB + path.substring(path.lastIndexOf('/') + 1));
					thumbBitmap = BitmapFactory.decodeStream(file);
				} else
					return null;
			} catch (MalformedURLException e) {
				e.printStackTrace();
				return null;
			}
		}
		return thumbBitmap;
	}
	public void setThumbBitmap(Bitmap thumbBitmap) {
		try {
			String path = new URL(thumbUrl).getPath();
			FileOutputStream file = DataHelper.openFileOutput(DataHelper.FILE_PREFIX_GROUP_THUMB + path.substring(path.lastIndexOf('/') + 1));
			thumbBitmap.compress(Bitmap.CompressFormat.PNG, 90, file);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		this.thumbBitmap = thumbBitmap;
	}
	public ArrayList<I4pProjectTranslation> getProjects() {
		return projects;
	}
	public void setProjects(ArrayList<I4pProjectTranslation> projects) {
		this.projects = projects;
	}
	public ArrayList<User> getSubscribers() {
		return subscribers;
	}
	public void setSubscribers(ArrayList<User> subscribers) {
		this.subscribers = subscribers;
	}
	public ArrayList<String> getTags() {
		return tags;
	}
	public void setTags(ArrayList<String> tags) {
		this.tags = tags;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeStringList(tags);

		String[] stringData = new String[] {
				name,
				description,
				slug,
				language,
				imageUrl
		};
		dest.writeStringArray(stringData);

		Bundle data = new Bundle();
		data.putParcelableArrayList("projects", projects);
		data.putParcelableArrayList("subscribers", subscribers);
		dest.writeBundle(data);
	}

	public static final Parcelable.Creator<Group> CREATOR = new Parcelable.Creator<Group>() {
		@Override
		public Group createFromParcel(Parcel source) {
			return new Group(source);
		}

		@Override
		public Group[] newArray(int size) {
			return new Group[size];
		}
	};

	private Group(Parcel in) {
		id = in.readInt();
		in.readStringList(tags);

		String[] stringData = new String[5];
		in.readStringArray(stringData);
		name = stringData[0];
		description = stringData[1];
		slug = stringData[2];
		language = stringData[3];
		imageUrl = stringData[4];

		Bundle data = in.readBundle();
		projects = data.getParcelableArrayList("projects");
		subscribers = data.getParcelableArrayList("subscribers");
	}
}
