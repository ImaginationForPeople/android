package org.imaginationforpeople.android2.groupview;

import org.imaginationforpeople.android2.R;
import org.imaginationforpeople.android2.activity.ProjectViewActivity;
import org.imaginationforpeople.android2.adapter.ProjectsGridAdapter;
import org.imaginationforpeople.android2.handler.ListImageHandler;
import org.imaginationforpeople.android2.helper.DataHelper;
import org.imaginationforpeople.android2.model.Group;
import org.imaginationforpeople.android2.model.I4pProjectTranslation;
import org.imaginationforpeople.android2.thread.ProjectsListImagesThread;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

public class ProjectsGroupViewFragment extends SherlockFragment implements OnItemClickListener {
	private Group group;
	private ProjectsGridAdapter adapter;
	private ProjectsListImagesThread thread;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		group = getArguments().getParcelable(DataHelper.GROUP_VIEW_KEY);

		if(group.getProjects().isEmpty()) {
			RelativeLayout infoLayout = (RelativeLayout) inflater.inflate(R.layout.information, null);
			TextView infoMessage = (TextView) infoLayout.findViewById(R.id.information_text);
			infoMessage.setText(R.string.groupview_projects_noprojects);
			return infoLayout;
		} else {
			adapter = new ProjectsGridAdapter(getActivity(), group.getProjects());

			GridView projectsLayout = (GridView) inflater.inflate(R.layout.list, null);
			projectsLayout.setAdapter(adapter);
			projectsLayout.setOnItemClickListener(this);
			return projectsLayout;
		}
	}

	@Override
	public void onStart() {
		super.onStart();

		if(!group.getProjects().isEmpty()) {
			ListImageHandler handler = new ListImageHandler(adapter);
			thread = new ProjectsListImagesThread(handler, group.getProjects());
			thread.start();
		}
	}

	@Override
	public void onStop() {
		if(thread != null && thread.isAlive())
			thread.requestStop();
		super.onStop();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		I4pProjectTranslation project = adapter.getItem(position);

		Intent intent = new Intent(getActivity(), ProjectViewActivity.class);
		intent.putExtra("project_lang", project.getLanguageCode());
		intent.putExtra("project_slug", project.getSlug());
		intent.putExtra("project_title", project.getTitle());
		startActivity(intent);
	}
}
