 package org.imaginationforpeople.android2.fragment;

import java.util.ArrayList;

import org.imaginationforpeople.android2.R;
import org.imaginationforpeople.android2.activity.GroupViewActivity;
import org.imaginationforpeople.android2.adapter.GroupsGridAdapter;
import org.imaginationforpeople.android2.handler.ListImageHandler;
import org.imaginationforpeople.android2.model.Group;
import org.imaginationforpeople.android2.thread.GroupsListImagesThread;

import com.actionbarsherlock.app.SherlockFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class GroupListFragment extends SherlockFragment implements OnItemClickListener {
	public static final String GROUPS_KEY = "GROUPS_KEY";
	
	private ArrayList<Group> groups;
	private GroupsGridAdapter adapter;
	private GroupsListImagesThread imageThread;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		groups = getArguments().getParcelableArrayList(GROUPS_KEY);
		adapter = new GroupsGridAdapter(getActivity(), groups);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.list, container, false);
		GridView grid = (GridView) view.findViewById(android.R.id.list);
		grid.setAdapter(adapter);
		grid.setOnItemClickListener(this);
		return view;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		ListImageHandler handler = new ListImageHandler(adapter);
		imageThread = new GroupsListImagesThread(handler, adapter.getGroups());
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
		Group group = adapter.getItem(position);
		
		Intent intent = new Intent(getActivity(), GroupViewActivity.class);
		intent.putExtra("group_slug", group.getSlug());
		intent.putExtra("group_title", group.getName());
		startActivity(intent);
	}
}
