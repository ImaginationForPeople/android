package org.imaginationforpeople.android2.fragment;

import java.util.ArrayList;

import org.imaginationforpeople.android2.R;
import org.imaginationforpeople.android2.activity.ProjectViewActivity;
import org.imaginationforpeople.android2.adapter.ProjectsGridAdapter;
import org.imaginationforpeople.android2.handler.ListImageHandler;
import org.imaginationforpeople.android2.model.I4pProjectTranslation;
import org.imaginationforpeople.android2.thread.ProjectsListImagesThread;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class ProjectListFragment extends Fragment implements OnItemClickListener {
	public final static String PROJECTS_KEY = "PROJECTS_KEY";
	
	private ArrayList<I4pProjectTranslation> projects;
	private ProjectsGridAdapter adapter;
	private ProjectsListImagesThread imageThread;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		projects = getArguments().getParcelableArrayList(PROJECTS_KEY);
		adapter = new ProjectsGridAdapter(getActivity(), projects);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.projectslist, container, false);
		GridView grid = (GridView) view.findViewById(android.R.id.list);
		grid.setAdapter(adapter);
		grid.setOnItemClickListener(this);
		return view;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		ListImageHandler handler = new ListImageHandler(adapter);
		imageThread = new ProjectsListImagesThread(handler, adapter.getProjects());
		imageThread.start();
	}
	
	@Override
	public void onStop() {
		if(imageThread != null && imageThread.isAlive())
			imageThread.requestStop();
		super.onStop();
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		I4pProjectTranslation project = adapter.getItem(position);
		
		Intent intent = new Intent(getActivity(), ProjectViewActivity.class);
		intent.putExtra("project_lang", project.getLanguageCode());
		intent.putExtra("project_slug", project.getSlug());
		intent.putExtra("project_title", project.getTitle());
		startActivity(intent);
	}
}