package org.imaginationforpeople.android2.model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.imaginationforpeople.android2.helper.DataHelper;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class User implements Parcelable {
	private String fullname;
	private String username;
	@JsonProperty("avatar")
	private String avatarUrl;
	@JsonIgnore
	private Drawable avatarDrawable;

	public User() {}

	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getAvatarUrl() {
		return avatarUrl;
	}
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}
	public Drawable getAvatarDrawable() {
		if(avatarDrawable == null) {
			String path;
			try {
				path = new URL(avatarUrl).getPath();
				if(DataHelper.checkAvatarFile(avatarUrl)) {
					FileInputStream file = DataHelper.openFileInput(DataHelper.FILE_PREFIX_USER_AVATAR + path.substring(path.lastIndexOf('/') + 1));
					avatarDrawable = Drawable.createFromStream(file, null);
				} else
					return null;
			} catch (MalformedURLException e) {
				e.printStackTrace();
				return null;
			}
		}
		return avatarDrawable;
	}
	public void setAvatarDrawable(Drawable avatarDrawable) {
		try {
			String path = new URL(avatarUrl).getPath();
			FileOutputStream file = DataHelper.openFileOutput(DataHelper.FILE_PREFIX_USER_AVATAR + path.substring(path.lastIndexOf('/') + 1));
			((BitmapDrawable) avatarDrawable).getBitmap().compress(Bitmap.CompressFormat.PNG, 90, file);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		this.avatarDrawable = avatarDrawable;
	}

	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeStringArray(new String[] {
				fullname,
				username,
				avatarUrl
		});
	}

	public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
		@Override
		public User createFromParcel(Parcel source) {
			return new User(source);
		}

		@Override
		public User[] newArray(int size) {
			return new User[size];
		}
	};

	private User(Parcel source) {
		String[] stringData = new String[3];
		source.readStringArray(stringData);
		fullname = stringData[0];
		username = stringData[1];
		avatarUrl = stringData[2];
	}
}
