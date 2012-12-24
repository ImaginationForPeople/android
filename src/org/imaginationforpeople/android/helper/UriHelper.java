package org.imaginationforpeople.android.helper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.imaginationforpeople.android.model.I4pProjectTranslation;

import android.location.Address;

public class UriHelper extends BaseHelper {
	public final static String BASE_URL = "http://192.168.1.164:8000";
	public final static String API_BASE_URL = BASE_URL + "/api/v1";
	
	// Public class with private constructor prevent instantiations
	private UriHelper() {}
	
	public static String getBestProjectsListUri() {
		return API_BASE_URL + "/project/bestof/?format=json&lang=" + LanguageHelper.getPreferredLanguageCode();
	}
	
	public static String getLatestProjectsListUri() {
		return API_BASE_URL + "/project/latest/?format=json&lang=" + LanguageHelper.getPreferredLanguageCode();
	}
	
	public static String getCurrentCountryProjectsListUri(Address address) {
		return API_BASE_URL + "/project/by-country/" + address.getCountryCode() + "/?format=json";
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
	
	public static String getProjectUrl(I4pProjectTranslation project) {
		return BASE_URL + "/" + project.getLanguageCode() + "/project/" + project.getSlug() + "/";
	}
	
	public static String getQuickSearchUrl(String search) {
		try {
			return API_BASE_URL + "/search/project/?q=" + URLEncoder.encode(search, "UTF-8") + "&format=json&limit=3&lang=" + LanguageHelper.getPreferredLanguageCode();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return API_BASE_URL + "/search/project/?q=" + search + "&format=json&limit=3&lang=" + LanguageHelper.getPreferredLanguageCode();
		}
	}
	
	public static String getFullSearchUrl(String search) {
		try {
			return API_BASE_URL + "/search/project/?q=" + URLEncoder.encode(search, "UTF-8") + "&format=json&lang=" + LanguageHelper.getPreferredLanguageCode();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return API_BASE_URL + "/search/project/?q=" + search + "&format=json&lang=" + LanguageHelper.getPreferredLanguageCode();
		}
	}
}
