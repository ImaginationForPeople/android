package org.imaginationforpeople.android2.handler;

import java.util.ArrayList;

import org.imaginationforpeople.android2.fragment.LoadingFragment;
import org.imaginationforpeople.android2.fragment.ProjectListFragment;
import org.imaginationforpeople.android2.model.I4pProjectTranslation;

import android.os.Bundle;

public class ProjectsListHandler extends BaseHandler {
	public final static int BEST_PROJECTS = 1001;
	public final static int LATEST_PROJECTS = 1002;

	private final int contentToLoad;
	private final LoadingFragment.UpdateLoadingScreenListener updateListener;
	private final LoadingFragment.OnContentLoadedListener loadedListener;
	private final LoadingFragment.OnLoadErrorListener errorListener;

	public ProjectsListHandler(int ctl, LoadingFragment.UpdateLoadingScreenListener usl, LoadingFragment.OnContentLoadedListener lcl, LoadingFragment.OnLoadErrorListener lel) {
		contentToLoad = ctl;
		updateListener = usl;
		loadedListener = lcl;
		errorListener = lel;
	}

	@Override
	protected void onStart(int arg, Object obj) {}

	@Override
	protected void onSpecificEvent(int arg, Object obj) {
		switch(arg) {
		case SPECIFIC_UPDATE:
			updateListener.updateLoadingScreen();
			break;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onSuccess(int arg, Object obj) {
		ArrayList<I4pProjectTranslation> projects = (ArrayList<I4pProjectTranslation>) obj;

		Bundle data = new Bundle();
		data.putParcelableArrayList(ProjectListFragment.PROJECTS_KEY, projects);
		loadedListener.onContentLoaded(contentToLoad, data);
	}

	@Override
	protected void onError(int arg, Object obj) {
		errorListener.onLoadError(arg);
	}
}
