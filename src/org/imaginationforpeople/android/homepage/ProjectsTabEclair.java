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
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class ProjectsTabEclair implements ProjectsTab, OnItemClickListener {
	protected Activity activity;
	protected ProjectsGridAdapter adapter;
	
	public ProjectsTabEclair(Activity a) {
		activity = a;
	}
	
	public void setAdapter(ProjectsGridAdapter adapter) {
		this.adapter = adapter;
	}
	
	public void display() {
		LinearLayout content = (LinearLayout) activity.findViewById(R.id.homepage_content);
		LayoutInflater inflater = activity.getLayoutInflater();
		View contentView;
		if(adapter.getCount() == 0) {
			contentView = inflater.inflate(R.layout.loading, null);
			contentView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			TextView message = (TextView) contentView.findViewById(R.id.loading_text);
			message.setText(activity.getResources().getString(R.string.loading_projects));
		} else {
			contentView = inflater.inflate(R.layout.projectslist, null); 
			GridView grid = (GridView) contentView.findViewById(android.R.id.list);
			grid.setAdapter(adapter);
			grid.setOnItemClickListener(this);
		}
		content.removeAllViews();
		content.addView(contentView);
	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		I4pProjectTranslation project = adapter.getItem(position);
		
		Intent intent = new Intent(activity, ProjectViewActivity.class);
		intent.putExtra("project_lang", project.getLanguageCode());
		intent.putExtra("project_slug", project.getSlug());
		intent.putExtra("project_title", project.getTitle());
		activity.startActivity(intent);
	}
}
