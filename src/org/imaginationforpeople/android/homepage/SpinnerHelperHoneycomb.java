package org.imaginationforpeople.android.homepage;

import org.imaginationforpeople.android.R;
import org.imaginationforpeople.android.adapter.ProjectsGridAdapter;
import org.imaginationforpeople.android.helper.DataHelper;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

@TargetApi(11)
public class SpinnerHelperHoneycomb extends SpinnerHelper implements OnNavigationListener {	
	@Override
	public void init() {
		super.init();
		
		// Hiding title here to keep application title on drawer
		activity.setTitle("");
		
		SpinnerAdapter spinner = ArrayAdapter.createFromResource(activity, R.array.homepage_spinner_dropdown, android.R.layout.simple_spinner_dropdown_item);;
		
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
	public int getCurrentSelection() {
		return activity.getActionBar().getSelectedNavigationIndex();
	}
	
	@Override
	public void displayContent(ProjectsGridAdapter adapter, boolean isUpdated) {
		if(!stopped) {
			ProjectsTabHoneycomb content;
			switch(getCurrentSelection()) {
			case DataHelper.CONTENT_COUNTRY:
				content = new ProjectsCountryTabHoneycomb(isUpdated);
				break;
			default:
				content = new ProjectsTabHoneycomb();
			}
			content.setAdapter(adapter);
			
			FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
			ft.replace(R.id.homepage_content, content);
			ft.commit();
			
			activity.loadImages(adapter);
		}
	}
	
	public boolean onNavigationItemSelected(int position, long itemId) {
		activity.changeContent((int) itemId);
		return true;
	}
	
	// This will never be called on Android 3.0+ 
	public void onClick(DialogInterface dialog, int which) {}
}
