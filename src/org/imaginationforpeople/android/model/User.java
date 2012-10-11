package org.imaginationforpeople.android.model;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User implements Parcelable {
	private String fullname;
	private String username;
	@JsonProperty("avatar")
	private String avatarUrl;
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
		return avatarDrawable;
	}
	public void setAvatarDrawable(Drawable avatarDrawable) {
		this.avatarDrawable = avatarDrawable;
	}
	
	public int describeContents() {
		return 0;
	}
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeStringArray(new String[] {
			fullname,
			username,
			avatarUrl
		});
		dest.writeParcelable(((BitmapDrawable) avatarDrawable).getBitmap(), 0);
	}
	
	public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
		public User createFromParcel(Parcel source) {
			return new User(source);
		}
		
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
		avatarDrawable = new BitmapDrawable((Bitmap) source.readParcelable(User.class.getClassLoader()));
	}
}
