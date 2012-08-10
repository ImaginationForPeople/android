package org.imaginationforpeople.android.helper;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.imaginationforpeople.android.R;

import android.content.SharedPreferences;
import android.content.res.Resources;

public class LanguageHelper {
	private static List<String> languagesCodes;
	
	private static SharedPreferences preferences;
	private static Resources resources;
	
	private LanguageHelper() {}
	
	public static void setSharedPreferences(SharedPreferences p) {
		preferences = p;
	}
	
	public static void setResources(Resources r) {
		resources = r;
	}
	
	private static List<String> getAllLanguagesCodes() {
		if(languagesCodes == null)
			languagesCodes = Arrays.asList(resources.getStringArray(R.array.projectslist_spinner_keys));
		
		return languagesCodes;
	}
	
	public static int getPreferredLanguageInt() {
		// Getting user's preferred language
		int intLanguage = preferences.getInt("language", -1);
		
		if(intLanguage == -1)
			// Getting system language
			intLanguage = getAllLanguagesCodes().indexOf(Locale.getDefault().getLanguage());
		
		if(intLanguage == -1)
			// Setting language to english
			intLanguage = 1;
		
		return intLanguage;
	}
}
