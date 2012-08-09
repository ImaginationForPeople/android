package org.imaginationforpeople.android.activity;

import org.imaginationforpeople.android.R;
import org.imaginationforpeople.android.handler.ProjectsListHandler;
import org.imaginationforpeople.android.model.I4pProjectTranslation;
import org.imaginationforpeople.android.thread.ProjectsListThread;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ProjectsListActivity extends Activity {
	private static ProjectsListThread thread;
	private ArrayAdapter<I4pProjectTranslation> adapter;
	
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
	}
	
	@Override
	public Object onRetainNonConfigurationInstance() {
		return adapter;
	}

	@Override
	protected void onStop() {
		super.onStop();
		thread.requestStop();
	}
}
