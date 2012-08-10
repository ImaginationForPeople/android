package org.imaginationforpeople.android.activity;

import org.imaginationforpeople.android.R;
import org.imaginationforpeople.android.handler.ProjectsListHandler;
import org.imaginationforpeople.android.model.I4pProjectTranslation;
import org.imaginationforpeople.android.thread.ProjectsListThread;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
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
		
		adapter = (ArrayAdapter<I4pProjectTranslation>) getLastNonConfigurationInstance();
		if(adapter == null)
			adapter = new ArrayAdapter<I4pProjectTranslation>(this, android.R.layout.simple_list_item_1);
		ListView list = (ListView) findViewById(R.id.projectslist);
		list.setAdapter(adapter);
		
		if(adapter.getCount() == 0) {
			ProjectsListHandler handler = new ProjectsListHandler(this, adapter);
			thread = new ProjectsListThread(handler);
			
			thread.start();
		}
		
		// -- Initializing language chooser UI
		AlertDialog.Builder languagesBuilder = new AlertDialog.Builder(this);
		languagesBuilder.setTitle(R.string.projectslist_spinner_prompt);
		languagesBuilder.setSingleChoiceItems(R.array.projectslist_spinner_languages, 1, this);
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
		dialog.dismiss();
	}
}
