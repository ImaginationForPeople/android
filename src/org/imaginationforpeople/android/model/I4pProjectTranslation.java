package org.imaginationforpeople.android.model;

public class I4pProjectTranslation {
	private int id;
	private String aboutSection;
	private String baseline;
	private String calltoSection;
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
}
