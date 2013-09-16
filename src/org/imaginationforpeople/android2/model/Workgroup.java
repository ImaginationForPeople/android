package org.imaginationforpeople.android2.model;

import java.util.List;

public class Workgroup {
	private String name;
	private String language;
	private String description;
	private String image;
	private List<String> tags;
	private List<I4pProjectTranslation> projects;
	private List<User> subscribers;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public List<String> getTags() {
		return tags;
	}
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	public List<I4pProjectTranslation> getProjects() {
		return projects;
	}
	public void setProjects(List<I4pProjectTranslation> projects) {
		this.projects = projects;
	}
	public List<User> getSubscribers() {
		return subscribers;
	}
	public void setSubscribers(List<User> subscribers) {
		this.subscribers = subscribers;
	}
}
