package org.imaginationforpeople.android.adapter;

import org.imaginationforpeople.android.R;
import org.imaginationforpeople.android.helper.DataHelper;
import org.imaginationforpeople.android.model.I4pProject;

import android.app.Activity;
import android.os.Bundle;
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
		return project.getPictures().size() + project.getVideos().size();
	}

	public Bundle getItem(int position) {
		Bundle data = new Bundle();
		if(position < project.getPictures().size()) {
			data.putInt("type", DataHelper.PROJECT_GALLERY_GRID_TYPE_PICTURE);
			data.putInt("position", position);
		}
		else {
			data.putInt("type", DataHelper.PROJECT_GALLERY_GRID_TYPE_VIDEO);
			data.putParcelable("object", project.getVideos().get(position - project.getPictures().size()));
		}
		return data;
	}

	public long getItemId(int position) {
		if(position < project.getPictures().size())
			return project.getPictures().get(position).getId();
		else
			return project.getVideos().get(position - project.getPictures().size()).getId();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null)
			convertView = activity.getLayoutInflater().inflate(R.layout.projectview_gallery_item, null);
		convertView.setMinimumHeight(size);
		
		Bundle data = getItem(position);
		switch(data.getInt("type")) {
		case DataHelper.PROJECT_GALLERY_GRID_TYPE_PICTURE:
			((ImageView) convertView).setImageBitmap(project.getPictures().get(position).getThumbBitmap());
			break;
		case DataHelper.PROJECT_GALLERY_GRID_TYPE_VIDEO:
			((ImageView) convertView).setImageResource(R.drawable.projectview_gallery_video);
			break;
		}
		
		return convertView;
	}

}
