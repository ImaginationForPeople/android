package org.imaginationforpeople.android2.helper;

import org.imaginationforpeople.android2.helper.NetworkHelper.API_AUTH_TYPES;

/*************
 * IMPORTANT *
 *************
 * Rename this file to "ConfigHelper.java", edit configuration below
 * and move it to src/org/imaginationforpeople/android2/helper
 * unless the application can't be compiled!
 */

public class ConfigHelper {
	private ConfigHelper() {}

	public final static String BASE_URL = "http://imaginationforpeople.org";
	public final static String API_BASE_URL = BASE_URL + "/api/v1";

	public final static boolean API_REQUIRES_BASIC_AUTH = false;
	public final static String BASIC_AUTH_USER = "";
	public final static String BASIC_AUTH_PSWD = "";

	public final static API_AUTH_TYPES API_AUTH = API_AUTH_TYPES.NONE;
	public final static String API_AUTH_USER = "";
	public final static String API_AUTH_KEY = "";
}
