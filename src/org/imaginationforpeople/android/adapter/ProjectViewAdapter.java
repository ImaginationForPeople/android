package org.imaginationforpeople.android.adapter;

import org.imaginationforpeople.android.R;
import org.imaginationforpeople.android.helper.DataHelper;
import org.imaginationforpeople.android.model.I4pProjectTranslation;
import org.imaginationforpeople.android.projectview.InfoProjectViewFragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ProjectViewAdapter extends FragmentPagerAdapter {
	private static int[] fragmentsName = {
		R.string.projectview_root_info
	};
	private static Fragment[] fragments = {
		new InfoProjectViewFragment()
	};
	private static Resources resources;

	public ProjectViewAdapter(FragmentManager fm, I4pProjectTranslation project, Resources r) {
		super(fm);
		resources = r;
		Bundle data = new Bundle();
		data.putParcelable(DataHelper.PROJECT_VIEW_KEY, project);
		for(Fragment fragment : fragments) {
			fragment.setArguments(data);
		}
	}

	@Override
	public Fragment getItem(int position) {
		return fragments[position];
	}

	@Override
	public int getCount() {
		return fragments.length;
	}
	
	@Override
    public CharSequence getPageTitle(int position) {
      return resources.getText(fragmentsName[position]);
    }
}
