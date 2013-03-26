 package org.imaginationforpeople.android2.fragment;

import java.util.ArrayList;

import org.imaginationforpeople.android2.R;
import org.imaginationforpeople.android2.adapter.GroupsGridAdapter;
import org.imaginationforpeople.android2.handler.GroupsListImageHandler;
import org.imaginationforpeople.android2.model.Group;
import org.imaginationforpeople.android2.thread.GroupsListImagesThread;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class GroupListFragment extends Fragment {
	public static final String GROUPS_KEY = "GROUPS_KEY";
	
	private ArrayList<Group> groups;
	private GroupsGridAdapter adapter;
	private GroupsListImagesThread imageThread;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		groups = getArguments().getParcelableArrayList(GROUPS_KEY);
		adapter = new GroupsGridAdapter(getActivity(), groups);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.projectslist, container, false);
		GridView grid = (GridView) view.findViewById(android.R.id.list);
		grid.setAdapter(adapter);
		return view;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		GroupsListImageHandler handler = new GroupsListImageHandler(adapter);
		imageThread = new GroupsListImagesThread(handler, adapter.getGroups());
		imageThread.start();
	}
	
	@Override
	public void onStop() {
		if(imageThread != null && imageThread.isAlive())
			imageThread.requestStop();
		super.onStop();
	}
}
