package org.imaginationforpeople.android.homepage;

import org.imaginationforpeople.android.R;
import org.imaginationforpeople.android.activity.ProjectViewActivity;
import org.imaginationforpeople.android.adapter.ProjectsGridAdapter;
import org.imaginationforpeople.android.model.I4pProjectTranslation;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TabHost.TabContentFactory;

public class ProjectsTabEclair implements TabContentFactory, ProjectsTab, OnItemClickListener {
	private Activity activity;
	private ProjectsGridAdapter adapter;
	
	public ProjectsTabEclair(Activity a) {
		activity = a;
	}
	
	public void setAdapter(ProjectsGridAdapter adapter) {
		this.adapter = adapter;
	}
	
	public View createTabContent(String tag) {
		View view = LayoutInflater.from(activity).inflate(R.layout.projectslist, null, false);
		GridView grid = (GridView) view.findViewById(android.R.id.list);
		grid.setAdapter(adapter);
		grid.setOnItemClickListener(this);
		return view;
	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		I4pProjectTranslation project = adapter.getItem(position);
		
		Intent intent = new Intent(activity, ProjectViewActivity.class);
		intent.putExtra("project_id", project.getId());
		intent.putExtra("project_title", project.getTitle());
		activity.startActivity(intent);
	}
}
