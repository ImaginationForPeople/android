package org.imaginationforpeople.android.homepage;

import org.imaginationforpeople.android.R;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.os.Bundle;

@TargetApi(11)
public class TabHelperHoneycomb extends TabHelper {	
	@Override
	public void init() {
		activity.getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		ProjectsTabHoneycomb bestFragment = new ProjectsTabHoneycomb();
		bestFragment.setAdapter(bestProjectsAdapter);
		ActionBar.Tab bestProjectsTab = activity.getActionBar().newTab();
		bestProjectsTab.setText(R.string.homepage_tab_bestof);
		bestProjectsTab.setTabListener(new ProjectsTabListenerHoneycomb(bestFragment));
		activity.getActionBar().addTab(bestProjectsTab);
		
		ProjectsTabHoneycomb latestFragment = new ProjectsTabHoneycomb();
		latestFragment.setAdapter(latestProjectsAdapter);
		ActionBar.Tab latestProjectsTab = activity.getActionBar().newTab();
		latestProjectsTab.setText(R.string.homepage_tab_latest);
		latestProjectsTab.setTabListener(new ProjectsTabListenerHoneycomb(latestFragment));
		activity.getActionBar().addTab(latestProjectsTab);
	}

	@Override
	public void saveCurrentTab(Bundle outState) {
		outState.putInt(STATE_KEY, activity.getActionBar().getSelectedTab().getPosition());
	}

	@Override
	public void restoreCurrentTab(int position) {
		activity.getActionBar().setSelectedNavigationItem(position);
	}
}
