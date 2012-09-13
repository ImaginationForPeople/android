package org.imaginationforpeople.android.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class I4pProjectTranslation {
	private int id;
	@JsonProperty("resource_uri")
	private String resourceUri;
	private String slug;
	@JsonProperty("language_code")
	private String languageCode;
	@JsonProperty("about_section")
	private String aboutSection;
	private String baseline;
	@JsonProperty("callto_section")
	private String calltoSection;
	@JsonProperty("partners_section")
	private String partnersSection;
	private I4pProject project;
	private String themes;
	private String title;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getResourceUri() {
		return resourceUri;
	}
	public void setResourceUri(String resourceUri) {
		this.resourceUri = resourceUri;
	}
	public String getSlug() {
		return slug;
	}
	public void setSlug(String slug) {
		this.slug = slug;
	}
	public String getLanguageCode() {
		return languageCode;
	}
	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}
	public String getAboutSection() {
		return aboutSection;
	}
	public void setAboutSection(String aboutSection) {
		this.aboutSection = aboutSection;
	}
	public String getBaseline() {
		return baseline;
	}
	public void setBaseline(String baseline) {
		this.baseline = baseline;
	}
	public String getCalltoSection() {
		return calltoSection;
	}
	public void setCalltoSection(String calltoSection) {
		this.calltoSection = calltoSection;
	}
	public String getPartnersSection() {
		return partnersSection;
	}
	public void setPartnersSection(String partnersSection) {
		this.partnersSection = partnersSection;
	}
	public I4pProject getProject() {
		return project;
	}
	public void setProject(I4pProject project) {
		this.project = project;
	}
	public String getThemes() {
		return themes;
	}
	public void setThemes(String themes) {
		this.themes = themes;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	//TODO: Overriding toString() method is a bad thing.
	@Override
	public String toString() {
		return title;
	}
}
