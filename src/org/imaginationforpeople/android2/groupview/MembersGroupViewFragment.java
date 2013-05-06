package org.imaginationforpeople.android2.groupview;

import org.imaginationforpeople.android2.R;
import org.imaginationforpeople.android2.adapter.UsersGridAdapter;
import org.imaginationforpeople.android2.helper.DataHelper;
import org.imaginationforpeople.android2.model.Group;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MembersGroupViewFragment extends Fragment {
	private Group group;
	private UsersGridAdapter adapter;
	
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
			
			GridView projectsLayout = (GridView) inflater.inflate(R.layout.projectslist, null);
			projectsLayout.setAdapter(adapter);
			return projectsLayout;
		}
	}
}
