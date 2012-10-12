package org.imaginationforpeople.android.adapter;

import org.imaginationforpeople.android.R;
import org.imaginationforpeople.android.model.I4pProject;
import org.imaginationforpeople.android.model.Picture;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ProjectGalleryGridAdapter extends BaseAdapter {
	private Activity activity;
	private I4pProject project;
	private int size;
	
	public ProjectGalleryGridAdapter(Activity a, I4pProject p, int s) {
		activity = a;
		project = p;
		size = s;
	}
	
	public int getCount() {
		return project.getPictures().size();
	}

	public Picture getItem(int position) {
		return project.getPictures().get(position);
	}

	public long getItemId(int position) {
		return project.getPictures().get(position).getId();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null)
			convertView = activity.getLayoutInflater().inflate(R.layout.projectview_gallery_item, null);
		convertView.setMinimumHeight(size);
		((ImageView) convertView).setImageBitmap(getItem(position).getThumbBitmap());
		return convertView;
	}

}
