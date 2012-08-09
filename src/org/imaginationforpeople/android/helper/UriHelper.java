package org.imaginationforpeople.android.helper;

import java.util.Locale;

public class UriHelper {
	// Public class with private constructor prevent instantiations
	private UriHelper() {}
	
	public static String getProjectsListUri() {
		//TODO: API base URL must be a shared parameter
		return "http://10.0.0.9:8000/api/project/?lang=" + Locale.getDefault().getLanguage();
	}
}
