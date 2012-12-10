package org.imaginationforpeople.android.homepage;

import org.imaginationforpeople.android.activity.HomepageActivity;
import org.imaginationforpeople.android.adapter.ProjectsGridAdapter;

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
	
	public abstract void displayContent(ProjectsGridAdapter adapter);
	
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
