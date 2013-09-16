package org.imaginationforpeople.android2.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Video implements Parcelable {
	private int id;
	@JsonProperty("video_url")
	private String videoUrl;

	public Video() {}

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

	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(videoUrl);
	}

	public static final Parcelable.Creator<Video> CREATOR = new Parcelable.Creator<Video>() {
		@Override
		public Video createFromParcel(Parcel source) {
			return new Video(source);
		}

		@Override
		public Video[] newArray(int size) {
			return new Video[size];
		}
	};

	private Video(Parcel source) {
		id = source.readInt();
		videoUrl = source.readString();
	}
}
