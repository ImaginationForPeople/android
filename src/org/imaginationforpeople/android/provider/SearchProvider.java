package org.imaginationforpeople.android.provider;

import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.imaginationforpeople.android.helper.UriHelper;
import org.imaginationforpeople.android.model.I4pProjectTranslation;
import org.json.JSONArray;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class SearchProvider extends ContentProvider {
	public static final String[] columns = {
		BaseColumns._ID,
		SearchManager.SUGGEST_COLUMN_TEXT_1,
		SearchManager.SUGGEST_COLUMN_INTENT_DATA,
		SearchManager.SUGGEST_COLUMN_INTENT_EXTRA_DATA
	};
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		return null;
	}

	@Override
	public boolean onCreate() {
		return true;
	}

	@Override
	public Cursor query(Uri contentUri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		String query = contentUri.getLastPathSegment().toLowerCase();
		MatrixCursor projects = new MatrixCursor(columns);
		
		// If the text field is empty, do not try to search projects
		if(SearchManager.SUGGEST_URI_PATH_QUERY.equals(query))
			return projects;
		
		BasicHttpParams basicHttpParams = new BasicHttpParams();
		// Connection must time out after 10 second to prevent infinite loop
		HttpConnectionParams.setConnectionTimeout(basicHttpParams, 10000);
		HttpClient httpClient = new DefaultHttpClient(basicHttpParams);
		
		HttpGet httpGet = new HttpGet();
		httpGet.setHeader("Accept", "application/json");
		try {
			URI uri = new URI(UriHelper.getQuickSearchUrl(query));
			httpGet.setURI(uri);
			HttpResponse response = httpClient.execute(httpGet);
			
			JsonFactory factory = new JsonFactory();
			ObjectMapper mapper = new ObjectMapper();
			
			JSONArray jsonProjects = new JSONArray(EntityUtils.toString(response.getEntity()));
			
			int jsonLength = jsonProjects.length();
			for(int i = 0; i < jsonLength; i++) {
				JsonParser parser = factory.createJsonParser(jsonProjects.getString(i));
				I4pProjectTranslation project = mapper.readValue(parser, I4pProjectTranslation.class);
				projects.addRow(new String[]{String.valueOf(i), project.getTitle(), project.getLanguageCode() + "/" + project.getSlug(), project.getTitle()});
			}
			
			return projects;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		return 0;
	}
}
