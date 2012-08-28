package org.imaginationforpeople.android.homepage;

import org.imaginationforpeople.android.R;
import org.imaginationforpeople.android.helper.DisplayHelper;

import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class TabHelperEclair extends TabHelper {
	public final static int tabHeight = DisplayHelper.dpToPx(50);
	private TabHost mTabHost;

	@Override
	public void init() {
		mTabHost = (TabHost) activity.findViewById(android.R.id.tabhost);
		mTabHost.setup();
		
		ProjectsTabEclair bestTab = new ProjectsTabEclair(activity);
		bestTab.setAdapter(bestProjectsAdapter);
		TabSpec bestProjectsTab = mTabHost.newTabSpec("best");
		bestProjectsTab.setIndicator(activity.getResources().getText(R.string.homepage_tab_bestof));
		bestProjectsTab.setContent(bestTab);
		mTabHost.addTab(bestProjectsTab);
		
		ProjectsTabEclair latestTab = new ProjectsTabEclair(activity);
		latestTab.setAdapter(latestProjectsAdapter);
		TabSpec latestProjectsTab = mTabHost.newTabSpec("latest");
		latestProjectsTab.setIndicator(activity.getResources().getText(R.string.homepage_tab_latest));
		latestProjectsTab.setContent(latestTab);
		mTabHost.addTab(latestProjectsTab);
		
		mTabHost.getTabWidget().getChildAt(0).getLayoutParams().height = tabHeight;
		mTabHost.getTabWidget().getChildAt(1).getLayoutParams().height = tabHeight;
	}

	@Override
	public void saveCurrentTab(Bundle outState) {
		outState.putInt(STATE_KEY, mTabHost.getCurrentTab());
	}

	@Override
	public void restoreCurrentTab(int position) {
		mTabHost.setCurrentTab(position);
	}
}
