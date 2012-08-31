package org.imaginationforpeople.android.helper;

public class UriHelper extends BaseHelper {
	// Public class with private constructor prevent instantiations
	private UriHelper() {}
	
	public static String getProjectsListUri() {
		//TODO: API base URL must be a shared parameter
		return "http://10.0.0.9:8000/api/project/?lang=" + LanguageHelper.getPreferredLanguageCode();
	}
	
	public static String getProjectViewUri(int projectId) {
		return "http://10.0.0.9:8000/api/project/" + String.valueOf(projectId);
	}
	
	public static String getAboutUri() {
		return "http://10.0.0.9:8000/api/about/";
	}
}
