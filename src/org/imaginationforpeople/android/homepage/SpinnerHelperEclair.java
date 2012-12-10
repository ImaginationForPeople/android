package org.imaginationforpeople.android.homepage;

import org.imaginationforpeople.android.R;
import org.imaginationforpeople.android.adapter.ProjectsGridAdapter;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

public class SpinnerHelperEclair extends SpinnerHelper {
	private int selectedContent = 0;

	@Override
	public void init() {
		generateView(0);
	}

	@Override
	public void saveCurrentSelection(Bundle outState) {
		outState.putInt(STATE_KEY, selectedContent);
	}

	@Override
	public void restoreCurrentSelection(int position) {
		generateView(position);
	}
	
	@Override
	public int getCurrentSelection() {
		return selectedContent;
	}

	public void onClick(DialogInterface dialog, int which) {
		dialog.dismiss();
		generateView(which);
		selectedContent = which;
	}
	
	private void generateView(int id) {
		LinearLayout content = (LinearLayout) activity.findViewById(R.id.homepage_content);
		LayoutInflater inflater = activity.getLayoutInflater();
		View contentView = inflater.inflate(R.layout.projectslist, null);
		//GridView grid = (GridView) contentView.findViewById(android.R.id.list);
		//grid.setAdapter(adapters[id]);
		content.removeAllViews();
		content.addView(contentView);
		// Displaying content type in the application title
		activity.setTitle(activity.getResources().getStringArray(R.array.homepage_spinner_dropdown)[id]);
	}

	@Override
	public void displayContent(ProjectsGridAdapter adapter) {
		// TODO Auto-generated method stub
		
	}
}
