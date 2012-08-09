package org.imaginationforpeople.android.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class I4pProject {
	private int id;
	@JsonProperty("best_of")
	private boolean bestOf;
	private Location location;
	private List<User> members;
	private List<Objective> objective;
	private List<Picture> pictures;
	private List<Question> questions;
	private List<Reference> references;
	private String status;
	private List<Video> videos;
	private String website;
	
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
	public List<User> getMembers() {
		return members;
	}
	public void setMembers(List<User> members) {
		this.members = members;
	}
	public List<Objective> getObjective() {
		return objective;
	}
	public void setObjective(List<Objective> objective) {
		this.objective = objective;
	}
	public List<Picture> getPictures() {
		return pictures;
	}
	public void setPictures(List<Picture> pictures) {
		this.pictures = pictures;
	}
	public List<Question> getQuestions() {
		return questions;
	}
	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}
	public List<Reference> getReferences() {
		return references;
	}
	public void setReferences(List<Reference> references) {
		this.references = references;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<Video> getVideos() {
		return videos;
	}
	public void setVideos(List<Video> videos) {
		this.videos = videos;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
}
