package org.imaginationforpeople.android2.groupview;

import org.imaginationforpeople.android2.R;
import org.imaginationforpeople.android2.adapter.UsersGridAdapter;
import org.imaginationforpeople.android2.handler.ListImageHandler;
import org.imaginationforpeople.android2.helper.DataHelper;
import org.imaginationforpeople.android2.model.Group;
import org.imaginationforpeople.android2.thread.UsersListImagesThread;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

public class MembersGroupViewFragment extends SherlockFragment {
	private Group group;
	private UsersGridAdapter adapter;
	private UsersListImagesThread thread;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		group = getArguments().getParcelable(DataHelper.GROUP_VIEW_KEY);

		if(group.getSubscribers().isEmpty()) {
			RelativeLayout infoLayout = (RelativeLayout) inflater.inflate(R.layout.information, null);
			TextView infoMessage = (TextView) infoLayout.findViewById(R.id.information_text);
			infoMessage.setText(R.string.groupview_subscribers_nosubscribers);
			return infoLayout;
		} else {
			adapter = new UsersGridAdapter(getActivity(), group.getSubscribers());

			GridView projectsLayout = (GridView) inflater.inflate(R.layout.list, null);
			projectsLayout.setAdapter(adapter);
			return projectsLayout;
		}
	}

	@Override
	public void onStart() {
		super.onStart();

		if(!group.getSubscribers().isEmpty()) {
			ListImageHandler handler = new ListImageHandler(adapter);
			thread = new UsersListImagesThread(handler, group.getSubscribers());
			thread.start();
		}
	}

	@Override
	public void onStop() {
		if(thread != null && thread.isAlive())
			thread.requestStop();
		super.onStop();
	}
}
