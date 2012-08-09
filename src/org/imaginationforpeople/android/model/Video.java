package org.imaginationforpeople.android.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Video {
	private int id;
	@JsonProperty("video_url")
	private String videoUrl;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getVideoUrl() {
		return videoUrl;
	}
	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}
}
