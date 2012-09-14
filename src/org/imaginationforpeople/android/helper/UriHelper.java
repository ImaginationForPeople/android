package org.imaginationforpeople.android.helper;

public class UriHelper extends BaseHelper {
	public final static String API_BASE_URL = "http://10.0.0.5:8000/api/v1";
	
	// Public class with private constructor prevent instantiations
	private UriHelper() {}
	
	public static String getBestProjectsListUri() {
		return API_BASE_URL + "/project/bestof/?format=json&lang=" + LanguageHelper.getPreferredLanguageCode();
	}
	
	public static String getLatestProjectsListUri() {
		return API_BASE_URL + "/project/latest/?format=json&lang=" + LanguageHelper.getPreferredLanguageCode();
	}
	
	public static String getProjectViewUriById(int projectId) {
		return API_BASE_URL + "/project/" + String.valueOf(projectId);
	}
	
	public static String getProjectViewUriBySlug(String language_code, String slug) {
		return API_BASE_URL + "/project/" + language_code + "/" + slug;
	}
	
	public static String getRandomProjectViewUri() {
		return API_BASE_URL + "/project/random/?format=json&lang=" + LanguageHelper.getPreferredLanguageCode();
	}
}
