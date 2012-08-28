package org.imaginationforpeople.android.homepage;

import org.imaginationforpeople.android.adapter.ProjectsGridAdapter;

import android.app.Activity;

public abstract class TabHelper {
	protected Activity activity;
	protected ProjectsGridAdapter bestProjectsAdapter, latestProjectsAdapter;
	
	public void setActivity(Activity activity) {
		this.activity = activity;
	}
	public void setBestProjectsAdapter(ProjectsGridAdapter bestProjectsAdapter) {
		this.bestProjectsAdapter = bestProjectsAdapter;
	}
	public void setLatestProjectsAdapter(ProjectsGridAdapter latestProjectsAdapter) {
		this.latestProjectsAdapter = latestProjectsAdapter;
	}
	
	public abstract void init();
}
