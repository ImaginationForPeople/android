package org.imaginationforpeople.android2.homepage;

import org.imaginationforpeople.android2.activity.HomepageActivity;
import org.imaginationforpeople.android2.adapter.ProjectsGridAdapter;

import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

public abstract class SpinnerHelper implements OnClickListener {
	public final static String STATE_KEY = "selected";
	
	protected HomepageActivity activity;
	protected boolean stopped = false;
	
	public void setActivity(HomepageActivity activity) {
		this.activity = activity;
	}
	
	public void init() {
		startHandle();
	}
	
	public abstract void displayContent(ProjectsGridAdapter adapter, boolean isUpdated);
	public void displayContent(ProjectsGridAdapter adapter) {
		displayContent(adapter, false);
	}
	
	public void startHandle() {
		stopped = false;
	}
	
	public void stopHandle() {
		stopped = true;
	}
	
	public abstract void saveCurrentSelection(Bundle outState);
	public abstract void restoreCurrentSelection(int position);
	public abstract int getCurrentSelection();
}
