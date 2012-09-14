package org.imaginationforpeople.android.helper;

public class UriHelper extends BaseHelper {
	// Public class with private constructor prevent instantiations
	private UriHelper() {}
	
	public static String getBestProjectsListUri() {
		//TODO: API base URL must be a shared parameter
		return "http://10.0.0.9:8000/api/v1/project/bestof/?format=json&lang=" + LanguageHelper.getPreferredLanguageCode();
	}
	
	public static String getLatestProjectsListUri() {
		return "http://10.0.0.9:8000/api/v1/project/latest/?format=json&lang=" + LanguageHelper.getPreferredLanguageCode();
	}
	
	public static String getProjectViewUri(int projectId) {
		return "http://10.0.0.9:8000/api/v1/project/" + String.valueOf(projectId);
	}
	
	public static String getRandomProjectViewUri() {
		return "http://10.0.0.9:8000/api/v1/project/random/?format=json&lang=" + LanguageHelper.getPreferredLanguageCode();
	}
}
