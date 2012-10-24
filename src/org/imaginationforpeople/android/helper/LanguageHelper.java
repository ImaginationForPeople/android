package org.imaginationforpeople.android.helper;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.imaginationforpeople.android.R;

import android.content.SharedPreferences;

public class LanguageHelper extends BaseHelper {
	private static List<String> languagesCodes;
	
	private static SharedPreferences preferences;
	
	private LanguageHelper() {}
	
	public static void setSharedPreferences(SharedPreferences p) {
		preferences = p;
	}
	
	private static List<String> getAllLanguagesCodes() {
		if(languagesCodes == null)
			languagesCodes = Arrays.asList(getResources().getStringArray(R.array.homepage_spinner_keys));
		
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
	
	public static String getPreferredLanguageCode() {
		String languageCode;
		
		int intLanguage = preferences.getInt("language", -1);
		if(intLanguage == -1) {
			if(getAllLanguagesCodes().contains(Locale.getDefault().getLanguage()))
				languageCode = Locale.getDefault().getLanguage();
			else
				languageCode = "en";
		} else
			languageCode = getAllLanguagesCodes().get(intLanguage);
		
		return languageCode;
	}
}
