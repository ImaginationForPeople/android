package org.imaginationforpeople.android2.activity;

import java.util.ArrayList;

import org.imaginationforpeople.android2.R;
import org.imaginationforpeople.android2.adapter.ProjectsGridAdapter;
import org.imaginationforpeople.android2.handler.BaseHandler;
import org.imaginationforpeople.android2.handler.ProjectsListHandler;
import org.imaginationforpeople.android2.handler.ProjectsListImageHandler;
import org.imaginationforpeople.android2.helper.DataHelper;
import org.imaginationforpeople.android2.helper.LanguageHelper;
import org.imaginationforpeople.android2.homepage.SpinnerHelper;
import org.imaginationforpeople.android2.homepage.SpinnerHelperEclair;
import org.imaginationforpeople.android2.homepage.SpinnerHelperHoneycomb;
import org.imaginationforpeople.android2.model.I4pProjectTranslation;
import org.imaginationforpeople.android2.shake.ShakeAnimation;
import org.imaginationforpeople.android2.shake.ShakeEventListener;
import org.imaginationforpeople.android2.shake.ShakeAnimation.AnimationListener;
import org.imaginationforpeople.android2.sqlite.FavoriteSqlite;
import org.imaginationforpeople.android2.thread.BaseGetJson;
import org.imaginationforpeople.android2.thread.ProjectsCountryListThread;
import org.imaginationforpeople.android2.thread.ProjectsListImagesThread;
import org.imaginationforpeople.android2.thread.ProjectsListThread;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SearchView;
import android.widget.Toast;

public class HomepageActivity extends Activity implements OnClickListener,
		OnCancelListener, ShakeEventListener.ShakeListener, AnimationListener, OnCheckedChangeListener {
	private static BaseGetJson[] threads = new BaseGetJson[DataHelper.CONTENT_NUMBER];
	private ProjectsListImagesThread imageThread;
	private ArrayList<ArrayList<I4pProjectTranslation>> projects = new ArrayList<ArrayList<I4pProjectTranslation>>(DataHelper.CONTENT_NUMBER);
	private ProjectsGridAdapter adapters[] = new ProjectsGridAdapter[DataHelper.CONTENT_NUMBER];
	private AlertDialog languagesDialog;
	private SharedPreferences preferences;
	private SpinnerHelper spinnerHelper;
	private AlertDialog contentDialog;
	private ShakeEventListener shaker;
	private ShakeAnimation animation;
	private LocationManager mLocationManager;
	private SearchView searchView;
	
	@TargetApi(11)
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.projectslist, menu);
		
		if(Build.VERSION.SDK_INT >= 11) {
			SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
			searchView = (SearchView) menu.findItem(R.id.homepage_search).getActionView();
			searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
			searchView.setIconifiedByDefault(true);
		}
		
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.homepage_content:
			AlertDialog.Builder contentBuilder = new AlertDialog.Builder(this);
			contentBuilder.setTitle(R.string.homepage_spinner_content_prompt);
			contentBuilder.setSingleChoiceItems(R.array.homepage_spinner_dropdown, spinnerHelper.getCurrentSelection()                                                                                                                                      , spinnerHelper);
			contentDialog = contentBuilder.create();
			contentDialog.show();
		case R.id.homepage_search:
			onSearchRequested();
			break;
		case R.id.homepage_favorites:
			FavoriteSqlite db = new FavoriteSqlite(this);
			if(db.hasFavorites()) {
				Intent intent = new Intent(this, FavoritesActivity.class);
				startActivity(intent);
			} else {
				Toast t = Toast.makeText(this, R.string.favorites_no, Toast.LENGTH_SHORT);
				t.show();
			}
			break;
		case R.id.homepage_lang:
			languagesDialog.show();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@TargetApi(11)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.homepage);
		// -- Initializing application
		preferences = getPreferences(Context.MODE_PRIVATE);
		LanguageHelper.setSharedPreferences(preferences);
		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		// -- Initializing projects list
		if(savedInstanceState != null)
			for(int i = 0; i < DataHelper.CONTENT_NUMBER; i++) {
				ArrayList<I4pProjectTranslation> project = savedInstanceState.getParcelableArrayList("projects_" + String.valueOf(i)); 
				projects.add(i, project); 
			}
		else
			for(int i = 0; i < DataHelper.CONTENT_NUMBER; i++) {
				projects.add(i, new ArrayList<I4pProjectTranslation>());
			}
		
		adapters = new ProjectsGridAdapter[DataHelper.CONTENT_NUMBER];
		for(int i = 0; i < DataHelper.CONTENT_NUMBER; i++) {
			adapters[i] = new ProjectsGridAdapter(this, projects.get(i));
		}
		
		if(Build.VERSION.SDK_INT >= 11)
			spinnerHelper = new SpinnerHelperHoneycomb();
		else
			spinnerHelper = new SpinnerHelperEclair();
		
		spinnerHelper.setActivity(this);
		spinnerHelper.init();
		
		if(savedInstanceState != null && savedInstanceState.containsKey(SpinnerHelper.STATE_KEY))
			spinnerHelper.restoreCurrentSelection(savedInstanceState.getInt(SpinnerHelper.STATE_KEY));
		
		// -- Initializing language chooser UI
		int selectedLanguage = LanguageHelper.getPreferredLanguageInt();
		
		AlertDialog.Builder languagesBuilder = new AlertDialog.Builder(this);
		languagesBuilder.setTitle(R.string.homepage_spinner_language_prompt);
		languagesBuilder.setSingleChoiceItems(R.array.homepage_spinner_languages, selectedLanguage, this);
		languagesDialog = languagesBuilder.create();
		
		// -- Initializing shaker
		shaker = new ShakeEventListener(this);
		if(Build.VERSION.SDK_INT >= 12)
			animation = new ShakeAnimation(findViewById(R.id.homepage_shake), this);
		else
			animation = new ShakeAnimation(this, findViewById(R.id.homepage_shake), this);
	}
	
	@TargetApi(11)
	@Override
	protected void onRestart() {
		super.onRestart();
		if(Build.VERSION.SDK_INT >= 11) {
			// Calling twice: first empty text field, second iconify the view
			searchView.setIconified(true);
			searchView.setIconified(true);
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		spinnerHelper.stopHandle();
		spinnerHelper.saveCurrentSelection(outState);
		for(int i = 0; i < DataHelper.CONTENT_NUMBER; i++)
			outState.putParcelableArrayList("projects_" + String.valueOf(i), projects.get(i));
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onPause() {
		shaker.unregisterListener();
		stopLocationListener();
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		spinnerHelper.startHandle();
		shaker.registerListener();
		
		changeContent(spinnerHelper.getCurrentSelection());
	}

	@Override
	protected void onStop() {
		super.onStop();
		spinnerHelper.stopHandle();
		requestStopThreads();
		if(languagesDialog.isShowing())
			languagesDialog.cancel();
	}
	
	public void onClick(DialogInterface dialog, int which) {
		if(which == DialogInterface.BUTTON_NEGATIVE) {
			finish();
		} else if(which != LanguageHelper.getPreferredLanguageInt()) {
			Editor editor = preferences.edit();
			editor.putInt("language", which);
			editor.commit();
			resetProjects();
		}
		dialog.dismiss();
	}
	
	public void onCancel(DialogInterface arg0) {
		finish();
	}
	
	public void changeContent(int content) {
		// Setting title on Android 2.x
		if(Build.VERSION.SDK_INT < 11)
			setTitle(getResources().getStringArray(R.array.homepage_spinner_dropdown)[content]);
		
		stopLocationListener();
		requestStopThreads();
		spinnerHelper.displayContent(adapters[content]);
		if(projects.get(content).size() == 0) {
			ProjectsListHandler handler = new ProjectsListHandler(this, spinnerHelper, adapters[content]);
			if(content == DataHelper.CONTENT_COUNTRY) {
				ArrayList<String> providers = new ArrayList<String>();
				if(mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
					providers.add(LocationManager.NETWORK_PROVIDER);
				if(mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
					providers.add(LocationManager.GPS_PROVIDER);
				
				if(providers.size() == 0) {
					Message msg = new Message();
					msg.arg1 = BaseHandler.STATUS_ERROR;
					msg.arg2 = BaseHandler.ERROR_LOCATION;
					handler.sendMessage(msg);
				}
				
				Geocoder mGeocoder = new Geocoder(this);
				threads[content] = new ProjectsCountryListThread(handler, content, mLocationManager, mGeocoder);
				
				for(String provider : providers)
					mLocationManager.requestLocationUpdates(provider, 0, 0, (LocationListener) threads[content]);
			} else {
				threads[content] = new ProjectsListThread(handler, content);
				threads[content].start();
			}
		} else
			loadImages(adapters[content]);
	}
	
	public void updateContent() {
		spinnerHelper.displayContent(adapters[spinnerHelper.getCurrentSelection()], true);
	}
	
	public void loadImages(ProjectsGridAdapter adapter) {
		if(imageThread != null && imageThread.isAlive())
			imageThread.requestStop();
		
		ProjectsListImageHandler handler = new ProjectsListImageHandler(adapter);
		imageThread = new ProjectsListImagesThread(handler, adapter.getProjects());
		imageThread.start();
	}
	
	public void resetProjects() {
		for(ProjectsGridAdapter adapter : adapters)
			adapter.clearProjects();
		changeContent(spinnerHelper.getCurrentSelection());
	}
	
	public void requestStopThreads() {
		for(int i = 0; i < DataHelper.CONTENT_NUMBER; i++) {
			if(threads[i] != null)
				threads[i].requestStop();
			if(imageThread != null && imageThread.isAlive())
				imageThread.requestStop();
		}
	}
	
	private void stopLocationListener() {
		if(threads[DataHelper.CONTENT_COUNTRY] != null)
			mLocationManager.removeUpdates((LocationListener) threads[DataHelper.CONTENT_COUNTRY]);
	}

	public void onShake() {
		Intent intent = new Intent(this, ProjectViewActivity.class);
		intent.putExtra("project_id", DataHelper.PROJECT_RANDOM);
		startActivity(intent);
	}
	
	public void onLittleShake() {
		if(preferences.getBoolean("shake_hint", true))
			animation.showAnimation();
	}
	
	public void onClick() {
		animation.hideAnimation();
		
		View shakeLayout = getLayoutInflater().inflate(R.layout.shake_dialog, null);
		CheckBox shakeCheckbox = (CheckBox) shakeLayout.findViewById(R.id.shake_checkbox);
		shakeCheckbox.setOnCheckedChangeListener(this);
		
		AlertDialog.Builder shakeAlert = new AlertDialog.Builder(this);
		shakeAlert.setIcon(android.R.drawable.ic_dialog_info);
		shakeAlert.setTitle(R.string.shake_dialog_title);
		shakeAlert.setView(shakeLayout);
		shakeAlert.setPositiveButton(android.R.string.ok, null);
		shakeAlert.create().show();
	}
	
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		Editor editor = preferences.edit();
		editor.putBoolean("shake_hint", !isChecked);
		editor.commit();
	}
}
