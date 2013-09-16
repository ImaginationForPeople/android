package org.imaginationforpeople.android2.adapter;

import org.imaginationforpeople.android2.R;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DrawerAdapter extends BaseAdapter {
	public final static int DRAWER_PROJECTS_ITEM = 0;
	public final static int DRAWER_GROUPS_ITEM = 1;
	public final static int DRAWER_FAVORITES_ITEM = 2;

	private final Activity activity;
	private final String[] drawerItems;

	public DrawerAdapter(Activity a) {
		activity = a;
		drawerItems = activity.getResources().getStringArray(R.array.drawer_items);
	}

	@Override
	public int getCount() {
		return drawerItems.length;
	}

	@Override
	public String getItem(int position) {
		return drawerItems[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null)
			convertView = activity.getLayoutInflater().inflate(R.layout.drawer_list_item, parent, false);

		TextView text = (TextView) convertView.findViewById(android.R.id.text1);
		text.setText(getItem(position));

		ImageView icon = (ImageView) convertView.findViewById(android.R.id.icon);
		switch(position) {
		case DRAWER_PROJECTS_ITEM:
			icon.setImageResource(R.drawable.ic_menu_projects);
			break;
		case DRAWER_GROUPS_ITEM:
			icon.setImageResource(R.drawable.ic_menu_groups);
			break;
		case DRAWER_FAVORITES_ITEM:
			icon.setImageResource(R.drawable.ic_menu_favorites);
			break;
		}
		return convertView;
	}

}
