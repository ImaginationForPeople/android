package org.imaginationforpeople.android.activity;

import java.util.ArrayList;

import org.imaginationforpeople.android.R;
import org.imaginationforpeople.android.adapter.ProjectsGridAdapter;
import org.imaginationforpeople.android.handler.ProjectsListHandler;
import org.imaginationforpeople.android.handler.ProjectsListImageHandler;
import org.imaginationforpeople.android.helper.DataHelper;
import org.imaginationforpeople.android.helper.LanguageHelper;
import org.imaginationforpeople.android.homepage.SpinnerHelper;
import org.imaginationforpeople.android.homepage.SpinnerHelperEclair;
import org.imaginationforpeople.android.homepage.SpinnerHelperHoneycomb;
import org.imaginationforpeople.android.model.I4pProjectTranslation;
import org.imaginationforpeople.android.shake.ShakeAnimation;
import org.imaginationforpeople.android.shake.ShakeAnimation.AnimationListener;
import org.imaginationforpeople.android.shake.ShakeEventListener;
import org.imaginationforpeople.android.sqlite.FavoriteSqlite;
import org.imaginationforpeople.android.thread.ProjectsListImagesThread;
import org.imaginationforpeople.android.thread.ProjectsListThread;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

public class HomepageActivity extends Activity implements OnClickListener,
		OnCancelListener, ShakeEventListener.ShakeListener, AnimationListener, OnCheckedChangeListener {
	private static ProjectsListThread[] threads = new ProjectsListThread[DataHelper.CONTENT_NUMBER];
	private ProjectsListImagesThread imageThread;
	private ArrayList<ArrayList<I4pProjectTranslation>> projects = new ArrayList<ArrayList<I4pProjectTranslation>>(DataHelper.CONTENT_NUMBER);
	private ProjectsGridAdapter adapters[] = new ProjectsGridAdapter[DataHelper.CONTENT_NUMBER];
	private AlertDialog languagesDialog;
	private SharedPreferences preferences;
	private SpinnerHelper spinnerHelper;
	private AlertDialog contentDialog;
	private ShakeEventListener shaker;
	private ShakeAnimation animation;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.projectslist, menu);
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
		requestStopThreads();
		spinnerHelper.displayContent(adapters[content]);
		if(projects.get(content).size() == 0) {
			ProjectsListHandler handler = new ProjectsListHandler(this, spinnerHelper, adapters[content]);
			threads[content] = new ProjectsListThread(handler, content);
			threads[content].start();
		} else
			loadImages(adapters[content]);
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
