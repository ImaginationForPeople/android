package org.imaginationforpeople.android.homepage;

import org.imaginationforpeople.android.adapter.ProjectsGridAdapter;

import android.app.Activity;
import android.os.Bundle;

public abstract class TabHelper {
	public final static String STATE_KEY = "selected_tab";
	
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
	
	public abstract void saveCurrentTab(Bundle outState);
	public abstract void restoreCurrentTab(int position);
}
