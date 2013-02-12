package org.imaginationforpeople.android2.adapter;

import java.util.List;

import org.imaginationforpeople.android2.R;
import org.imaginationforpeople.android2.model.I4pProjectTranslation;
import org.imaginationforpeople.android2.model.Picture;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ProjectsGridAdapter extends BaseAdapter {
	private Activity activity;
	private List<I4pProjectTranslation> projects;
	
	public ProjectsGridAdapter(Activity a, List<I4pProjectTranslation> p) {
		activity = a;
		projects = p;
	}
	
	public void addProject(I4pProjectTranslation p) {
		projects.add(p);
		notifyDataSetChanged();
	}
	
	public void clearProjects() {
		projects.clear();
		notifyDataSetInvalidated();
	}
	
	public int getCount() {
		return projects.size();
	}

	public I4pProjectTranslation getItem(int position) {
		return projects.get(position);
	}

	public long getItemId(int position) {
		return projects.get(position).getId();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null)
			convertView = activity.getLayoutInflater().inflate(R.layout.projectslist_item, parent, false);
		
		I4pProjectTranslation project = getItem(position);
		TextView projectTitle = (TextView) convertView.findViewById(R.id.projectslist_item_text);
		ImageView projectImage = (ImageView) convertView.findViewById(R.id.projectslist_item_image);
		ProgressBar projectLoading = (ProgressBar) convertView.findViewById(R.id.projectslist_item_loading);
		projectTitle.setText(project.getTitle());
		projectTitle.getBackground().setAlpha(127);
		List<Picture> projectPictures = project.getProject().getPictures();
		if(projectPictures.size() > 0) {
			Bitmap thumb = projectPictures.get(0).getThumbBitmap();
			if(thumb == null) {
				projectImage.setVisibility(View.GONE);
				projectLoading.setVisibility(View.VISIBLE);
			} else {
				projectImage.setImageBitmap(thumb);
				projectLoading.setVisibility(View.GONE);
				projectImage.setVisibility(View.VISIBLE);
			}
		} else {
			projectLoading.setVisibility(View.GONE);
			projectImage.setImageResource(R.drawable.project_nophoto);
			projectImage.setVisibility(View.VISIBLE);
		}
		
		return convertView;
	}
	
	public List<I4pProjectTranslation> getProjects() {
		return projects;
	}
}
