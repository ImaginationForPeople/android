package org.imaginationforpeople.android.homepage;

import org.imaginationforpeople.android.R;
import org.imaginationforpeople.android.activity.ProjectViewActivity;
import org.imaginationforpeople.android.adapter.ProjectsGridAdapter;
import org.imaginationforpeople.android.model.I4pProjectTranslation;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

@TargetApi(11)
public class ProjectsTabHoneycomb extends Fragment implements ProjectsTab, OnItemClickListener {
	private ProjectsGridAdapter adapter;
	
	public void setAdapter(ProjectsGridAdapter projectsGridAdapter) {
		adapter = projectsGridAdapter;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.projectslist, container, false);
		GridView grid = (GridView) view.findViewById(android.R.id.list);
		grid.setAdapter(adapter);
		grid.setOnItemClickListener(this);
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
