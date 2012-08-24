package org.imaginationforpeople.android.adapter;

import java.util.List;

import org.imaginationforpeople.android.R;
import org.imaginationforpeople.android.model.I4pProjectTranslation;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ProjectsGridAdapter extends BaseAdapter {
	private Activity activity;
	private List<I4pProjectTranslation> projects;
	
	public ProjectsGridAdapter(Activity a, List<I4pProjectTranslation> p) {
		activity = a;
		projects = p;
	}
	
	public void addProject(I4pProjectTranslation p) {
		projects.add(p);
		notifyDataSetChanged();
	}
	
	public void clearProjects() {
		projects.clear();
		notifyDataSetInvalidated();
	}
	
	public int getCount() {
		return projects.size();
	}

	public I4pProjectTranslation getItem(int position) {
		return projects.get(position);
	}

	public long getItemId(int position) {
		return projects.get(position).getId();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null)
			convertView = activity.getLayoutInflater().inflate(R.layout.projectslist_item, parent, false);
		
		I4pProjectTranslation project = getItem(position);
		TextView projectTitle = (TextView) convertView.findViewById(R.id.projectslist_item_text);
		projectTitle.setText(project.getTitle());
		projectTitle.getBackground().setAlpha(127); 
		
		return convertView;
	}

}
