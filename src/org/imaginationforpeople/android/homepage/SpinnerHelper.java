package org.imaginationforpeople.android.homepage;

import org.imaginationforpeople.android.adapter.ProjectsGridAdapter;

import android.app.Activity;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

public abstract class SpinnerHelper implements OnClickListener {
	public final static String STATE_KEY = "selected";
	
	protected Activity activity;
	protected ProjectsGridAdapter[] adapters;
	
	public void setActivity(Activity activity) {
		this.activity = activity;
	}
	public void setAdapters(ProjectsGridAdapter[] a) {
		adapters = a;
	}
	
	public abstract void init();
	
	public abstract void saveCurrentSelection(Bundle outState);
	public abstract void restoreCurrentSelection(int position);
	public abstract int getCurrentSelection();
}
