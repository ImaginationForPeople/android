package org.imaginationforpeople.android2.helper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.imaginationforpeople.android2.model.Group;
import org.imaginationforpeople.android2.model.I4pProjectTranslation;

import android.location.Address;

public class UriHelper extends BaseHelper {
	// Public class with private constructor prevent instantiations
	private UriHelper() {}
	
	public static String getBestProjectsListUri() {
		return ConfigHelper.API_BASE_URL + "/project/bestof/?format=json&lang=" + LanguageHelper.getPreferredLanguageCode();
	}
	
	public static String getLatestProjectsListUri() {
		return ConfigHelper.API_BASE_URL + "/project/latest/?format=json&lang=" + LanguageHelper.getPreferredLanguageCode();
	}
	
	public static String getCurrentCountryProjectsListUri(Address address) {
		return ConfigHelper.API_BASE_URL + "/project/by-country/" + address.getCountryCode() + "/?format=json";
	}
	
	public static String getProjectViewUriById(int projectId) {
		return ConfigHelper.API_BASE_URL + "/project/" + String.valueOf(projectId);
	}
	
	public static String getProjectViewUriBySlug(String language_code, String slug) {
		return ConfigHelper.API_BASE_URL + "/project/" + language_code + "/" + slug;
	}
	
	public static String getPictureUploadUri() {
		return API_BASE_URL + "/picture/";
	}
	
	public static String getRandomProjectViewUri() {
		return ConfigHelper.API_BASE_URL + "/project/random/?format=json&lang=" + LanguageHelper.getPreferredLanguageCode();
	}
	
	public static String getProjectUrl(I4pProjectTranslation project) {
		return ConfigHelper.BASE_URL + "/" + project.getLanguageCode() + "/project/" + project.getSlug() + "/";
	}
	
	public static String getQuickSearchUrl(String search) {
		try {
			return ConfigHelper.API_BASE_URL + "/search/project/?q=" + URLEncoder.encode(search, "UTF-8") + "&format=json&limit=3&lang=" + LanguageHelper.getPreferredLanguageCode();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return ConfigHelper.API_BASE_URL + "/search/project/?q=" + search + "&format=json&limit=3&lang=" + LanguageHelper.getPreferredLanguageCode();
		}
	}
	
	public static String getFullSearchUrl(String search) {
		try {
			return ConfigHelper.API_BASE_URL + "/search/project/?q=" + URLEncoder.encode(search, "UTF-8") + "&format=json&lang=" + LanguageHelper.getPreferredLanguageCode();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return ConfigHelper.API_BASE_URL + "/search/project/?q=" + search + "&format=json&lang=" + LanguageHelper.getPreferredLanguageCode();
		}
	}
	
	public static String getGroupsListUri() {
		return ConfigHelper.API_BASE_URL + "/workgroup/?format=json";
	}
	
	public static String getGroupViewUriBySlug(String slug) {
		return ConfigHelper.API_BASE_URL + "/workgroup/" + slug + "/?format=json";
	}
	
	public static String getGroupUrl(Group group) {
		return ConfigHelper.BASE_URL + "/group/" + group.getSlug() + "/";
	}
}
