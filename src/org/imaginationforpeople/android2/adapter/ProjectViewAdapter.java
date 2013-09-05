package org.imaginationforpeople.android2.adapter;

import org.imaginationforpeople.android2.R;
import org.imaginationforpeople.android2.helper.DataHelper;
import org.imaginationforpeople.android2.model.I4pProjectTranslation;
import org.imaginationforpeople.android2.projectview.GalleryProjectViewFragment;
import org.imaginationforpeople.android2.projectview.InfoProjectViewFragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.actionbarsherlock.app.SherlockFragment;

public class ProjectViewAdapter extends FragmentStatePagerAdapter {
	private static int[] fragmentsName = {
		R.string.projectview_root_info,
		R.string.projectview_root_gallery
	};

	private static SherlockFragment[] fragments = {
		new InfoProjectViewFragment(),
		new GalleryProjectViewFragment()
	};

	private static Resources resources;

	public ProjectViewAdapter(FragmentManager fm, I4pProjectTranslation project, Resources r) {
		super(fm);
		resources = r;
		Bundle data = new Bundle();
		data.putParcelable(DataHelper.PROJECT_VIEW_KEY, project);
		for(SherlockFragment fragment : fragments) {
			fragment.setArguments(data);
		}
	}

	@Override
	public SherlockFragment getItem(int position) {
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
