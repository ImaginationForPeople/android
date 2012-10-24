package org.imaginationforpeople.android.handler;

import org.imaginationforpeople.android.adapter.ProjectsGridAdapter;

import android.os.Handler;
import android.os.Message;

public class ProjectsListImageHandler extends Handler {
	private ProjectsGridAdapter adapter;
	
	public ProjectsListImageHandler(ProjectsGridAdapter a) {
		adapter = a;
	}
	
	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		
		adapter.notifyDataSetChanged();
	}

}
