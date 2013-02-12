package org.imaginationforpeople.android2.homepage;

import org.imaginationforpeople.android2.R;
import org.imaginationforpeople.android2.activity.ProjectViewActivity;
import org.imaginationforpeople.android2.adapter.ProjectsGridAdapter;
import org.imaginationforpeople.android2.model.I4pProjectTranslation;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

@TargetApi(11)
public class ProjectsTabHoneycomb extends Fragment implements ProjectsTab, OnItemClickListener {
	ProjectsGridAdapter adapter;
	
	public void setAdapter(ProjectsGridAdapter projectsGridAdapter) {
		adapter = projectsGridAdapter;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(adapter == null)
			return null;
		
		View view;
		if(adapter.getCount() == 0) {
			view = inflater.inflate(R.layout.loading, container, false);
			TextView message = (TextView) view.findViewById(R.id.loading_text);
			message.setText(getResources().getString(R.string.loading_projects));
		} else {
			view = inflater.inflate(R.layout.projectslist, container, false);
			GridView grid = (GridView) view.findViewById(android.R.id.list);
			grid.setAdapter(adapter);
			grid.setOnItemClickListener(this);
		}
		return view;
	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		I4pProjectTranslation project = adapter.getItem(position);
		
		Intent intent = new Intent(getActivity(), ProjectViewActivity.class);
		intent.putExtra("project_lang", project.getLanguageCode());
		intent.putExtra("project_slug", project.getSlug());
		intent.putExtra("project_title", project.getTitle());
		startActivity(intent);
	}
}
