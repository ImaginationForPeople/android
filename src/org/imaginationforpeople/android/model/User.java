package org.imaginationforpeople.android.model;

import android.graphics.Bitmap;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
	private String fullname;
	private String username;
	@JsonProperty("avatar")
	private String avatarUrl;
	private Bitmap avatarBitmap;
	
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
	public Bitmap getAvatarBitmap() {
		return avatarBitmap;
	}
	public void setAvatarBitmap(Bitmap avatarBitmap) {
		this.avatarBitmap = avatarBitmap;
	}
}
