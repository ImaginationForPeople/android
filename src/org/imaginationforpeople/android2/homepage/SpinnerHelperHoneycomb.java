package org.imaginationforpeople.android2.homepage;

import org.imaginationforpeople.android2.R;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.OnNavigationListener;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

@TargetApi(11)
public class SpinnerHelperHoneycomb extends SpinnerHelper implements OnNavigationListener {
	private Activity activity;
	
	public SpinnerHelperHoneycomb(Activity a) {
		activity = a;
	}
	
	@Override
	public void init() {
		SpinnerAdapter spinner = ArrayAdapter.createFromResource(activity, R.array.homepage_spinner_dropdown, android.R.layout.simple_spinner_dropdown_item);
		
		activity.getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		activity.getActionBar().setListNavigationCallbacks(spinner, this);
	}
	
	@Override
	public void saveCurrentSelection(Bundle outState) {
		outState.putInt(STATE_KEY, activity.getActionBar().getSelectedNavigationIndex());
	}

	@Override
	public void restoreCurrentSelection(int position) {
		activity.getActionBar().setSelectedNavigationItem(position);
	}
	
	@Override
	public void displayCurrentContent() {
		activity.getActionBar().setSelectedNavigationItem(getCurrentSelection());
	}
	
	@Override
	public int getCurrentSelection() {
		return activity.getActionBar().getSelectedNavigationIndex();
	}
	
	public boolean onNavigationItemSelected(int position, long itemId) {
		listener.onSpinnerItemSelected((int) itemId);
		return true;
	}
	
	// This will never be called on Android 3.0+ 
	public void onClick(DialogInterface dialog, int which) {}
}
