package org.imaginationforpeople.android.activity;

import java.util.ArrayList;

import org.imaginationforpeople.android.R;
import org.imaginationforpeople.android.adapter.ProjectsGridAdapter;
import org.imaginationforpeople.android.handler.ProjectsListHandler;
import org.imaginationforpeople.android.helper.DataHelper;
import org.imaginationforpeople.android.helper.LanguageHelper;
import org.imaginationforpeople.android.homepage.TabHelper;
import org.imaginationforpeople.android.homepage.TabHelperEclair;
import org.imaginationforpeople.android.homepage.TabHelperHoneycomb;
import org.imaginationforpeople.android.model.I4pProjectTranslation;
import org.imaginationforpeople.android.thread.ProjectsListThread;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

@TargetApi(11)
public class HomepageActivity extends Activity implements OnClickListener {
	private static ProjectsListThread thread;
	private SparseArray<ProjectsGridAdapter> adapters;
	private AlertDialog languagesDialog;
	private SharedPreferences preferences;
	private ProjectsListHandler handler;
	
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
		
		TabHelper helper = null;
		if(Build.VERSION.SDK_INT >= 11)
			helper = new TabHelperHoneycomb();
		else
			helper = new TabHelperEclair();
		
		helper.setActivity(this);
		helper.setBestProjectsAdapter(adapters.get(DataHelper.BEST_PROJECTS_KEY));
		helper.setLatestProjectsAdapter(adapters.get(DataHelper.LATEST_PROJECTS_KEY));
		helper.init();
		
		if(Build.VERSION.SDK_INT >= 11 && savedInstanceState != null && savedInstanceState.containsKey("selected_tab"))
			getActionBar().setSelectedNavigationItem(savedInstanceState.getInt("selected_tab"));
		
		handler = new ProjectsListHandler(this, adapters.get(DataHelper.BEST_PROJECTS_KEY), adapters.get(DataHelper.LATEST_PROJECTS_KEY));
		
		if(adapters.get(DataHelper.BEST_PROJECTS_KEY).getCount() == 0 || adapters.get(DataHelper.LATEST_PROJECTS_KEY).getCount() == 0)
			loadProjects();
		
		// -- Initializing language chooser UI
		int selectedLanguage = LanguageHelper.getPreferredLanguageInt();
		
		AlertDialog.Builder languagesBuilder = new AlertDialog.Builder(this);
		languagesBuilder.setTitle(R.string.homepage_spinner_prompt);
		languagesBuilder.setSingleChoiceItems(R.array.homepage_spinner_languages, selectedLanguage, this);
		languagesDialog = languagesBuilder.create();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		if(Build.VERSION.SDK_INT >= 11)
			outState.putInt("selected_tab", getActionBar().getSelectedTab().getPosition());
		super.onSaveInstanceState(outState);
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		return adapters;
	}

	@Override
	protected void onStop() {
		super.onStop();
		thread.requestStop();
		if(languagesDialog.isShowing())
			languagesDialog.cancel();
	}
	
	public void onClick(DialogInterface dialog, int which) {
		if(which != LanguageHelper.getPreferredLanguageInt()) {
			Editor editor = preferences.edit();
			editor.putInt("language", which);
			editor.commit();
			loadProjects();
		}
		dialog.dismiss();
	}
	
	private void loadProjects() {
		thread = new ProjectsListThread(handler);
		thread.start();
	}
}
