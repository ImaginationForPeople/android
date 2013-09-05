package org.imaginationforpeople.android2.fragment;

import java.util.ArrayList;

import org.imaginationforpeople.android2.R;
import org.imaginationforpeople.android2.activity.ProjectViewActivity;
import org.imaginationforpeople.android2.adapter.ProjectsGridAdapter;
import org.imaginationforpeople.android2.handler.ListImageHandler;
import org.imaginationforpeople.android2.model.I4pProjectTranslation;
import org.imaginationforpeople.android2.model.Picture;
import org.imaginationforpeople.android2.thread.ProjectsListImagesThread;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class ProjectListFragment extends Fragment implements OnItemClickListener {
	public final static String PROJECTS_KEY = "PROJECTS_KEY";

	public final static int PROJECT_VIEW_REQUEST_CODE = 1000;
	public final static String PROJECT_RESULT_EXTRA_SLUG = "PROJECT_RESULT_EXTRA_SLUG";
	public final static String PROJECT_RESULT_EXTRA_IMG_URL = "PROJECT_RESULT_EXTRA_IMG_URL";

	private ArrayList<I4pProjectTranslation> projects;
	private ProjectsGridAdapter adapter;
	private ProjectsListImagesThread imageThread;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		projects = getArguments().getParcelableArrayList(PROJECTS_KEY);
		adapter = new ProjectsGridAdapter(getActivity(), projects);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.list, container, false);
		GridView grid = (GridView) view.findViewById(android.R.id.list);
		grid.setAdapter(adapter);
		grid.setOnItemClickListener(this);
		return view;
	}

	@Override
	public void onStart() {
		super.onStart();

		ListImageHandler handler = new ListImageHandler(adapter);
		imageThread = new ProjectsListImagesThread(handler, adapter.getProjects());
		imageThread.start();
	}

	@Override
	public void onStop() {
		if(imageThread != null && imageThread.isAlive())
			imageThread.requestStop();
		super.onStop();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		I4pProjectTranslation project = adapter.getItem(position);

		Intent intent = new Intent(getActivity(), ProjectViewActivity.class);
		intent.putExtra("project_lang", project.getLanguageCode());
		intent.putExtra("project_slug", project.getSlug());
		intent.putExtra("project_title", project.getTitle());
		startActivityForResult(intent, PROJECT_VIEW_REQUEST_CODE);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == Activity.RESULT_OK) {
			String slug = data.getStringExtra(PROJECT_RESULT_EXTRA_SLUG);
			for(I4pProjectTranslation project : projects) {
				if(slug.equals(project.getSlug())) {
					String url = data.getStringExtra(PROJECT_RESULT_EXTRA_IMG_URL);
					if(project.getProject().getPictures().size() > 0)
						project.getProject().getPictures().get(0).setThumbUrl(url);
					else {
						Picture picture = new Picture();
						picture.setThumbUrl(url);
						project.getProject().getPictures().add(picture);
					}
					adapter.notifyDataSetChanged();
					break;
				}
			}
		}
	}
}