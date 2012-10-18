package org.imaginationforpeople.android.projectview;

import java.util.List;

import org.imaginationforpeople.android.model.Picture;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class FullImageProjectViewAdapter extends FragmentPagerAdapter {
	private Fragment[] fragments;
	private List<Picture> pictures;
	
	public FullImageProjectViewAdapter(FragmentManager fm, List<Picture> p) {
		super(fm);
		pictures = p;
		fragments = new Fragment[pictures.size()];
	}

	@Override
	public Fragment getItem(int position) {
		if(fragments[position] == null) {
			Bundle data = new Bundle();
			data.putParcelable("picture", pictures.get(position));
			
			Fragment fragment = new FullImageProjectViewFragment();
			fragment.setArguments(data);
			
			fragments[position] = fragment;
		}
		return fragments[position];
	}

	@Override
	public int getCount() {
		return pictures.size();
	}
}
