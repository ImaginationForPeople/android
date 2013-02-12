package org.imaginationforpeople.android2.homepage;

import org.imaginationforpeople.android2.adapter.ProjectsGridAdapter;
import org.imaginationforpeople.android2.helper.DataHelper;

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
		selectedContent = position;
		activity.changeContent(position);
	}
	
	@Override
	public int getCurrentSelection() {
		return selectedContent;
	}

	public void onClick(DialogInterface dialog, int which) {
		dialog.dismiss();
		selectedContent = which;
		activity.changeContent(which);
	}
	
	@Override
	public void displayContent(ProjectsGridAdapter adapter, boolean isUpdated) {
		if(!stopped) {
			ProjectsTabEclair content;
			switch(getCurrentSelection()) {
			case DataHelper.CONTENT_COUNTRY:
				content = new ProjectsCountryTabEclair(activity, isUpdated);
				break;
			default:
				content = new ProjectsTabEclair(activity);
				break;
			}
			content.setAdapter(adapter);
			content.display();
			
			activity.loadImages(adapter);
		}
	}
}
