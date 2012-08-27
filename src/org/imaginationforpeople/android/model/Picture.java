package org.imaginationforpeople.android.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import android.graphics.Bitmap;

public class Picture {
	private int id;
	private String author;
	private String created;
	private String desc;
	private String license;
	private String source;
	@JsonProperty("thumb")
	private String thumbUrl;
	private Bitmap thumbBitmap;
	@JsonProperty("url")
	private String imageUrl;
	private Bitmap imageBitmap;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getLicense() {
		return license;
	}
	public void setLicense(String license) {
		this.license = license;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getThumbUrl() {
		return thumbUrl;
	}
	public void setThumbUrl(String thumbUrl) {
		this.thumbUrl = thumbUrl;
	}
	public Bitmap getThumbBitmap() {
		return thumbBitmap;
	}
	public void setThumbBitmap(Bitmap thumbBitmap) {
		this.thumbBitmap = thumbBitmap;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public Bitmap getImageBitmap() {
		return imageBitmap;
	}
	public void setImageBitmap(Bitmap imageBitmap) {
		this.imageBitmap = imageBitmap;
	}
}
