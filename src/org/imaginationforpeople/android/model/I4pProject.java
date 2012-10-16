package org.imaginationforpeople.android.model;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class I4pProject implements Parcelable {
	private int id;
	@JsonProperty("best_of")
	private boolean bestOf;
	private Location location;
	private ArrayList<User> members;
	private ArrayList<Objective> objectives;
	private ArrayList<Picture> pictures;
	private ArrayList<Question> questions;
	private ArrayList<Reference> references;
	private String status;
	private ArrayList<Video> videos;
	private String website;
	
	public I4pProject() {}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean getBestOf() {
		return bestOf;
	}
	public void setBestOf(boolean bestOf) {
		this.bestOf = bestOf;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public ArrayList<User> getMembers() {
		return members;
	}
	public void setMembers(ArrayList<User> members) {
		this.members = members;
	}
	public ArrayList<Objective> getObjectives() {
		return objectives;
	}
	public void setObjectives(ArrayList<Objective> objectives) {
		this.objectives = objectives;
	}
	public ArrayList<Picture> getPictures() {
		return pictures;
	}
	public void setPictures(ArrayList<Picture> pictures) {
		this.pictures = pictures;
	}
	public ArrayList<Question> getQuestions() {
		return questions;
	}
	public void setQuestions(ArrayList<Question> questions) {
		this.questions = questions;
	}
	public ArrayList<Reference> getReferences() {
		return references;
	}
	public void setReferences(ArrayList<Reference> references) {
		this.references = references;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public ArrayList<Video> getVideos() {
		return videos;
	}
	public void setVideos(ArrayList<Video> videos) {
		this.videos = videos;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	
	public int describeContents() {
		return 0;
	}
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeBooleanArray(new boolean[] {bestOf});
		dest.writeStringArray(new String[] {
			status,
			website
		});
		dest.writeParcelable(location, 0);
		Bundle data = new Bundle();
		data.putParcelableArrayList("members", members);
		data.putParcelableArrayList("objectives", objectives);
		data.putParcelableArrayList("pictures", pictures);
		data.putParcelableArrayList("questions", questions);
		data.putParcelableArrayList("references", references);
		data.putParcelableArrayList("videos", videos);
		dest.writeBundle(data);
	}
	
	public static final Parcelable.Creator<I4pProject> CREATOR = new Parcelable.Creator<I4pProject>() {
		public I4pProject createFromParcel(Parcel source) {
			return new I4pProject(source);
		}
		
		public I4pProject[] newArray(int size) {
			return new I4pProject[size];
		}
	};
	
	private I4pProject(Parcel source) {
		id = source.readInt();
		boolean[] booleanData = new boolean[1];
		source.readBooleanArray(booleanData);
		bestOf = booleanData[0];
		String[] stringData = new String[2];
		source.readStringArray(stringData);
		status = stringData[0];
		website = stringData[1];
		location = source.readParcelable(Location.class.getClassLoader());
		Bundle bundleData = source.readBundle();
		members = bundleData.getParcelableArrayList("members");
		objectives = bundleData.getParcelableArrayList("objectives");
		pictures = bundleData.getParcelableArrayList("pictures");
		questions = bundleData.getParcelableArrayList("questions");
		references = bundleData.getParcelableArrayList("references");
		videos = bundleData.getParcelableArrayList("videos");
	}
}
