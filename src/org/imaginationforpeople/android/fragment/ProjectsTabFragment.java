package org.imaginationforpeople.android.fragment;

import org.imaginationforpeople.android.activity.ProjectViewActivity;
import org.imaginationforpeople.android.model.I4pProjectTranslation;

import android.annotation.TargetApi;
import android.app.ListFragment;
import android.content.Intent;
import android.view.View;
import android.widget.ListView;

@TargetApi(11)
public class ProjectsTabFragment extends ListFragment {
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		I4pProjectTranslation project = (I4pProjectTranslation) getListView().getItemAtPosition(position);
		
		Intent intent = new Intent(getActivity(), ProjectViewActivity.class);
		intent.putExtra("project_id", project.getId());
		intent.putExtra("project_title", project.getTitle());
		startActivity(intent);
	}
}
