package org.imaginationforpeople.android.homepage;

import org.imaginationforpeople.android.R;

import android.annotation.TargetApi;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Fragment;
import android.app.FragmentTransaction;

@TargetApi(11)
public class ProjectsTabListenerHoneycomb implements TabListener {
	private Fragment fragment;
	
	public ProjectsTabListenerHoneycomb(Fragment f) {
		fragment = f;
	}
	
	public void onTabReselected(Tab tab, FragmentTransaction ft) {}

	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		ft.replace(R.id.homepage_fragment, fragment);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
	}

	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		ft.remove(fragment);
	}

}