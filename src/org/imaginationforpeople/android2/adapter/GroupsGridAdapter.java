package org.imaginationforpeople.android2.adapter;

import java.util.List;

import org.imaginationforpeople.android2.R;
import org.imaginationforpeople.android2.model.Group;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class GroupsGridAdapter extends BaseAdapter {
	private Activity activity;
	private List<Group> groups;
	
	public GroupsGridAdapter(Activity a, List<Group> g) {
		activity = a;
		groups = g;
	}
	
	public void addGroup(Group g) {
		groups.add(g);
		notifyDataSetChanged();
	}
	
	public void clearProjects() {
		groups.clear();
		notifyDataSetInvalidated();
	}
	
	public int getCount() {
		return groups.size();
	}

	public Group getItem(int position) {
		return groups.get(position);
	}

	public long getItemId(int position) {
		return groups.get(position).getId();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null)
			convertView = activity.getLayoutInflater().inflate(R.layout.projectslist_item, parent, false);
		
		Group group = getItem(position);
		TextView groupTitle = (TextView) convertView.findViewById(R.id.projectslist_item_text);
		ImageView groupImage = (ImageView) convertView.findViewById(R.id.projectslist_item_image);
		ProgressBar groupLoading = (ProgressBar) convertView.findViewById(R.id.projectslist_item_loading);
		groupTitle.setText(group.getName());
		groupTitle.getBackground().setAlpha(127);
		if(group.getThumbUrl() != null) {
			Bitmap groupPicture = group.getThumbBitmap();
			if(groupPicture == null) {
				groupImage.setVisibility(View.GONE);
				groupLoading.setVisibility(View.VISIBLE);
			} else {
				groupImage.setImageBitmap(groupPicture);
				groupLoading.setVisibility(View.GONE);
				groupImage.setVisibility(View.VISIBLE);
			}
		} else {
			groupLoading.setVisibility(View.GONE);
			groupImage.setImageResource(R.drawable.project_nophoto);
			groupImage.setVisibility(View.VISIBLE);
		}
		
		return convertView;
	}
	
	public List<Group> getGroups() {
		return groups;
	}
}
