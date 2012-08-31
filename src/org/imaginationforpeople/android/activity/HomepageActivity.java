package org.imaginationforpeople.android.activity;

import java.util.ArrayList;

import org.imaginationforpeople.android.R;
import org.imaginationforpeople.android.adapter.ProjectsGridAdapter;
import org.imaginationforpeople.android.handler.AboutHandler;
import org.imaginationforpeople.android.handler.ProjectsListHandler;
import org.imaginationforpeople.android.helper.DataHelper;
import org.imaginationforpeople.android.helper.LanguageHelper;
import org.imaginationforpeople.android.homepage.TabHelper;
import org.imaginationforpeople.android.homepage.TabHelperEclair;
import org.imaginationforpeople.android.homepage.TabHelperHoneycomb;
import org.imaginationforpeople.android.model.I4pProjectTranslation;
import org.imaginationforpeople.android.thread.AboutThread;
import org.imaginationforpeople.android.thread.ProjectsListThread;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class HomepageActivity extends Activity implements OnClickListener, OnCancelListener {
	private static ProjectsListThread thread;
	private static AboutThread aboutThread;
	private SparseArray<ProjectsGridAdapter> adapters;
	private static ProgressDialog progress;
	private AlertDialog languagesDialog;
	private SharedPreferences preferences;
	private ProjectsListHandler handler;
	private TabHelper tabHelper;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.projectslist, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.homepage_lang:
			languagesDialog.show();
			break;
		case R.id.homepage_reload:
			loadProjects();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.homepage);
		
		// -- Initializing application
		preferences = getPreferences(Context.MODE_PRIVATE);
		LanguageHelper.setSharedPreferences(preferences);
		LanguageHelper.setResources(getResources());
		
		// -- Initializing projects list
		adapters = (SparseArray<ProjectsGridAdapter>) getLastNonConfigurationInstance();
		if(adapters == null) {
			adapters = new SparseArray<ProjectsGridAdapter>(); 
			adapters.append(DataHelper.BEST_PROJECTS_KEY, new ProjectsGridAdapter(this, new ArrayList<I4pProjectTranslation>()));
			adapters.append(DataHelper.LATEST_PROJECTS_KEY, new ProjectsGridAdapter(this, new ArrayList<I4pProjectTranslation>()));
		}
		
		if(Build.VERSION.SDK_INT >= 11)
			tabHelper = new TabHelperHoneycomb();
		else
			tabHelper = new TabHelperEclair();
		
		tabHelper.setActivity(this);
		tabHelper.setBestProjectsAdapter(adapters.get(DataHelper.BEST_PROJECTS_KEY));
		tabHelper.setLatestProjectsAdapter(adapters.get(DataHelper.LATEST_PROJECTS_KEY));
		tabHelper.init();
		
		if(savedInstanceState != null && savedInstanceState.containsKey(TabHelper.STATE_KEY))
			tabHelper.restoreCurrentTab(savedInstanceState.getInt(TabHelper.STATE_KEY));
		
		progress = new ProgressDialog(this);
		progress.setOnCancelListener(this);
		progress.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getText(R.string.cancel), this);
		
		handler = new ProjectsListHandler(this, progress, adapters.get(DataHelper.LATEST_PROJECTS_KEY), adapters.get(DataHelper.BEST_PROJECTS_KEY));
		
		if(adapters.get(DataHelper.BEST_PROJECTS_KEY).getCount() == 0 || adapters.get(DataHelper.LATEST_PROJECTS_KEY).getCount() == 0) {
			AboutHandler aboutHandler = new AboutHandler(this, progress);
			aboutThread = new AboutThread(aboutHandler);
			aboutThread.start();
		}
		
		// -- Initializing language chooser UI
		int selectedLanguage = LanguageHelper.getPreferredLanguageInt();
		
		AlertDialog.Builder languagesBuilder = new AlertDialog.Builder(this);
		languagesBuilder.setTitle(R.string.homepage_spinner_prompt);
		languagesBuilder.setSingleChoiceItems(R.array.homepage_spinner_languages, selectedLanguage, this);
		languagesDialog = languagesBuilder.create();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		tabHelper.saveCurrentTab(outState);
		super.onSaveInstanceState(outState);
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		return adapters;
	}

	@Override
	protected void onStop() {
		super.onStop();
		if(aboutThread != null)
			aboutThread.requestStop();
		if(thread != null)
			thread.requestStop();
		if(languagesDialog.isShowing())
			languagesDialog.cancel();
		progress.dismiss();
	}
	
	public void onClick(DialogInterface dialog, int which) {
		if(which == DialogInterface.BUTTON_NEGATIVE) {
			finish();
		} else if(which != LanguageHelper.getPreferredLanguageInt()) {
			Editor editor = preferences.edit();
			editor.putInt("language", which);
			editor.commit();
			loadProjects();
		}
		dialog.dismiss();
	}
	
	public void onCancel(DialogInterface arg0) {
		finish();
	}
	
	public void loadProjects() {
		thread = new ProjectsListThread(handler);
		thread.start();
	}
}
