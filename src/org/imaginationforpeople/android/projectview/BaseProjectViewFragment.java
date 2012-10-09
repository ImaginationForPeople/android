package org.imaginationforpeople.android.projectview;

import org.imaginationforpeople.android.model.I4pProjectTranslation;

import android.support.v4.app.Fragment;

public abstract class BaseProjectViewFragment extends Fragment {
	private I4pProjectTranslation project;
	
	public void setProject(I4pProjectTranslation p) {
		project = p;
	}
	
	protected I4pProjectTranslation getProject() {
		return project;
	}
}
