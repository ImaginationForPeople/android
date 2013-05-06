package org.imaginationforpeople.android2.adapter;

import org.imaginationforpeople.android2.R;
import org.imaginationforpeople.android2.groupview.InfoGroupViewFragment;
import org.imaginationforpeople.android2.groupview.MembersGroupViewFragment;
import org.imaginationforpeople.android2.groupview.ProjectsGroupViewFragment;
import org.imaginationforpeople.android2.helper.DataHelper;
import org.imaginationforpeople.android2.model.Group;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class GroupViewAdapter extends FragmentPagerAdapter {
	private static int[] fragmentsName = {
		R.string.groupview_root_info,
		R.string.groupview_root_projects,
		R.string.groupview_root_members,
	};
	private static Fragment[] fragments = {
		new InfoGroupViewFragment(),
		new ProjectsGroupViewFragment(),
		new MembersGroupViewFragment(),
	};
	private static Resources resources;

	public GroupViewAdapter(FragmentManager fm, Group group, Resources r) {
		super(fm);
		resources = r;
		Bundle data = new Bundle();
		data.putParcelable(DataHelper.GROUP_VIEW_KEY, group);
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
