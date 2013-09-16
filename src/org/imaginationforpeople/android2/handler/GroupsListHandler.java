package org.imaginationforpeople.android2.handler;

import java.util.ArrayList;

import org.imaginationforpeople.android2.fragment.GroupListFragment;
import org.imaginationforpeople.android2.fragment.LoadingFragment;
import org.imaginationforpeople.android2.model.Group;

import android.os.Bundle;

public class GroupsListHandler extends BaseHandler {
	private final LoadingFragment.OnContentLoadedListener loadedListener;
	private final LoadingFragment.OnLoadErrorListener errorListener;

	public GroupsListHandler(LoadingFragment.OnContentLoadedListener lcl, LoadingFragment.OnLoadErrorListener lel) {
		loadedListener = lcl;
		errorListener = lel;
	}

	@Override
	protected void onStart(int arg, Object obj) {}

	@Override
	protected void onSpecificEvent(int arg, Object obj) {}

	@SuppressWarnings("unchecked")
	@Override
	protected void onSuccess(int arg, Object obj) {
		ArrayList<Group> groups = (ArrayList<Group>) obj;

		Bundle data = new Bundle();
		data.putParcelableArrayList(GroupListFragment.GROUPS_KEY, groups);
		loadedListener.onContentLoaded(LoadingFragment.LOAD_GROUPS, data);
	}

	@Override
	protected void onError(int arg, Object obj) {
		errorListener.onLoadError(arg);
	}
}
