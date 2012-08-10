package org.imaginationforpeople.android.activity;

import org.imaginationforpeople.android.R;
import org.imaginationforpeople.android.handler.ProjectsListHandler;
import org.imaginationforpeople.android.helper.LanguageHelper;
import org.imaginationforpeople.android.model.I4pProjectTranslation;
import org.imaginationforpeople.android.thread.ProjectsListThread;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ProjectsListActivity extends Activity implements OnClickListener {
	private static ProjectsListThread thread;
	private ArrayAdapter<I4pProjectTranslation> adapter;
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
		case R.id.projectslist_lang:
			languagesDialog.show();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.projectslist);
		
		// -- Initializing application
		preferences = getPreferences(Context.MODE_PRIVATE);
		LanguageHelper.setSharedPreferences(preferences);
		LanguageHelper.setResources(getResources());
		
		// -- Initializing projects list
		adapter = (ArrayAdapter<I4pProjectTranslation>) getLastNonConfigurationInstance();
		if(adapter == null)
			adapter = new ArrayAdapter<I4pProjectTranslation>(this, android.R.layout.simple_list_item_1);
		ListView list = (ListView) findViewById(R.id.projectslist);
		list.setAdapter(adapter);
		handler = new ProjectsListHandler(this, adapter);
		
		if(adapter.getCount() == 0)
			loadProjects();
		
		// -- Initializing language chooser UI
		int selectedLanguage = LanguageHelper.getPreferredLanguageInt();
		
		AlertDialog.Builder languagesBuilder = new AlertDialog.Builder(this);
		languagesBuilder.setTitle(R.string.projectslist_spinner_prompt);
		languagesBuilder.setSingleChoiceItems(R.array.projectslist_spinner_languages, selectedLanguage, this);
		languagesDialog = languagesBuilder.create();
	}
	
	@Override
	public Object onRetainNonConfigurationInstance() {
		return adapter;
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
