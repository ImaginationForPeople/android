package org.imaginationforpeople.android.homepage;

import org.imaginationforpeople.android.adapter.ProjectsGridAdapter;

import android.content.DialogInterface;
import android.os.Bundle;

public class SpinnerHelperEclair extends SpinnerHelper {
	private int selectedContent = 0;

	@Override
	public void init() {
		super.init();
		activity.changeContent(0);
	}

	@Override
	public void saveCurrentSelection(Bundle outState) {
		outState.putInt(STATE_KEY, selectedContent);
	}

	@Override
	public void restoreCurrentSelection(int position) {
		activity.changeContent(position);
		selectedContent = position;
	}
	
	@Override
	public int getCurrentSelection() {
		return selectedContent;
	}

	public void onClick(DialogInterface dialog, int which) {
		dialog.dismiss();
		activity.changeContent(which);
		selectedContent = which;
	}

	@Override
	public void displayContent(ProjectsGridAdapter adapter) {
		if(!stopped) {
			ProjectsTabEclair content = new ProjectsTabEclair(activity);
			content.setAdapter(adapter);
			content.display();
			
			activity.loadImages(adapter);
		}
	}
}
